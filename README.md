## e-Book
### 项目简介（功能要求）
1. 用户管理：管理员身份登录后，可以看到该功能；用户分为两种角色：顾客和管理员
	* 管理员可以禁用 / 解禁用户，被禁用的用户将无法登录系统。
2. 用户登录与注册
	* 用户登录需要输入用户名和密码，未输入点登录按钮时，提示用户必须输入。
	* 被禁用用户无法登录系统，并且会提示用户 "您的账号已经被禁用"。 
	* 根据用户名来确认其为管理员还是顾客，不同角色的界面具有差异。
	* 新用户注册时需要填写用户名、密码、重复密码、邮箱。
	* 需要校验用户名是否重复、两次输入的密码是否相同、邮箱是否符合格式要求。
3. 书籍管理：管理员身份登录后，可以看到该功能
	* 管理员可以浏览数据库中已有的书籍，以列表形式显示，包括书名、作者、封面、ISBN 编号和库存量。
	* 在列表上方提供搜索功能，管理员可以用书名来过滤想要查找的书籍。
	* 管理员在列表中可以修改每本图书的上述各种属性，包括书名、作者、封面、ISBN 编号和库存量。
	* 管理员可以删除旧图书，可以添加新图书。
4. 浏览书籍：此部分功能可以复用“书籍管理”中的功能
	* 顾客和管理员都可以浏览数据库中已有的书籍，以列表形式显示，包括书名、作者、封面、ISBN 编号和库存量。
	* 提供搜索功能，用户可以用书名来过滤想要查找的书籍。
	* 选中某本书后，通过Ajax 方式获取并显示书的详细信息。
5. 购买书籍
	* 当用户浏览书籍时，可以选择将某本书放入购物车。
	* 用户可以浏览购物车，看到自己放入购物车的所有书籍。
	* 在购物车中点击购买书籍之后，清空购物车，同时书籍库存相应地减少。
	* 购买书籍后，生成订单，展示给用户，并将订单存入数据库。
6. 订单管理
	* 顾客可以查看自己的所有订单，并且可以使用搜索功能来实现过滤，具体可以按照时间范围或书籍名称过滤。
	* 管理员可以查看系统中所有的订单，并且可以使用搜索功能来实现过
	滤，具体可以按照时间范围或书籍名称过滤。
7. 统计
	* 管理员可以统计在指定时间范围内各种书的销量情况，按照销售量排
	序，形成热销榜，以图或表的方式呈现。
	* 管理员可以统计在指定时间范围内每个用户的累计消费情况，按照购书进行排序，形成消费榜，以图或表的方式呈现。
	* 顾客可以统计在指定时间范围内自己购买书籍的情况 ，包括每种书购买了多少本，购书总本数和总金额。

### 技术栈
**前端**: Vue、WebPack </br>
**后端**: SpringBoot、Maven </br>
**数据库**: MySQL、Redis </br>
**中间件**: Activemq、WebSocket、Lucene、SOAP、WSDL、Eureka </br>

### 技术亮点
#### [Activemq](https://github.com/WilliamX1/bookstore/blob/main/hw/hw1/hw1.md)
使用 Activemq 实现下订单功能消息队列。当用户点击下订单时，后端 Controller 先使用生产者将用户请求转发给消费者，并迅速返回给用户该订单正在执行中。后端消费者将在空闲时期完成这笔交易。有效提高了下订单的效率和吞吐量
#### [WebSocket](https://github.com/WilliamX1/bookstore/blob/main/hw/hw2/hw2.md)
使用 WebSocket 实现一个在线聊天室，用户可以在其中进行实时群聊。
#### [Transaction](https://github.com/WilliamX1/bookstore/blob/main/hw/hw2/hw2.md)
运用增加 SpringBoot 注释方式，增加对下订单服务的事务控制功能，确保下订单动作和数据库的一致性和完整性。
#### [MultiThreading](https://github.com/WilliamX1/bookstore/blob/main/hw/hw3/hw3.md)
采用面向切口编程理念，对用户访问接口进行访问次数统计。且使用 **AtomicCounter** 进行原子性计数，保证多线程访问时数据安全可靠。
#### [Redis](https://github.com/WilliamX1/bookstore/blob/main/hw/hw3/hw3.md)
使用 Redis 数据库作书籍信息的缓存，大大减少 MySQL 数据库压力，提高访问速度和稳定性。
#### [Lucene](https://github.com/WilliamX1/bookstore/blob/main/hw/hw4/hw4.md)
使用 Lucene 全文搜索引擎，对书籍简介文本建立索引，使用户可以快速高效进行全文搜索。
#### [SOAP](https://github.com/WilliamX1/bookstore/blob/main/hw/hw4/hw4.md)
使用 SOAP Web Service，将书籍的全文搜索开发并部署成 Web Service，使得其他应用可以跨平台访问。
#### [microservice](https://github.com/WilliamX1/bookstore/blob/main/hw/hw5/hw5.md)
使用 Eureka Server 和 Eureka Client 构建 SpringBoot 微服务架构，通过将功能分解到各个离散的服务中以实现解耦，并使用 Netflix-Zuul 进行路由，实现负载均衡。

