# hw8
#### id: 519021910861
#### name: xuhuidong
#### [项目后端源代码](./backend_src)
#### [项目前端源代码](./frontend_src)
------
<font color=red> 以下是 pdf-15 内容 </font>

### 要求
将合适的内容改造为在 MongoDB 中存储，例如书的产品评价或书评。可以参照课程样例将数据分别存储在 MySQL 和 MongoDB 中。

### 设计原理

#### MongoDB 简介
MongoDB 是一款开源的文档类型数据库，使用 C++ 编写而成，是 NoSQL 类型数据库的典型。

* MongoDB 特点
1. 面向文档进行存储。Document-Oriented Storage
2. 支持索引。Full Index Support
3. 可备份，高可用。Replication & High Availablity
4. 自动分区。Auto-Sharding
5. 支持条件查询。Querying
6. 快速原地更新。Fast In-Place Updates
7. 运用 Map/Reduce 加速。
8. GridFS
9. 商业版本。Commercial Support

* MongoDB 操作
1. `Create`：创建或插入操作会将新的 documents 添加到 collection 。如果该集合当前不存在，则插入操作将创建该集合。
```sql
db.collection.insertOne({key1: value1, key2: value2, ...})
db.collection.insertMany([{key1: value1, ...}, {key2: key2, ...}, ...])
```
2. `Read`：读取操作从 collection 检索 documents。即查询集合中的文档。
```sql
db.collection.find({ /* collection */
	{ age: { $gt: 18 } }, /* query criteria */
	{ name: 1, address: 1 } /* projection */
}).limit(5) /* cursor modifier */
```
3. `Update`：更新操作会修改 collection 中的现有 documents。
```sql
db.collection.updateOne(...)
db.collection.updateMany({ /* collection */
	{ age: { $lt: 18 } },	/* update filter */
	{ $set: { status: "reject" } } /* update action */
})
db.collection.replaceOne(...)
```
4. `Delete`：删除操作从集合中删除文档。
```sql
db.collection.deleteOne(...)
db.collection.deleteMant({ /* collection */
	{ status: "reject" } /* delete filter */
})
```

### 代码实现
#### 后端 SpringBoot 代码

### 代码运行结果

### 项目关联文件

### 参考
https://www.docs4dev.com/docs/zh/mongodb/v3.6/reference/reference-method-db.collection.insertMany.html
