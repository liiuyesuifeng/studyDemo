package com.utils.file;

import com.utils.PrintUtil;
import org.junit.Test;

import java.io.*;

public class BufferedInputFile {
    private static final String TEST_PATH = "D:\\TW\\";

    /**
     * 读取指定文件名称的内容
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readStr(String fileName) throws IOException {
        return readStr(new File(fileName));
    }

    public static String readStr(File file) throws IOException {
        /**
         * fileRead 继承自InputStreamReader,InputStreamReader又继承自Read
         * FileReader的构造方法中new 匿名FileInputStream
         * 调用super的构造方法传入到Read中lock接收
         */
        BufferedReader buffRead = new BufferedReader(new FileReader(file));
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = buffRead.readLine()) != null) {
            buffer.append(line + "\n");
        }
        buffRead.close();
        return buffer.toString();
    }

    @Test
    public void test1() {
        try {
            /*
            字符流读取文本
             */
            String all = readStr(TEST_PATH + "hashMap.txt");
            StringReader reader = new StringReader(all);
            int c;
            while ((c = reader.read()) != -1)
                PrintUtil.print((char) c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2() {
        try {
            /*
            字节流读取文本，一个中文占据两个字节，读取中文乱码
             */
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(readStr(TEST_PATH + "hashMap.txt").getBytes()));
            while (in.available() != 0) {
                PrintUtil.print((char) in.readByte());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
