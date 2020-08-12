package com.melody.java.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;


/**
 * @Description: TODO
 * @author: melody_wongzq
 * @since: 2020/7/7
 * @see
 */
public class FilesEncodeConverter {

    private String filesPath;
    private Charset charsetSource;
    private Charset charsetTarget;

    public FilesEncodeConverter(String filesPath){
        this(filesPath, "GBK", "UTF-8");
    }

    public FilesEncodeConverter(String path, String charsetSource, String charsetTarget) {
        this.filesPath = path;
        this.charsetSource = Charset.forName(charsetSource);
        this.charsetTarget = Charset.forName(charsetTarget);
    }

    public static void main(String[] args) throws IOException {
        convertCharset("E:\\BaiduYunDownload\\咕泡资料\\咕泡课件资料和源码（所有）\\Tom老师上课的所有源码(内含详细说明)\\gupaoedu-crawler",Charset.forName("GBK"),Charset.forName("UTF-8"),"java");
    }

    /**
     * 转换文件编码格式
     * @param path 需要转换的文件或文件夹路径
     * @param fromCharset 原编码格式
     * @param toCharset 目标编码格式
     * @param expansion 需要转换的文件扩展名,如需全部转换则传 null
     */
    private static void convertCharset(String path,Charset fromCharset,Charset toCharset,String expansion ) {
        if (StrUtil.isBlank(path)) {
            return;
        }
        File file = FileUtil.file(path);
        File[] listFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (StrUtil.isBlank(expansion)) {
                    return true;
                }
                if (FileUtil.isDirectory(pathname)||FileUtil.extName(pathname).equals("java")) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory()) {
                String canonicalPath = FileUtil.getCanonicalPath(listFiles[i]);
                //每个文件夹分个线程处理,提高一下效率
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        convertCharset(canonicalPath,fromCharset,toCharset,expansion);
                    }
                }).start();
            }else {
                FileUtil.convertCharset(listFiles[i], fromCharset, toCharset);
                Console.log("转换完成文件名:{}",listFiles[i].getName());
            }
        }
    }

}
