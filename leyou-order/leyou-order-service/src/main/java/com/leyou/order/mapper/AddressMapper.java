package com.leyou.order.mapper;

import com.leyou.order.pojo.Address;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Auther: wdd
 * @Date: 2019/11/15 11:07
 * @Description:
 */
public interface AddressMapper extends Mapper<Address> {
    @Update("update tb_address set receiver_default = 1 where address_id = #{addressId}")
    void updateAddressDefault(@Param("addressId") Long addressId);

    @Update("update tb_address set receiver_default = 0")
    void updateAddressDefaultAll();

}
