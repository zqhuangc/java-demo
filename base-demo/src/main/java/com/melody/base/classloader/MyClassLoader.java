package com.melody.base.classloader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 ClassLoader
 * @author zqhuangc
 */
public class MyClassLoader extends ClassLoader  {
    //类加载器名称
    private String name;
    //加载类的路径
    private String path="d:/";
    //类型
    private final String fileType = ".class";

    public MyClassLoader(String name) {
        //让系统类加载器成为该类加载器的父加载器
        super();
        this.name = name;
    }

    public MyClassLoader(ClassLoader parent, String name){
        //显示指定该类加载器的父加载器
        super(parent);
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MyClassLoader{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * 获取.class文件的字节数组
     * @param name
     * @return
     */
    private byte[] loaderClassData(String name){
        InputStream inputStream = null;
        byte[] data = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.name = this.name.replace(".", File.separator);
        try {
            inputStream = new FileInputStream(new File(path+name+fileType));
            int temp = 0;
            while(-1 != (temp = inputStream.read())){
                baos.write(temp);
            }
            data = baos.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取Class对象
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = loaderClassData(name);
        name = convert(name);
        System.out.println(name);
        return this.defineClass(name,data,0,data.length);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        //loader1的父加载器为系统类加载器
        MyClassLoader loader1 = new MyClassLoader("loader1");
        loader1.setPath("E:/source/lib/lib1/");
        //loader2的父加载器为loader1
        MyClassLoader loader2 = new MyClassLoader(loader1, "loader2");
        loader2.setPath("E:/source/ideacode/mode/target/classes/com/test/mode/service/");
        //loader3的父加载器为根类加载器
        MyClassLoader loader3 = new MyClassLoader(null, "loader3");
        loader3.setPath("E:\\source\\lib\\lib3");

        Class clazz = loader2.loadClass("com/test/entity/person");
        Object object = clazz.newInstance();
    }

    public MyClassLoader(){}
    public static MyClassLoader loader = new MyClassLoader();
    /**
     * 自定义路径获取Class对象
     * @param name class 类的文件名
     * @param pack 类所在的包名 eg:com.melody.test.  后面需要带个"."<br>
     * @param location 类文件的路径，设定目录时，需要在最后带上"/"
     */
    public static Class<?> findClassByNameAndLocation(String name,String pack,File location) throws ClassNotFoundException{
        byte[] datas = loader.loadClassData1(name,location);     //将class文件的数据读入到byte数组中
        pack=loader.convert(pack);
        return loader.defineClass(pack+name, datas, 0, datas.length);//通过byte数组加载Class对象
    }
    /**
     *
     * @param pack 类所在的包名 eg:com.szelink.test.  后面需要带个"."
     * @param location 包所在的路径
     * @return
     */
    public static Map<String,Class> findClassesByLocation(String pack,File location){
        //列出所有的.class文件
        File[] childFiles=location.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.indexOf(".class")<=-1)
                    return false;
                else
                    return true;
            }
        });
        Map<String,Class> map=new HashMap<String, Class>();
        for(File f:childFiles){
            String name=f.getName().substring(0,f.getName().indexOf(".class"));
            Class c=null;
            try {
                c = findClassByNameAndLocation(name, pack, location);
                System.out.println(c.getName());
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            map.put(name, c);
        }
        return map;
    }
    /**
     * 将.class文件读入到byte[]数组中
     * @param name 要读取的文件名称
     * @param location 要读取的文件目录
     * @return 读取到的byte[]数组
     */
    protected static byte[] loadClassData1(String name,File location)
    {
        FileInputStream fis = null;
        byte[] datas = null;
        try
        {
            //File dir=new File(location);
            File classFile=new File(location,name+".class");
            fis = new FileInputStream(classFile);
            //fis = new FileInputStream(location+name+".class");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b;
            while( (b=fis.read())!=-1 )
            {
                bos.write(b);
            }
            datas = bos.toByteArray();
            bos.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fis != null)
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return datas;

    }
    /**
     * 转换包名
     * @param pack 要转换的包名
     * @return
     */
    private String convert(String pack){
        String result="";
        result=pack.replace('/', '.');//将所有的/转成.
        result=result.replace('\"', '.');//将所有的\转成.
        result=result.replace('\\', '.');
        /*
        if(!(result.substring(result.length()-1)).equals(".")){//判断最后一位是否为.，若不是，加上一个.
            result+=".";
        }*/
        return result;
    }
}
