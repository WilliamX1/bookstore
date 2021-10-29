# hw6
#### id: 519021910861
#### name: xuhuidong
------
<font color=red> 以下是 pdf-11 内容 </font>

#### 要求
详细回答以下 4 个问题，并提交更新的数据库的设计方案，包括库结构、表结构和表与表之间的关联。

#### 数据库优化
##### Index - 索引
* 对于常用于 _query_ 操作的列分别建立索引。
* 所有的 MySQL 数据类型都可以建立索引。
* 不必要的索引会浪费空间和时间（让 MySQL 去决定使用哪个索引消耗时间）。
* 索引会增加 _insert_ , _update_ , _delete_ 的时间开销。 
* 绝大多数 MySQL 索引都存储在 B-tree。例外：
	1. 空间数据的索引使用 _R-tree_。
	2. _Memory table_ 支持_哈希表_。
	3. _InnoDB_ 对 _FULLTEXT_ 的索引使用_反向链表_。
* 对于 _小表_ 或者 _每次查询大部分数据的大表_ ，索引不重要。

##### 常用策略 - Optimizing Data Size
1. Table Columns
	* 使用更高效的（更小的）数据类型。$$eg：INT \rightarrow MEDIUMINT$$
	* 尽可能将 _Column_ 声明为 _NOT NULL_。
2. Row Format
	* 使用 _DYNAMIC_ 为默认。
	* 可以使用 _REDUNDANT_ ,  _COMPACT_ , _DYNAMIC_, _COMPARESSED_ 来减少使用空间。
3. Indexes
	* _primary index_ 应该尽可能短。只有需要频繁查询的地方才需要建立索引。
4. Joins
	* 一个经常 _scan_ 的表格可以拆分成两个表。
	* 不同表格中相同信息的 _column_ 应该被声明为相同数据类型。
	* 保持 _column_ 名字简单，方便跨表格使用。（简单名字，尽量不超过 18 个字符）
5. Normalization
	* 保持所有信息非冗余。
	* 为了提高速度可以适当放弃精简冗余信息。
##### 常用策略 - Optimizing MySQL Data Types
* **Optimizing for Numeric Data** - 尽可能选用 _numeric column_ 而不是 _string column_ 。
* **Optimizing for Character and String Types** - 1. 使用 _binary collation order_ 在快速比较和排序的操作上。2. 尽可能比较数据类型相同的列，减少类型转换。3. 对于少于 8 KB的 _column_ 使用 _binary VARCHAR_ 而不是 _BLOB_。
* **Optimizing for BLOB Types**。

### 作业
#### 1. E-Book 数据库应该建立什么样的索引？为什么？
1. 主键索引应该尽可能短。这使得每一行的识别和定位更加简便更加高效，而且如果有很多二级索引的话还能减少很多存储空间。
2. 仅在需要改善**查询**性能的地方建立索引。因为索引能提高查询操作的速度，但会降低插入和更新操作的速度。
3. 仅在字符串的不同前缀上建立索引而不是整个字符串上。因为短的索引可以节约空间，而且还能提高缓存命中率从而减少磁盘读写从而提高速度。
4. 选择唯一性索引。唯一性索引的值是惟一的，可以更快速的通过该索引来确定某条记录。
5. 为经常需要排序、分组和联合操作的字段建立索引。因为索引可以帮助上述操作避免排序操作，从而减少运行时间。

#### 2. E-Book 数据库中每个表中的字段类型和长度是如何确定的？为什么？
见后面 " **数据库更新方案** " 。

#### 3. 如果每个用户的邮箱只有一个，是否有必要将邮箱专门存储在一张表中，然后通过外键关联？为什么？
我认为没有必要单独将邮箱存储在一张表中。因为多开一张表不仅会造成存储空间浪费，而且由于用户邮箱只有一个，所以直接在用户信息中添加邮箱字段即可，完全没有新开一张表的必要。最后通过外键关联会降低查询操作性能，不如直接写在用户信息中快。

#### 4. 主键使用自增主键和 UUID 各自的优缺点是什么？
* 自增主键 优点：
	1. 字段长度小，能提高**读性能**。
	2. 新增的数据总在后面，大大提升**写性能**。
	3. 数据库自动编号，**速度快**，且按顺序存放，有利于**检索**。
	4. 数字型，**占用空间少**，**易排序**，程序间传递也较为方便。
	5. 通过非系统增加记录时，不用指定该字段，避免了**主键重复**的问题。
* 自增主键 缺点：
	1. 很容易被别人猜出，从而**被得知业务量和数据量**等，而且容易**被爬取数据**。
	2. **高并发时**，**竞争自增锁**会**降低数据库的吞吐能力**。
	3. **数据迁移时**，尤其是发生表格合并时，多个表容易**主键冲突**。

* UUID 优点：
	1. 保证**全局唯一性**，在数据拆分、合并存储时，绝对不会发生冲突。
	2. 可以在应用层生成，提高数据库吞吐能力。
	3. String 类型，易于写代码。
* UUID 缺点：
	1. **插入速度**慢，且造成硬盘使用率低。
	2. 比较大小时困难，**降低查询速度**。
	3. **占空间大**，且建立的索引越多，影响越严重。
	4. 读取出来的数据没有规律，通常需要 _order by_，消耗较多数据库资源。

### 数据库更新方案
#### 数据库结构
```sql
database e_book:
	table book 			/* 书籍信息 */
	table cartitem 		/* 购物车物品信息 */
	table chat			/* 聊天室聊天内容 */
	table orderitem		/* 订单物品信息 */
	table order			/* 订单信息 */
	table user			/* 用户信息 */
	table type			/* 书籍种类对应表 */
```
#### 表结构
##### Book 表结构如下：
```sql
table book:
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column name varchar(20)
	column author varchar(20)
	column type smallint NOT NULL INDEX REFERENCES type(id)
	column isbn char(13)
	column price int NOT NULL
	column introduction varchar(500)
	column image varchar(100)
	column inventory mediumint NOT NULL
	column state smallint default 1 INDEX
```
* _id_ 是自增主键，建立唯一性索引且非空，便于其他表格外键引用，能够提升表格查询性能。使用 _int_ 类型，由于是小型书籍网站，不容易有高并发或与其他数据库合并等操作，因此自增主键能够很好满足比较等操作而不影响数据库性能。
* _name_ 是书籍名称，没有使用 _bookname_，减少字段名称长度。且使用 _varchar(20)_ 将长度限制为 20 个字符以内，符合绝大部分书籍名称长度要求且节约了存储空间。
* _author_ 是书籍作者，考虑到作者可能有多个且可能是英文名，故采用 _varchar(20)_ 。满足了绝大部分书籍作者名称长度要求且节约了存储空间。
* _type_ 是书籍种类对应的数字，因为书籍种类并没有很多，所以用 _smallint_ 而不是 _int_ 节约了空间。且用外键指向表 _type_ 中的主键而不是直接使用 _varchar_，在 _type_ 中存储着编号对应的书籍种类名称（_string_ 类型），这样大大节约了存储空间。且在 _type_ 上建立索引，提高了查询某一类书籍的效率。
* _isbn_ 是书籍的 isbn 编号，经过查阅资料，目前 isbn 的长度均为 13 位，因此用固定长度的字符串存储。
* _price_ 是书籍的价格，用 _int_ 存储而不是 _float_ 或 _double_ 存储，避免了加减计算时的精度丢失问题，且满足了小型书籍网站的需求。不为空意味着每本书必须有价格，这也符合正常书籍网站卖书的特性。
* _introduction_ 是书籍的简介。经过网上调查，大部分书籍介绍不超过 500 个字符，故我们使用 _varchar(500)_ 来存储书籍简介。不用 _blob_ 或 _text_，符合上课讲述的 " 不超过 8 KB 的数据不用 BLOB 用 VARCHAR " 的原则，提高了比较和排序的效率。
* _image_ 是书籍的图片路径。由于还没有学习 MongoDB 数据库的使用，因此简单地存储书籍图片在本地中的相对路径，由于绝大多数相对路径不会超过 100 个字符，故使用 _varchar(100)_ 来存储。
* _inventory_ 是书籍的库存量。由于小型书籍网站的书籍库存不会太大，因此使用 _mediumint_ 来存储，节约磁盘空间。又因为买卖书籍时库存量是必须的，所以这个值不为空。
* _state_ 是书籍的当前状态。我们在删除书籍时使用假删除，故 _state_ 记录该书籍的状态，默认状态即为正常（1）。目前状态虽然只有已删除和正常两种，但考虑到之后可能会有正在审核等其他状态，所以不用 _bool_ 来存储，方便以后拓展。又每次展示首页书籍信息时，需要筛选出所有状态为正常的书籍信息，故在此字段上建立索引，减小查询开销。

##### cartitem 表结构如下：
```sql
table cartitem:
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column bookcount mediumint default 0
	column userid int REFERENCES user(id) UNIQUE INDEX
	column bookid int REFERENCES book(id) UNIQUE INDEX
```
* _id_ 是购物车物品的主键，使用自增主键满足基本需求，建立唯一性索引方便搜索和排序。
* _bookcount_ 是购物车物品的数量，和书籍的库存使用同一类型 _mediumint_ ，节约了存储空间。
* _userid_ 是指向表 _user_ 的 _id_ 主键的外键，拥有唯一性索引，提高查询性能。
* _bookid_ 是指向表 _book_ 的 _id_ 主键的外键，拥有唯一性索引，提高查询性能。

##### cart 表结构如下：
```sql
table chat:
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column msg varchar(255)
	column time datetime(6)
	column userid REFERENCES user(id) UNIQUE INDEX
```
* _id_ 是聊天记录的主键，使用自增主键满足基本需求，建立唯一性索引方便搜索。
* _msg_ 是聊天记录的聊天内容，因为是简单的聊天室，所以我们规定聊天内容必须是字符串，且不得超过 255 个字符，因此用 _varchar(255)_，不用 _blob_ 是为了提高查询性能。
* _time_ 是聊天记录的日期，使用 _datetime(6)_ 而不是 _timestamp_，方便按照年月日格式展现日期，且符合国际标准。
* _userid_ 是指向表 _user_ 的主键 _id_ 的外键。建立索引方便查询某个人的所有聊天记录。

##### orderitem 表结构如下：
```sql
table orderitem:
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column orderid int REFERENCES order(id) UNIQUE INDEX
	column bookid int REFERENCES book(id) UNIQUE INDEX
	column bookcount mediumint default 0
	column bookprice int NOT NULL
```
* _id_ 是订单物品的主键，使用自增主键满足基本需求，建立唯一性索引方便搜索。
* _orderid_ 是指向订单的主键的外键，建立索引方便查询某个订单中的全部订单项。
* _bookid_ 是指向书籍的主键的外键，建立索引方便查询。
* _bookcount_ 是该订单物品的个数，使用 _mediumint_ 而不是 _int_ 与书籍的库存保持一致，节省了存储空间。
* _bookprice_ 是该订单物品当时购买时的价格，不能用它来指向书籍的价格，因为书籍的价格会可能会变化而订单项的单价应该在下单那一刻即确认。使用 _int_ 类型且非空，能满足正常书籍销售网站的要求。

##### order 表结构如下：
```sql
table order:
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column userid int REFERENCES user(id) UNIQUE INDEX
	column time datetime(6)
	column price int NOT NULL
	column receivername varchar(20) default ""
	column address varchar(20) default ""
```
* _id_ 是订单的主键，使用自增主键满足基本需求，建立唯一性索引方便搜索。
* _userid_ 是指向用户的主键的外键，建立索引方便查询某个用户的全部订单。
* _time_ 是订单的生成时间，使用 _datetime(6)_ 而不是 _timestamp_，方便按照年月日格式展现日期，且符合国际标准。
* _price_ 是订单的总价。使用 _int_ 且非空，满足正常的书籍商城需求。
* _receivername_ 是订单收货人名称。因为中英文名字一般不多于 20 个字符，故使用 _varchar(20)_ 且默认为空，因为允许不填写收件人姓名。
* _address_ 是订单收货人地址。因为地址一般不超过 20 个字符，故使用 _varchar(20)_ 且默认为空，因为允许电子书不需要收件人地址。

##### user 表结构如下：
```sql
table user:
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column name varchar(20)
	column role bool default false
	column email varchar(50) INDEX
    column password varchar(15) default "123456"
    column state bool default true INDEX
```
* _id_ 是用户的主键，使用自增主键满足基本需求，建立唯一性索引方便搜索。
* _name_ 是用户的姓名，没有使用 _username_，减少字段名称长度。且使用 _varchar(20)_ 将长度限制为 20 个字符以内，符合绝大部分用户名称长度要求且节约了存储空间。
* _role_ 是用户角色，分为管理员和普通用户，因此仅需用 _bool_ 值来存储即可表示这两种角色。
* _email_ 是用户的电子邮箱。考虑到我们限制每个用户仅可以有一个电子邮箱，故直接放在用户信息表中。且绝大多数电子邮箱长度不超过 50 个字符，故使用 _varchar(50)_ 来存储。又因为用户使用电子邮箱进行登录，故按电子邮箱查询很频繁，所以在此字段上建立索引。
* _password_ 是用户的密码。考虑到用户密码不仅限于数字类型，故用 _varchar_ 来存储。我们规定用户密码不得超过 15 个字符，节约了存储空间。
* _state_ 是用户的状态，分为正常和被禁用两种，故仅需用 _bool_ 值来表示即可。

##### type 表结构如下：
```sql
table type：
	column id int PRIMARY KEY NOT NULL UNIQUE INDEX
	column name varchar(20)
```
* _id_ 是书籍种类的主键，使用自增主键满足基本需求，建立唯一性索引方便搜索。
* _name_ 是书籍种类的名称，我们规定不超过 20 个字符，故用 _varchar(20)_ 来存储。

### 参考
[11-mysql optimization 1.pdf](./11-mysql optimization 1.pdf)
https://www.cnblogs.com/goloving/p/13663276.html
https://zhuanlan.zhihu.com/p/88963084

------
<font color=red> 以下是 pdf-12 内容 </font>

### 要求
搜索参考文献，总结 InnoDB 和 MyISAM 两种存储引擎的主要差异，并详细说明你的 E-Book 项目应该选择哪种存储引擎。

### InnoDB 和 MyISAM 差异
| | MyISAM | InnoDB |
| :--: | :-- | :-- |
| 构成 | 每个 MyISQM 在磁盘上存储成 3 个文件。第一个文件的名字以表的名字开始，拓展名指出文件类型。.frm 文件存储表定义。.MYD 文件存储数据。.MYI 文件存储索引。| 基于磁盘的资源是 InnoDB 表空间数据文件和它的日志文件，InnoDB 表的大小只受限于操作系统文件的大小，一般为 2 GB。|
| 事务处理 | MyISAM 类型的表强调性能，其执行数度比 InnoDB 类型更快，但是不提供事务支持。 | InnoDB 提供事务支持事务，外键等高级数据库功能 |
| SELECT，UPDATE，INSERT 和 DELETE 操作 | 如果执行大量的 SELECT 操作，MyISAM 是更好的选择。 | 如果执行大量的 INSERT 或 UPDATE 操作，出于性能考虑应该选用 InnoDB 表。DELETE 操作时不会重新建立表，会一行一行的删除。LOAD 操作对 InnoDB 不起作用。|
| AUTO_INCREMENT | 每个表有一个 AUTO_INCREMENT 列的内部处理，对于 AUTO_INCREMENT 类型的字段可以和其他字段一起建立联合索引，能更好更快地处理 AUTO_INCREMENT。 | 自动增长计数器仅被储存在主内存中而不是磁盘中。|
| 表的具体行数 | `select count(*) from table`, MyISAM 只要简单的读出保存好的行数，注意的是，当 `count(*)` 语句包含 where 条件时，两种表的操作是一样的 |  InnoDB 中不保存表的具体行数，也就是说，执行 `select count(*) from table` 时，InnoDB 要扫描一遍整个表来计算有多少行 |
| 锁 | 表锁 | 提供行锁（locking on row level），提供与 Oracle 类型一致的不加锁读取(non-locking read in SELECTs)，另外，InnoDB 表的行锁也不是绝对的，如果在执行一个 SQL 语句时 MySQL 不能确定要扫描的范围，InnoDB 表同样会锁全表，例如 `update table set num=1 where name like "%aaa%"` 。|

### MyISAM 与 InnoDB 选择原则和 E-Book 选择分析
1. InnoDB 支持事务，而 MyISAM 不支持。因为 E-Book 中需要将下单服务做成事务，应选用 InnoDB。
2. MyISAM 适合查询以及插入为主的应用，InnoDB 适合频繁修改以及涉及到安全性较高的应用。在这点上 E-Book 没有明显倾向。
3. InnoDB 支持外键，MyISAM 不支持。因为在 E-Book 数据库中存在大量外键，应选用 InnoDB。

### 参考
[12-mysql optimization 2.pdf](./12-mysql optimization 2.pdf)
https://www.runoob.com/w3cnote/mysql-different-nnodb-myisam.html
