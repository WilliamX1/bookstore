package com.bookstore.bookstore.fulltextsearching;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;


@Component
public class ReadWriteFiles {
    private static final FilesPositionConfig filesPositionConfig = new FilesPositionConfig();

//    public static void main(String[] args) {
//        create_docs_files(1, "oh ssg", false);
//    }

    public static void create_docs_files(Integer id, String description, Boolean update) {
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
//        out.println(contents.toJSONString());
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
                } catch (IOException ignored) {
                }
            }
        }
        JSONObject jsonObject = JSON.parseObject(laststr.toString());
        return jsonObject.get(key).toString();
    }
}
