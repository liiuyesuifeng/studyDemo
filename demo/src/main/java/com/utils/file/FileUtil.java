package com.utils.file;


import com.utils.PrintUtil;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public final class FileUtil {
    public static final String DEFAULT_PATH = "";
    public static final String DEFAULT_CODE_UTF8 = "UTF-8";
    public static final int DEFAULT_SIZE = 1024;
    public static final String RANDOM_FILE_STAT_RW = "rw";//读写

    private FileUtil() {

    }

    /**
     * 获取dir中满足正则表达式的目录
     *
     * @param dir
     * @param regex
     * @return
     */
    public static File[] local(File dir, final String regex) {
        return dir.listFiles(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);

            public boolean accept(File dir, String name) {
                return pattern.matcher(new File(name).getName()).matches();
            }
        });
    }

    /**
     * 获取指定路径下满足条件的目录
     *
     * @param path
     * @param regex
     * @return
     */
    public static File[] local(String path, final String regex) {
        return local(new File(path), regex);
    }

    /**
     * 获取File or  FilePath 中所有dir和file
     *
     * @param startFile
     * @param regex
     * @return TreeNode
     */
    public static FileTreeInfo getFileTreeInfo(File startFile, String regex) {
        return FileTreeInfo.walk(startFile, regex);
    }

    public static FileTreeInfo getFileTreeInfo(String startFilePath, String regex) {
        return FileTreeInfo.walk(startFilePath, regex);
    }

    public static FileTreeInfo getFileTreeInfo(String startFilePath) {
        return FileTreeInfo.walk(startFilePath);
    }

    /**
     * 按照一定编码格式读取文件类容返回字符串
     *
     * @param file     目标文件
     * @param fileCode 编码格式
     * @return 文件类容
     */
    public static String readFile(File file, String fileCode) {
        /*
        文件编码格式和项目编码格式不相同，需要用字节流编码转字符流
         */
        StringBuffer result = new StringBuffer();
        int lineNum = 0;
        try (FileInputStream inputStream = new FileInputStream(file);
             InputStreamReader castIO = new InputStreamReader(inputStream, fileCode);
             BufferedReader bufferRead = new BufferedReader(castIO);
        ) {
            String line;
            while ((line = bufferRead.readLine()) != null) {
                result.append(line + "\r\n");
            }
        } catch (IOException ioe) {
            PrintUtil.print("IO创建异常");
            ioe.printStackTrace();
        } finally {
            return result.toString();
        }
    }

    public static String readFile(String filePath, String fileCode) {
        File file = new File(filePath);
        return readFile(file, fileCode);
    }

    /**
     * 使用NIO方式读取文件内容
     *
     * @param file
     * @param fileCode
     * @return
     */
    public static String readFileInNIO(File file, String fileCode) {
        //声明字节流管道
        try (FileChannel channel = new FileInputStream(file).getChannel();
        ) {
            //获取字符流编码
            Charset charset = Charset.forName(fileCode);
            //申明字节流缓冲区
            ByteBuffer buff = ByteBuffer.allocate(DEFAULT_SIZE);
            //声明字符流缓冲区
            CharBuffer charBuffer = CharBuffer.allocate(DEFAULT_SIZE);
            //将文件写入字节流缓冲区
            channel.read(buff);
            //将postion指针移动到初始位置。limit移动到文本最后一个位置
            buff.flip();
            //以一定编码将字节流转换为字符流
            charset.newDecoder().decode(buff, charBuffer, false);
            //输出
            return charBuffer.toString();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return "";
    }

    /**
     * 默认格式读取文件类容
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        return readFile(file, DEFAULT_CODE_UTF8);
    }

    public static String readFile(String filePath) {
        File file = new File(filePath);
        return readFile(file);
    }

    /**
     * 创建/更新文件类容
     *
     * @param file     目标文件
     * @param fileCode 编码格式
     * @param data     文件类容
     * @return 状态
     */
    public static boolean writerFile(File file, String fileCode, String data) {
        try (FileOutputStream outputStream = new FileOutputStream(file);
             OutputStreamWriter castIO = new OutputStreamWriter(outputStream, fileCode);
             BufferedWriter bufferedWriter = new BufferedWriter(castIO);
             PrintWriter out = new PrintWriter(bufferedWriter);
        ) {
            out.println(data);
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            PrintUtil.print("创建IO失败，写入文件失败！");
            return false;
        }
    }

    public static boolean writerFile(String filePath, String fileCode, String data) {
        File file = new File(filePath);
        return writerFile(file, fileCode, data);
    }

    /**
     * NIO方式写入文件
     *
     * @param file
     * @param fileCode
     * @param data
     * @return
     */
    public static boolean writerFileNIO(File file, String fileCode, String data) {
        try (FileChannel channel = new FileOutputStream(file).getChannel();) {
            channel.write(ByteBuffer.wrap(data.getBytes(fileCode)));
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    public static boolean writerFile(File file, String data) {
        return writerFile(file, DEFAULT_CODE_UTF8, data);
    }

    public static boolean writerFile(String filePath, String data) {
        File file = new File(filePath);
        return writerFile(file, DEFAULT_CODE_UTF8, data);
    }

    /**
     * 拷贝文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param fileCode   编码格式
     * @return 状态
     */
    public static boolean copyFile(File sourceFile, File targetFile, String fileCode) {
        String data = readFile(sourceFile, fileCode);
        return writerFile(targetFile, fileCode, data);
    }

    public static boolean copyFile(String sourceFilePath, String targetFilePath, String fileCode) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        String data = readFile(sourceFile, fileCode);
        return writerFile(targetFile, fileCode, data);
    }

    public static boolean moveFile(File file, File moveDir) throws IOException {
        if (moveDir.isFile()) {
            throw new IOException("目标路径是文件！");
        }
        if (file.isFile() && file.exists()) {
            file.renameTo(moveDir);
            return true;
        } else {
            throw new IOException("该文件不存在或文件损坏！");
        }
    }

    /**
     * 移动文件
     *
     * @param file    目标文件
     * @param moveDir 目标目录
     * @return
     * @throws IOException
     */
    public static boolean moveFile(File file, String moveDir) throws IOException {
        File mvDir = new File(moveDir);
        return moveFile(file, moveDir);
    }

    @Test
    public void test1() {
        File file = new File("D:\\TW\\hashMap.txt");
        String out = readFile(file);
        PrintUtil.print(out);
    }

    @Test
    public void test2() {
        File file1 = new File("D:\\TW\\hashMap.txt");
        String out = readFile(file1);
        File file = new File("D:\\TW\\hashMap1.txt");
        boolean out1 = writerFile(file, out);
        PrintUtil.print(out1);
    }
}
