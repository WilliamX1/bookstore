package com.bookstore.bookstore.fulltextsearching;


public class FilesPositionConfig {

    private static final FilesPositionConfig filesPositionConfig = new FilesPositionConfig();

    FilesPositionConfig() {

    }

    public final static String indexPath = "backend\\index\\INDEX_PATH\\";
    public final static String docsDir = "backend\\index\\DOCS_PATH\\";
    public final static String docsType = "json";
    public static String docsPath(String pathName, String type) {
        return docsDir + pathName + "." + type;
    };
    public static String bookid2filePath(Integer id) {
        return docsPath("book-" + id, docsType);
    }
}
