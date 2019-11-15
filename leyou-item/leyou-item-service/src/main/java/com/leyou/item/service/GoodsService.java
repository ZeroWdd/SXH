package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import com.leyou.item.vo.SpuVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2019/10/24 16:32
 * @Description:
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 分页条件查询spuVo
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Transactional
    public PageResult<SpuVo> querySpuVoByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 搜索条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        // 分页条件
        PageHelper.startPage(page, rows);

        // 执行查询
        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        List<SpuVo> spuVos = new ArrayList<>();
        spus.forEach(spu->{
            SpuVo spuVo = new SpuVo();
            // copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu, spuVo);
            // 查询分类名称
            List<String> names = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuVo.setCname(StringUtils.join(names, "/"));

            // 查询品牌的名称
            spuVo.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());

            spuVos.add(spuVo);
        });

        if(CollectionUtils.isEmpty(spuVos)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return new PageResult<>(pageInfo.getTotal(), spuVos);
    }

    /**
     * 新增商品
     * @param spuVo
     */
    @Transactional
    public void saveGoods(SpuVo spuVo) {
        try {
            // 新增spu
            spuVo.setSaleable(true);
            spuVo.setValid(true);
            spuVo.setCreateTime(new Date());
            spuVo.setLastUpdateTime(spuVo.getCreateTime());
            spuMapper.insert(spuVo);

            // 新增spuDetail
            SpuDetail spuDetail = spuVo.getSpuDetail();
            spuDetail.setSpuId(spuVo.getId());
            spuDetailMapper.insert(spuDetail);

            saveSkuAndStock(spuVo);
            //创建消息并发送
            sendMessage(spuVo.getId(),"insert");
        }catch (Exception e){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    private void saveSkuAndStock(SpuVo spuVo) {
        spuVo.getSkus().forEach(sku -> {
            // 新增sku
            sku.setSpuId(spuVo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(org.springframework.util.StringUtils.isEmpty(spuDetail)){
            throw new LyException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
     * 根据spuId查询skulist
     * @param spuId
     * @return
     */
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        skus.forEach(s -> {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(org.springframework.util.StringUtils.isEmpty(stock)){
                throw new LyException(ExceptionEnum.STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 更改商品
     * @param spuVo
     */
    @Transactional
    public void updateGoods(SpuVo spuVo) {
        try {
            // 查询以前sku
            List<Sku> skus = this.querySkusBySpuId(spuVo.getId());
            // 如果以前存在，则删除
            if(!CollectionUtils.isEmpty(skus)) {
                List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
                // 删除以前库存
                Example example = new Example(Stock.class);
                example.createCriteria().andIn("skuId", ids);
                stockMapper.deleteByExample(example);

                // 删除以前的sku
                Sku record = new Sku();
                record.setSpuId(spuVo.getId());
                skuMapper.delete(record);

            }
            // 新增sku和库存
            saveSkuAndStock(spuVo);

            // 更新spu
            spuVo.setLastUpdateTime(new Date());
            spuVo.setCreateTime(null);
            spuVo.setValid(null);
            spuVo.setSaleable(null);
            spuMapper.updateByPrimaryKeySelective(spuVo);

            // 更新spu详情
            spuDetailMapper.updateByPrimaryKeySelective(spuVo.getSpuDetail());

            //创建消息并发送
            sendMessage(spuVo.getId(),"update");
        }catch (Exception e){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
    }

    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return spu;
    }

    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            rabbitTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            //logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
            e.printStackTrace();
        }
    }

    public Sku querySkuById(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }
}
