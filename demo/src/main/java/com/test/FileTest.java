package com.test;

import com.utils.FileUtil;
import com.utils.PrintUtil;
import org.apache.coyote.http11.filters.BufferedInputFilter;
import org.junit.Test;

import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public class FileTest {
    @Test
    public void test1() throws Exception {
        File[] local = FileUtil.local(FileUtil.DEFAULT_PATH, ".*.html$");
//        InputStream stream = null;
//        FilterInputStream filter = null;
        for(File file : local){
//            FileInputStream fileRead = new FileInputStream(file);
//            FilterInputStream filter = new FilterInputStream(fileRead);
           // BufferedInputStream buffed = new BufferedInputStream(fileRead);
//            InputStreamReader stRead = new InputStreamReader(fileRead);
            LinkedList<String> linkedList = new LinkedList<>();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferRead = new BufferedReader(fileReader);
            StringBuffer str = new StringBuffer();
            String line ;
            while((line = bufferRead.readLine())!=null){
                linkedList.add(line.toUpperCase());
               // str.append(line+"\n\r");
                PrintUtil.print(line+"\n\r");
            }
            fileReader.close();
           // PrintUtil.print(str.toString());
            ListIterator<String> iter = linkedList.listIterator();
            while(iter.hasNext()){
                String next = iter.next();
                if(next.contains("ALERT")){
                    PrintUtil.print(next);
                }
            }
//            int alert = Collections.binarySearch(linkedList,"ALERT");
//            PrintUtil.print(linkedList.get(alert));
//            Collections.reverse(linkedList);
//            PrintUtil.print(linkedList);

        }

    }
    @Test
    public void test2() throws Exception{
        File[] local = FileUtil.local(FileUtil.DEFAULT_PATH, ".*.html$");
        File writerFile = new File(FileUtil.DEFAULT_PATH + "//aa.html");
        for(File file : local){
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream,"utf-8");
            //FileReader fileReader = new FileReader(file);

            BufferedReader bufferRead = new BufferedReader(streamReader);
           // FileWriter fileWriter = new FileWriter(writerFile);
            FileOutputStream outputStream = new FileOutputStream(writerFile);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            //BufferedWriter writer = new BufferedWriter(streamWriter);
            PrintWriter out = new PrintWriter(streamWriter);
            String line;
            int num = 0;
            while(((line = bufferRead.readLine())) != null){
                //writer.write(line);
                PrintUtil.print(line + "\n\r");
                out.println(line);
            }
            bufferRead.close();
           // writer.close();
            out.close();
        }

    }
    @Test
    public void test3() throws Exception{
        File[] local = FileUtil.local(FileUtil.DEFAULT_PATH, ".*.html$");
        File writerFile = new File(FileUtil.DEFAULT_PATH + "//aa.html");
        for(File file : local){
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream,FileUtil.DEFAULT_CODE_UTF8);
            //FileReader fileReader = new FileReader(file);

            //BufferedReader bufferRead = new BufferedReader(streamReader);
            LineNumberReader lineReader = new LineNumberReader(streamReader);
            // FileWriter fileWriter = new FileWriter(writerFile);
            FileOutputStream outputStream = new FileOutputStream(writerFile);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream,FileUtil.DEFAULT_CODE_UTF8);
            BufferedWriter writer = new BufferedWriter(streamWriter);
            PrintWriter out = new PrintWriter(writer);
            String line;
            while(((line = lineReader.readLine())) != null){
                //writer.write(line);
                int lineNumber = lineReader.getLineNumber();
                PrintUtil.print(lineNumber + " : " + line + "\n\r");
                out.println(lineNumber + " : " + line);
            }
            lineReader.close();
            // writer.close();
            out.close();
        }

    }
    @Test
    public void test4() throws Exception{
        File outFile = new File(FileUtil.DEFAULT_PATH + "//bb.txt");
        FileOutputStream outputStream = new FileOutputStream(outFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        OutputStreamWriter writer = new OutputStreamWriter(bufferedOutputStream,FileUtil.DEFAULT_CODE_UTF8);
        DataOutputStream out = new DataOutputStream(bufferedOutputStream);
        out.writeDouble(3.1415926535);
        String string = "?????";
        String decode =URLDecoder.decode(URLDecoder.decode(string, FileUtil.DEFAULT_CODE_UTF8),FileUtil.DEFAULT_CODE_UTF8) ;
        out.writeUTF(decode);
        out.close();
    }
    @Test
    public void test5() throws Exception{
        File outFile = new File(FileUtil.DEFAULT_PATH + "//bb.txt");
        FileInputStream inputStream = new FileInputStream(outFile);
        BufferedInputStream bufferSteam = new BufferedInputStream(inputStream);
        DataInputStream input = new DataInputStream(bufferSteam);
        PrintUtil.print(input.readDouble());
        input.close();
    }
    @Test
    public void test6() throws Exception{
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = bufferRead.readLine()) != null){
            PrintUtil.print(line);
        }
    }
    public static  void main(String [] args) throws Exception{
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = bufferRead.readLine()) != null){
            PrintUtil.print(line);
        }
    }
    @Test
    public void test7() throws Exception{
        final int siez = 1024;
        //???????
        FileChannel fc = new FileOutputStream(new File(FileUtil.DEFAULT_PATH + "\\test7.txt")).getChannel();
        //????????????????��?��??????��?????
        fc.write(ByteBuffer.wrap("nio study".getBytes()));
        //?????
        fc.close();
        //?????RandomAccessFile ???????????????????��
        fc = new RandomAccessFile(FileUtil.DEFAULT_PATH + "\\test7.txt","rw").getChannel();
        //???????????��???????????????????????????????????
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap("????".getBytes()));
        fc.close();
        //??????????
        fc = new FileInputStream(FileUtil.DEFAULT_PATH + "\\test7.txt").getChannel();
        //????????Bytebuffer
        ByteBuffer buff = ByteBuffer.allocate(siez);
        fc.read(buff);//????????????????
        buff.flip();
        //?��?????????
        while(buff.hasRemaining()){
            //??????
            PrintUtil.print((char) buff.get());
        }


    }

    @Test
    public void test8() throws Exception{
        final int siez = 1024;

        FileChannel in = new FileInputStream(new File(FileUtil.DEFAULT_PATH + "\\test7.txt")).getChannel();
        FileChannel out = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
       // in.transferTo(0,in.size(),out);
         in.transferFrom(in, 0, in.size());
    }
    @Test
    public void test9() throws Exception{
        FileChannel channel = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
        channel.write(ByteBuffer.wrap("Some Test".getBytes(FileUtil.DEFAULT_CODE_UTF8)));
        channel.close();
        channel = new FileInputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(FileUtil.DEFAULT_SIZE);
        channel.read(buff);
        buff.flip();
        PrintUtil.print(buff.asCharBuffer());
        String coding = System.getProperty("file.encoding");
        PrintUtil.print(coding);
        PrintUtil.print("decoding is " + coding + ": " + Charset.forName(coding).decode(buff));
        /**
         *
         *
         */
        channel = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
        channel.write(ByteBuffer.wrap("Some Test".getBytes(FileUtil.DEFAULT_CODE_UTF8)));
        channel.close();
        channel = new FileInputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
        buff.clear();
        channel.read(buff);
        buff.flip();
        PrintUtil.print(Charset.forName(FileUtil.DEFAULT_CODE_UTF8).decode(buff));
        channel = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
        buff = ByteBuffer.allocate(24);
        buff.asCharBuffer().put("Some Test");
        channel.write(buff);
        channel.close();
        channel = new FileInputStream(FileUtil.DEFAULT_PATH + "\\test8.txt").getChannel();
        buff.clear();
        channel.read(buff);
        buff.flip();
        PrintUtil.print(buff.asCharBuffer());
    }
    @Test
    public void test10() throws Exception{
        FileChannel channel = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test10.txt").getChannel();
        String text = "hello,JAVA!";
        byte[] arr = text.getBytes("utf-8");
        String o = new String(arr,"utf-8");
        ByteBuffer byteBuf = ByteBuffer.wrap(arr);
        channel.write(byteBuf);
        channel.close();
    }
    @Test
    public void test11() throws Exception{
        /**
         * 使用nio写入文件时，保证项目编码格式和输出格式一致，否则在转换为输出流时会转换为项目编码，产生乱码
         */
        FileChannel channel = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\test10.txt").getChannel();
        //字符流转为字节流编码
        channel.write(ByteBuffer.wrap("呵呵一笑".getBytes(FileUtil.DEFAULT_CODE_UTF8)));
        channel.close();
        /**
         *
         */
        channel = new FileInputStream(FileUtil.DEFAULT_PATH + "\\test10.txt").getChannel();
        //获取一定大小的缓冲类
        ByteBuffer buff = ByteBuffer.allocate(FileUtil.DEFAULT_SIZE);
        //将数据放入缓冲类中
        channel.read(buff);
        //准备就绪
        buff.flip();
        //获取一定大小的字符缓冲类
        CharBuffer charBuff = CharBuffer.allocate(FileUtil.DEFAULT_SIZE);
        //指定需要转换的编码格式
        Charset charset = Charset.forName(FileUtil.DEFAULT_CODE_UTF8);
        //解码，字节转换为字符并放入数据
        charset.newDecoder().decode(buff,charBuff,false);
        //准备就绪
        charBuff.flip();
        charBuff.clear();
        charBuff.rewind();
        Byte [] by = new Byte[10];


        //输出结果
        PrintUtil.print(charBuff.toString());
    }
    @Test
    public void test12(){
        ByteBuffer buff = ByteBuffer.allocate(FileUtil.DEFAULT_SIZE);
        buff.asCharBuffer().put("HELLO");
        char c;
        while((c=buff.getChar())!=0&&buff.hasRemaining()){
            PrintUtil.print(String.valueOf(c));
        }
        buff.asShortBuffer().put((short)123);
    }
    @Test
    public void test13(){
//        ByteBuffer buff = ByteBuffer.allocate(FileUtil.DEFAULT_SIZE);
//        CharBuffer ch = buff.asCharBuffer();
//        ch.put("Hello world".toCharArray());
//        PrintUtil.print(ch.rewind());
//        symmertrice(ch);
//        PrintUtil.print(ch.rewind());
        ByteBuffer buff = ByteBuffer.allocate(FileUtil.DEFAULT_SIZE);
        buff.put("Hello world".getBytes());
        buff.rewind();
        CharBuffer ch = buff.asCharBuffer();
        PrintUtil.print(ch.toString());


    }
    public void test14() throws Exception{
        FileChannel channel = new FileInputStream(FileUtil.DEFAULT_PATH + "\\index1.html").getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());



    }
    @Test
    public void test15() throws Exception{
        FileChannel channel = new RandomAccessFile(FileUtil.DEFAULT_PATH + "\\index1.html","rw").getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PrintUtil.print(map.asCharBuffer() + "dfjkl;");
        map.rewind();
        Charset charset = Charset.forName(FileUtil.DEFAULT_CODE_UTF8);
        CharBuffer buff = CharBuffer.allocate(2048);
        charset.newDecoder().decode(map,buff,false);
        buff.flip();
        PrintUtil.print(buff + "ssssaa");


    }
    @Test
    public void test16() throws Exception{
        FileOutputStream out = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\aa.html");
        FileLock fileLock = out.getChannel().tryLock();
        if(fileLock!=null){
            PrintUtil.print("LOCK FILE");
            TimeUnit.MICROSECONDS.sleep(1000000);
            fileLock.release();
            PrintUtil.print("Release lock");
        }
        out.close();

    }
    @Test
    public void test17() throws Exception{
        /**
         * GZIP压缩和解压方法和  正常流操作一
         * 只是在解压或者压缩时多了转换为GZIP的流过程
         */
        FileInputStream fis = new FileInputStream(FileUtil.DEFAULT_PATH + "\\index1.html");
        InputStreamReader ins = new InputStreamReader(fis,FileUtil.DEFAULT_CODE_UTF8);
            BufferedReader read = new BufferedReader(ins);
            //输出gz
            FileOutputStream out = new FileOutputStream(FileUtil.DEFAULT_PATH + "\\b.gz");
            GZIPOutputStream zout = new GZIPOutputStream(out);
            OutputStreamWriter osw = new OutputStreamWriter(zout,FileUtil.DEFAULT_CODE_UTF8);
            BufferedWriter bw = new BufferedWriter(osw);
            String line;
            while((line = read.readLine()) != null){
            PrintUtil.print(line);
            bw.write(line);
        }
        read.close();
        ins.close();
        fis.close();
        bw.close();
        osw.close();
        zout.close();
        out.close();
    }
    public void test18(){
        List<? super Object> aa = new ArrayList<>();
        List<? extends A> bbb = new ArrayList<>();
        aa.add(new A());
        //concurrent
//        new Concurre
    }

    private void symmertrice(CharBuffer charBuffer){
        while(charBuffer.hasRemaining()){
            charBuffer.mark();
            char c1  = charBuffer.get();
            char c2 = charBuffer.get();
            charBuffer.reset();
            charBuffer.put(c2).put(c1);
        }
    }
}
class A {}
class B extends A{

}
