package com.utils.file;

import com.utils.NewUtils;
import com.utils.PrintUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class FileTreeInfo implements Iterable<File> {
    public List<File> files = NewUtils.list();
    public List<File> dirs = NewUtils.list();

    public Iterator<File> iterator() {
        return files.iterator();
    }

    void addAll(FileTreeInfo other) {
        files.addAll(other.files);
        dirs.addAll(other.dirs);
    }

    @Override
    public String toString() {
        return "dirs : " + PrintUtil.getCollectionPrintStr(dirs) + "\n\n"
                + "files : " + PrintUtil.getCollectionPrintStr(files);
    }

    public static FileTreeInfo walk(String path, String regex) {
        return recurseDirs(new File(path), regex);
    }

    public static FileTreeInfo walk(File start, String regex) {
        return recurseDirs(start, regex);
    }

    public static FileTreeInfo walk(String path) {
        return recurseDirs(new File(path), ".*");
    }

    public static FileTreeInfo walk(File start) {
        return recurseDirs(start, ".*");
    }

    /**
     * 将获取的目录信息放入List
     *
     * @param start
     * @param regex
     * @return
     */
    private static FileTreeInfo recurseDirs(File start, String regex) {
        FileTreeInfo result = new FileTreeInfo();
        //遍历返回当前目录下的文件完整路径
        for (File item : start.listFiles()) {
            //判断是否为文件夹
            if (item.isDirectory()) {
                //是文件夹放入dirs中
                result.dirs.add(item);
                //递归调用当前方法循环下面文件夹中的目录信息
                result.addAll(recurseDirs(item, regex));
            } else {
                //判断当前文件名称是否满足正则，满足插入数据
                if (item.getName().matches(regex)) {
                    result.files.add(item);
                }
            }
        }
        return result;
    }
}
