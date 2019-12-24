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

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-16-08.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-16-28.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-16-58.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-17-12.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-18-04.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-20-15.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-20-33.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-25-18.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-25-35.jpg)

![]( https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-01.jpg)

![]( https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-28.jpg )

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-43.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-26-51.jpg)

![](https://github-image-save.oss-cn-beijing.aliyuncs.com/SXH/Snipaste_2019-12-20_17-27-03.jpg)