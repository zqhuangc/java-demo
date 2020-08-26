package com.melody.java.file;

import cn.hutool.core.io.FileUtil;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件编码格式转换
 * @author zqhuangc
 */
public abstract class FilesCodecConverter {

    private final static Charset charsetSource = CharsetUtil.charset("GBK");
    private final static Charset charsetTarget = CharsetUtil.charset("UTF-8");

    private FilesCodecConverter(){
    }

    public static void main(String[] args) {
        FilesCodecConverter.convertCharset(
                System.getProperty("user.dir"),
                charsetSource,
                charsetTarget,
                "txt");
    }

    /**
     * 转换文件编码格式
     * @param path 需要转换的文件或文件夹路径
     * @param fromCharset 原编码格式
     * @param toCharset 目标编码格式
     * @param ext 需要转换的文件扩展名,全部文件为 null
     */
    private static void convertCharset(String path,Charset fromCharset,Charset toCharset,String ext ) {

        File[] listFiles = getFiles(path, ext);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (File listFile : listFiles) {
            if (listFile.isDirectory()) {
                String canonicalPath = FileUtil.getCanonicalPath(listFile);
                //每个文件夹分个线程处理,提高一下效率
                executorService.execute(()->
                    convertCharset(canonicalPath,fromCharset,toCharset,ext)
                );
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        convertCharset(canonicalPath,fromCharset,toCharset,ext);
                    }
                }).start();*/
            }else {
                FileUtil.convertCharset(listFile, fromCharset, toCharset);
                Console.log("转换完成文件名:{}",listFile.getName());
            }
        }
    }

    private static File[] getFiles(String path, String ext){

        if(StrUtil.isBlank(path)){
            return new File[0];
        }
        File file = FileUtil.file(path);
        // 检索文件
        return file.listFiles(pathName -> {
            if (StrUtil.isBlank(ext)) {
                return true;
            }
            if(FileUtil.isDirectory(pathName)){
                return true;
            }
            if (FileUtil.extName(pathName).equals(ext)) {
                return true;
            }
            return false;
        });
    }

}
