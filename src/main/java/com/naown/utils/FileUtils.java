package com.naown.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @USER: chenjian
 * @DATE: 2021/2/12 1:53 周五
 **/
public class FileUtils {

    /**
     * 系统临时目录
     * <br>
     * windows 包含路径分割符，但Linux 不包含,
     * 在windows \\==\ 前提下，
     * 为安全起见 同意拼装 路径分割符，
     * <pre>
     *       java.io.tmpdir
     *       windows : C:\Users/xxx\AppData\Local\Temp\
     *       linux: /temp
     * </pre>
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    /**
     * inputStream 转 File
     */
    static File inputStreamToFile(InputStream ins, String name) throws Exception {
        // 创建临时文件的File
        File file = new File(SYS_TEM_DIR + name);
        // 如果File存在则返回该File
        if (file.exists()) {
            return file;
        }
        // 否则创建文件输出流
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        int len = 8192;
        byte[] buffer = new byte[len];
        // 从ins中读取8192字节再写入file
        while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        // 关闭文件输出流
        os.close();
        // 关闭文件输入流
        ins.close();
        return file;
    }
}
