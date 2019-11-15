package com.leyou.order.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.order.mapper.AddressMapper;
import com.leyou.order.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Auther: wdd
 * @Date: 2019/11/15 11:06
 * @Description:
 */
@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public Long addAddress(Address address) {
        Long addressId  = null;
        try {
            addressId = new Long(addressMapper.insert(address));
            if(addressId == null){
                throw new LyException(ExceptionEnum.ADDRESS_SAVE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnum.ADDRESS_SAVE_ERROR);
        }
        return addressId;
    }

    public void updataAddress(Address address) {
        try {
            int count = addressMapper.updateByPrimaryKeySelective(address);
            if(count != 1){
                throw new LyException(ExceptionEnum.ADDRESS_UPDATE_ERROR);
            }
        } catch (LyException e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnum.ADDRESS_UPDATE_ERROR);
        }

    }

    public Address queryAddress(Long addressId) {
        Address address = null;
        try {
            address = addressMapper.selectByPrimaryKey(addressId);
            if(StringUtils.isEmpty(address)){
                throw new LyException(ExceptionEnum.ADDRESS_NOT_FOUND);
            }
        } catch (LyException e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnum.ADDRESS_NOT_FOUND);
        }
        return address;

    }

    public void deleteAddress(Long addressId) {
        try {
            int count = addressMapper.deleteByPrimaryKey(addressId);
            if(count != 1){
                throw new LyException(ExceptionEnum.ADDRESS_DELETE_ERROR);
            }
        } catch (LyException e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnum.ADDRESS_DELETE_ERROR);
        }
    }
}
