package com.utils;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class FileUtil {
    public static final String DEFAULT_CODE_UTF8 = "UTF-8";
    public static final String DEFAULT_CODE_GBK = "GBK";
    public static final int DEFAULT_SIZE = 1024;
    public static File [] local(File dir,final String regex){
        return dir.listFiles(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(new File(name).getName()).matches();
            }
        });
    }
    public static File [] local(String path,final String regex){
        return local(new File(path),regex);
    }
    public static String readFile(File file,String encoding){
        try(FileInputStream fis = new FileInputStream(FileUtil.DEFAULT_PATH + "\\index1.html");
            InputStreamReader ins = new InputStreamReader(fis,FileUtil.DEFAULT_CODE_UTF8);
            BufferedReader read = new BufferedReader(ins);
            ){
            StringBuffer buff = new StringBuffer();
            String line ;
            while((line = read.readLine())!=null){
                buff.append(line);
            }
            return buff.toString();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return "";
    }

    public static  final String DEFAULT_PATH = "D:\\test";
    public static class FileTreeNode implements Iterable{
        private List<File> dirs = new ArrayList<>();
        private List<File> files = new ArrayList<>();
        @Override
        public Iterator iterator() {
            return files.iterator();
        }
        public void addAll(FileTreeNode other){
            dirs.addAll(other.dirs);
            files.addAll(other.files);
        }
        static FileTreeNode recurseDirs(File starDir,String regex){
              FileTreeNode  treeNode = new FileTreeNode();
              for(File item : starDir.listFiles()){
                  if(item.isDirectory()){
                      treeNode.dirs.add(item);
                      recurseDirs(item,regex);
                  }else{
                      if(item.getName().matches(regex)){
                          treeNode.files.add(item);
                      }
                  }
              }
              return treeNode;
        }
        public static FileTreeNode walk(String path,String regex){
            return walk(new File(path),regex);
        }
        public static FileTreeNode walk(File startFile,String regex){
            return recurseDirs(startFile,regex);
        }
        public static FileTreeNode walk(File startFile){
           return walk(startFile,".*");
        }
        public static FileTreeNode walk(String path){
           return walk(path,".*");
        }

       @Override
       public String toString() {
           return "dirs : " + PrintUtil.fileFormat(dirs) +"\n\n" + "files : " + PrintUtil.fileFormat(files);
       }
   }

}
class BufferedInputFile{
    public static String read(File readFile) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(readFile));
        return null;
    }
}
