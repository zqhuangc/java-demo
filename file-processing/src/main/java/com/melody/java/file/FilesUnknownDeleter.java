package com.melody.java.file;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * maven clean
 * @author zqhuangc
 */
public class FilesUnknownDeleter {

    private static final String PATH = "E:\\Repository\\mvn_repo\\repository";
    private static long total;
    private static long time;

    public static void main(String[] args) {
        File dir = new File(PATH);
        if (!dir.isDirectory()) {
            System.out.println("请设置Maven仓库所在路径");
            System.exit(0);
        } else {
            Instant startNow = Instant.now();
            FilesUnknownDeleter.delUnknownFiles(dir);
            Instant endNow = Instant.now();
            time = Duration.between(startNow, endNow).toMillis();
        }
        System.out.printf("删除数量：%d 耗时：%d 毫秒", total, time);
    }

    public static void delUnknownFiles(File file) {
        File[] list = file.listFiles();
        if(list == null){
            return;
        }
        for (File f : list) {
            if (f.isDirectory()) {
                delUnknownFiles(f);
                if (f.getName().equals("unknown")) {
                    delAll(f);
                } else if (f.getName().startsWith("${") && f.getName().endsWith("}")) {
                    // 如果 文件夹名称是以 ${ 开头 } 结尾，那么将这个文件夹及其下面所有文件全部删除
                    delAll(f);
                } else if (f.listFiles() != null && f.listFiles().length == 0) {
                    // 删除空文件夹
                    delFile(f);
                }
            } else {
                if (f.getName().endsWith(".lastUpdated")) {
                   delFile(f);
                }
            }
        }
    }

    /**
     * 删除文件夹下的所有文件夹、文件及其子文件夹、文件
     * @param file file
     */
    private static void delAll(File file) {
        File[] list = file.listFiles();
        if(list == null){
            return;
        }
        for (File f : list) {
            if (!f.isFile()) {
                // 先将文件夹下的文件夹和文件全部删除再删除源文件夹
                delAll(f);
            }
            delFile(f);
        }
        delFile(file);

    }

    private static void delFile(File file){
        if(!file.delete()){
            return;
        }
        total++;
        System.out.println("删除：" + file.getAbsolutePath());
    }

}

