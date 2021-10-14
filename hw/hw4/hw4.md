# hw3
#### id: 519021910861
#### name: xuhuidong
#### [项目 GitHub 网址](https://github.com/WilliamX1/bookstore.git/)
#### [项目后端源代码](./backend_src)
#### [项目前端源代码](./frontend_src)
------
<font color=red> 以下是 pdf-07 内容 </font>

### 要求
增加基于 Lucene 的针对书籍简介的全文搜索功能，用户可以在搜索界面输入搜索关键词，后端通过全文搜索引擎找到书籍简介中包含该关键词的书籍列表。

### 设计原理
#### Lucene 简介
Lucene 是一个开源的、成熟的全文索引与信息检索（IR）库，采用 Java 实现。信息检索指文档搜索、文档内信息搜索或者文档相关的元数据搜索等操作。Lucene 可以为 web 程序添加索引和搜索能力，使得可以转换成文本格式的任何数据能够被搜索。
#### Lucene 原理
主要分为**索引组件**和**搜索组件**两个步骤。
1. 索引组件
为了快速搜索大量文本，必须将文本内容转换成能够进行快速搜索的格式，从而消除慢速顺序扫描处理所带来的影响，得到检索位置，这个位置记录检索词出现的文件路径或者某个关键词。Lucene 采用**反向索引**机制，即维护一个词/短语表。整个索引过程包括：
* 获取内容
* 建立文档
* 文档分析
* 文档索引
2. 搜索组件
搜索是在一个索引中查找单词来找出它们所出现的文档的过程。搜索质量主要由**查准率**和**查全率**来衡量。查准率用来衡量搜索系统查找相关文档的能力，而查准率用来衡量搜索系统过滤相关文档的能力。
#### Lucene 概念
1. Analyzer
分析器，将字符串按照某种规则划分成一个个词语，并去除其中的无效词语，例如英文中的 "of"，中文中的"的"等词语，这些词语在文章中大量出现，但是本身不包含什么关键信息，去掉有利于缩小索引文件、提高效率和提高命中率。
2. Document
用户提供的源是一条条记录，它们可以是文本文件、字符串或者数据库表的一条记录等等。一条记录经过索引之后，就是以一个 Document 形式存储在索引文件中。用户进行搜索，也是以 Document 列表的形式返回。
3. Field
一个 Document 可以包含多个信息域，例如一篇文章可以包含"标题"、"正文"、"最后修改时间"等信息域，这些信息域就是通过 Field 在 Document 中存储的。
4. Term
搜索的最小单位，表示文档的一个词语。
5. Tocken
term 的一次出现，包含 term 文本和相应的起止偏移，以及一个类型字符串。一句话中可以出现多次相同的词语，它们都用同一个 term 表示，但是用不同的 tocken，每个 tocken 标记该词语出现的地方。
6. Segment
添加索引时并不是每个 document 都马上添加到同一个索引文件，它们首先被写入到不同的小文件，然后再合并成一个大索引文件，这里每个小文件都是一个 segment。
#### 设计方式
在管理员上传或者更改图书信息时将每本书**书籍简介**和**书籍 id**抽出写入一个单独的**json**文件，并利用 Lucene 为这些 document 建立索引。用户在前端进行全文搜索时，则直接从相应的索引文件中查找并返回书籍列表。

### 代码实现
#### 后端 SpringBoot 代码
[FilesPositionConfig]	定义程序索引文件接口和文件名生成方式等规范。
```Java
public class FilesPositionConfig {
	......
    public final static String indexPath = "index\\INDEX_PATH\\";
    public final static String docsDir = "index\\DOCS_PATH\\";
    public final static String docsType = "json";
    public static String docsPath(String pathName, String type) {
        return docsDir + pathName + "." + type;
    };
    public static String bookid2filePath(Integer id) {
        return docsPath("book-" + id, docsType);
    }
}
```
[ReadWriteFiles]	定义读写 JSON 文件方式，以及创建索引接口。
```Java
public class ReadWriteFiles {
	/* 创建索引和索引数据之间接口 */
	void create_docs_files(Integer id, String description, Boolean update) {
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("book_id", id);
        jsonObject.put("description", description);
        String filePath = FilesPositionConfig.bookid2filePath(id);
        try {
            writeFile(filePath, jsonObject);
            String[] args = {"-index", FilesPositionConfig.indexPath, "-docs", FilesPositionConfig.bookid2filePath(id),
                            update ? "-update" : "-not-update"};
            IndexFiles.index_interface(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/* 将 JSON 文件写入 DOCS_PATH */
    public static void writeFile(String filePath, JSONObject contents) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        out.write(contents.toJSONString());
        fw.close();
        out.close();
    }

    /* 读取 JSON 文件中特定 key 的 value */
    public static String readFile(String filePath, String key) throws IOException {
        BufferedReader reader = null;
        StringBuilder laststr = new StringBuilder();
        try {
            /* 通过路径获取流文件，这种形式能够确保在以 jar 包形式运行时也可以成功获取到文件 */
            InputStream inputStream = new FileInputStream(filePath);
            /* 设置字符编码为 UTF-8, 避免读取中文乱码 */
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            /* 通过 BufferedReader 进行读取 */
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) laststr.append(tempString);
            /* 关闭 BufferedReader */
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    /* 不管执行是否出现异常，必须确保关闭 BufferedReader */
                    reader.close();
                } catch (IOException ignored) {}
            }
        }
        JSONObject jsonObject = JSON.parseObject(laststr.toString());
        return jsonObject.get(key).toString();
    }
}
```
[IndexFiles]	为**json**文件建立索引，沿用老师所给样例。
[SearchFiles]	在建立的索引中进行全文搜索，沿用老师所给样例，增加返回列表。
```Java
public static List<Integer> search_interface(String[] args) throws Exception {
	...
	List<Integer> bookidLIst = doPagingSearch(...)
	...
	return bookidList;
}
public static List<Integer> doPagingSearch(...) {
	...
	/* 将书籍 book_id 添加到返回列表中 */
	bookidList.add(Integer.parseInt(ReadWriteFiles.readFile(doc.get("path"), "book_id")));
	...
}
```
[BookRepositoryImpl]	在修改书籍方法中添加增加索引步骤，增加全文搜索功能。
```Java
/* 修改图书信息 */
public Integer editBookInfo(Book book) {
	...
	/* 创建书籍信息 docs 并创建相应索引 */
	ReadWriteFiles.create_docs_files(book.getId(), book.getIntroduction(), true);
	...
}

/* 全文搜索书籍 */
public List<Book> fulltextSearchBook(String text) {
	List<Book> bookList = new ArrayList<>();
        try {
            String[] args = {"-index", FilesPositionConfig.indexPath, "-query", text};
            List<Integer> bookidList = SearchFiles.search_interface(args);
            for (Integer integer : bookidList) {
                Book book = getBookById(integer);
                /* 如果该书没有被删除 */
                if (book != null && book.getState() == 1) bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
}
```
[BookControllerImpl]	增加全文搜索访问接口。
```Java
@GetMappint("/book/fulltextSearchBook")
public ResponseEntity<List<Book>> fulltextSearchBook(String searchbookstr) {
        return new ResponseEntity<>(bookService.fulltextSearchBook(searchbookstr), HttpStatus.OK);
}
```
#### 前端 Vue 代码
[Home]	增加全文搜索按钮及功能
```Vue
<template>
    <el-menu-item>
        <el-switch
        v-model="searchtype"
        active-text="全文搜索"
        inactive-text="普通搜索">
        </el-switch>
    </el-menu-item>
</template>

export default {
	data() {
		return {
			...
			searchtype: true,
		}
	},
	methods: {
		/* 根据书名模糊搜索 & 全文搜索 */
		searchBook(searchbookstr) {
			...
			url: this.searchtype === true ? '.../fulltextSearchBook' : '.../searchBookByBookname'
		}
	}
}
```
### 代码运行结果
管理员上传/修改书籍信息时增加/修改相应索引。
![result1](./result1.png)
用户在前端进行全文搜索
![result3](./result3.png)
后端的全文搜索结果
![result2](./result2.png)

### 项目关联文件
[BookController](./BookController.java)
[BookRepositoryImpl](./BookRepositoryImpl.java)
[FilesPositionConfig](./FilesPositionConfig.java)
[ReadWriteFiles](./ReadWriteFiles.java)
[IndexFiles](./IndexFiles.java)
[SearchFiles](./SearchFiles.java)

### 参考
[07-searching.pdf](./07-searching.pdf)
https://developer.aliyun.com/article/25770
https://zhuanlan.zhihu.com/p/82483865
https://blog.csdn.net/yyunix/article/details/6887146
https://blog.csdn.net/Peacock__/article/details/84099685

------
<font color=red> 以下是 pdf-08 内容 </font>
### 要求
### 设计原理
### 代码实现
#### 后端 SpringBoot 代码
#### 前端 Vue 代码
### 代码运行结果
### 项目关联文件
### 参考

