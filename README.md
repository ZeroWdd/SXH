# SXH

## 项目简介 

分布式购物商城后端部分

* **使用springcloud实现分布式构建**
* **使用springboot实现项目的底层，去除xml配置**
* **前后端分离** 
  * **前台服务 ：  https://github.com/ZeroWdd/SXH-portal** 
  * **后台服务 ：  https://github.com/ZeroWdd/SXH-manage**
* **使用redis实现数据缓存，到达高并发**
* **消息队列采用rabbitmq**
* **采用mybaits框架对数据进行操作**
* **负载均衡使用nginx**

## 项目划分

* api后端服务 ：  https://github.com/ZeroWdd/SXH 
* 前台服务 ：  https://github.com/ZeroWdd/SXH-portal 
* 后台服务 ：  https://github.com/ZeroWdd/SXH-manage

## Web应用端口

| 名称     | 端口  | 说明         |
| -------- | ----- | ------------ |
| admin    | 8091  | 管理员模块   |
| auth     | 8087  | 身份校验模块 |
| cart     | 8088  | 购物车模块   |
| gateway  | 10010 | 网关         |
| order    | 8089  | 订单模块     |
| search   | 8083  | 搜索模块     |
| seckill  | 8092  | 秒杀模块     |
| sms      | 8086  | 短信模块     |
| stock    | 8090  | 库存模块     |
| user     | 8085  | 会员模块     |
| item     | 8081  | 商品模块     |
| registry | 10086 | 注册中心     |
| upload   | 8082  | 上传模块     |
| web      | 8084  | web模块      |

## 本地开发运行

* 创建数据库
* 更改application.yml中地址配置
* 修改本地`host`文件 将虚拟机地址 映射为 `www.leyou.com` 
* 配置nginx，配置文件在conf文件夹下
* 此项目采用jwt + rsa进行身份验证,因此需要更改部分application.yml的公钥和密钥地址

## 运行界面展示

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-20-15.jpg?Expires=1576839293&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=TubR%2BErnZuFS57%2BcJDovpl0Bz%2Fc%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-20-33.jpg?Expires=1576839322&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=Dn2ZaligO3ESDexEhEMEXs5Yt4U%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-25-18.jpg?Expires=1576839329&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=I96k%2B9E%2Bt89n1S7zN4ZiEd7VSkQ%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-25-35.jpg?Expires=1576839339&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=zEglVw5k8%2F2oQ%2BizKzCoSk3bYaU%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-01.jpg?Expires=1576839347&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=JCiLrhWlCAE2pODTZDZQhrh7Pns%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-28.jpg?Expires=1576839355&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=L0BRlscLPdnT4kNDhIIQYQu5MYE%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-43.jpg?Expires=1576839364&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=L75pZSVWsrRzRfGj0mdKDb76l3w%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-51.jpg?Expires=1576839372&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=PCyAMbJZ3IL4Eo0Cldgeezc7Nhg%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-27-03.jpg?Expires=1576839380&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=WucTfHGYBT%2BQPVlVg9EPUj7oq50%3D)

![]( https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-16-08.jpg?Expires=1576843004&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=JA6lnNcBCOyVDED9fCoSbkjbbOA%3D )

![]( https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-16-28.jpg?Expires=1576843144&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=TuA0gibmgUwDcbSzAADZJIMfFNk%3D )

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-16-58.jpg?Expires=1576843158&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=nEe5CBULCIEGQGobrV7iIEV3DV4%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-17-12.jpg?Expires=1576843170&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=HluYrg6UJw1WY90zoZTb7ydBt8E%3D)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-18-04.jpg?Expires=1576843182&OSSAccessKeyId=TMP.hhci3DLtUqMxf6KPdLCdmBqgi6ufhBfeCpxKeNgdstxHE669i5SzVP16jYWP6NJ2B3xF1mwWfBWqpJoaaskxqyr3qVRAgo5kT9mr5o7EUjiUkUp7YbYgBZ49Bqcwpd.tmp&Signature=tH253B5cpwnVCA0qx5neVLLm4KA%3D)