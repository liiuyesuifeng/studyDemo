package com.test;

import com.utils.FileUtil;
import com.utils.NewUtils;
import com.utils.PrintUtil;
import org.junit.Test;

import java.beans.Transient;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;


public class FileTestYS {
    /**
     * ZIP
     */
    @Test
    public void test1() throws Exception{
        //读取的数据源
        String data = FileUtil.readFile(new File(FileUtil.DEFAULT_PATH + "\\index1.html"),FileUtil.DEFAULT_CODE_UTF8);
        String data1 = FileUtil.readFile(new File(FileUtil.DEFAULT_PATH + "\\index.html"),FileUtil.DEFAULT_CODE_UTF8);
        List<String> names = new ArrayList<>();
        names.add("index.html");
        names.add("index1.html");
        //创建压缩文件并建立连接，声明IO
        FileOutputStream fops = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test.zip");
        //教研输出流
        CheckedOutputStream cops = new CheckedOutputStream(fops,new Adler32());
        //创建压缩流
        ZipOutputStream zip = new ZipOutputStream(cops);
        OutputStreamWriter osw = new OutputStreamWriter(zip,FileUtil.DEFAULT_CODE_UTF8);
        BufferedWriter bw = new BufferedWriter(osw);
        //赋值描述
        zip.setComment("Test zip java");
        //设置名称
        for(String name : names){
            //创建压缩文件中子目录or 子文件
            zip.putNextEntry(new ZipEntry(name));
            //写入数据
            bw.write(data);
            bw.flush();
        }

        //zip.write(data.getBytes(FileUtil.DEFAULT_CODE_UTF8));
////        zip.putNextEntry(new ZipEntry("index1.html"));
////        zip.write();
        bw.close();
        osw.close();
        zip.close();
        cops.close();
        fops.close();
    }
    @Test
    public void test2() throws Exception{
        //读取压缩文件，创建IO
        FileInputStream fis = new FileInputStream(FileUtil.DEFAULT_PATH + "\\test.zip");
        //创建校验输入流
        CheckedInputStream cis = new CheckedInputStream(fis,new Adler32());
        //创建压缩输入流
        ZipInputStream zis = new ZipInputStream(cis);
        InputStreamReader isr = new InputStreamReader(zis,FileUtil.DEFAULT_CODE_UTF8);
        BufferedReader br = new BufferedReader(isr);
        ZipEntry zipEntry ;
        //循环读取压缩文件子文件or子目录
        while((zipEntry = zis.getNextEntry()) != null){
            PrintUtil.print(zipEntry.getName());
            String line;
            StringBuffer buff = new StringBuffer();
            //读取文件内容
            while((line = br.readLine()) != null){
                buff.append(line + "\n\r");
            }
            PrintUtil.print(buff.toString());
        }
        br.close();
        isr.close();
        zis.close();
        cis.close();
        fis.close();
    }
    @Test
    public void test3() throws Exception{
        ZipFile zipFile = new ZipFile(FileUtil.DEFAULT_PATH + "\\test.zip");
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()){
            ZipEntry zipEntry = entries.nextElement();
            PrintUtil.print(zipEntry);
        }
    }
}
