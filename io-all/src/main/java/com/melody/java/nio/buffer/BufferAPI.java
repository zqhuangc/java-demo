package com.melody.java.nio.buffer;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * buffer api demo
 * @author zqhuangc
 * @see Buffer
 */
public class BufferAPI {

    public static void main(String[] args) {
        BufferAPI bufferAPI = new BufferAPI();
        String filePath = System.getProperty("user.dir")+ "\\io-all\\src\\main\\resources\\test.txt";
        // bufferAPI.fileParse(filePath);
        // bufferAPI.map(filePath);
        // bufferAPI.readOnly();
        bufferAPI.intBuf();
    }

    public void fileParse(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        FileChannel fc = fis.getChannel();

        // ByteBuffer 创建
        byte[] buf = new byte[256];
        ByteBuffer buffer = BufferDemo.allocateHeapBuffer(buf);
        //ByteBuffer buffer = BufferManagement.allocateHeapBuffer(512);

        output("初始化", buffer);

        // 读数据到 buffer
        fc.read(buffer);
        output("调用read()", buffer);

        //准备操作之前，先锁定操作范围
        buffer.flip();
        output("调用flip()", buffer);

        slice(buffer);
        output("调用slice()", buffer);

        System.out.println();
        //判断有没有可读数据
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            System.out.print(((char)b));
        }
        System.out.println();

        output("调用get()", buffer);

        //可以理解为解锁
        buffer.clear();
        output("调用clear()", buffer);
        //最后把管道关闭
        fis.close();

    }

    public void map(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile( fileName, "rw" );
        FileChannel fc = raf.getChannel();

        //把缓冲区跟文件系统进行一个映射关联
        //只要操作缓冲区里面的内容，文件内容也会跟着改变
        MappedByteBuffer mbb = fc.map( FileChannel.MapMode.READ_WRITE, 0,  256 );

        mbb.put( 0, (byte)97 );
        mbb.put( 128, (byte)122 );

        output("调用map()", mbb);
        fc.close();
        raf.close();
    }

    public void slice(ByteBuffer buffer){
        buffer.position(20);
        int old = buffer.limit();
        buffer.limit(30);
        ByteBuffer slice = buffer.slice();
        // 改变子缓冲区的内容
        for (int i=0; i < slice.capacity(); ++i) {
            char c = (char) slice.get(i);
            c += 3;
            slice.put( i, (byte) c );
        }
        buffer.position(0);
        buffer.limit(old);
    }

    public void intBuf(){
        // 分配新的int缓冲区，参数为缓冲区容量
        // 新缓冲区的当前位置将为零，其界限(限制位置)将为其容量。它将具有一个底层实现数组，其数组偏移量将为零。
        //分配了8个长度的int数组
        IntBuffer buffer = IntBuffer.allocate(8);

        // capacity //数组的长度，容量

        for (int i = 0; i < buffer.capacity(); ++i) {
            int j = (i + 1);
            // 将给定整数写入此缓冲区的当前位置，当前位置递增
            buffer.put(j);
        }

        // 重设此缓冲区，将限制设置为当前位置，然后将当前位置设置为0
        //固定缓冲区中的某些值，告诉缓冲区，
        //我要开始操作了，如果你再往缓冲区写数据的话
        //不要再覆盖我固定状态以前的数据了
        buffer.flip();

        // 查看在当前位置和限制位置之间是否有元素
        while (buffer.hasRemaining()) {
            // 读取此缓冲区当前位置的整数，然后当前位置递增
            int j = buffer.get();
            System.out.print(j + "  ");
        }
    }


    public void readOnly(){

        ByteBuffer buffer = BufferDemo.allocateHeapBuffer(10);

        // 缓冲区中的数据0-9
        for (int i=0; i<buffer.capacity(); ++i) {
            buffer.put( (byte)i );
        }

        // 创建只读缓冲区
        ByteBuffer readonly = buffer.asReadOnlyBuffer();

        // 改变原缓冲区的内容
        for (int i=0; i<buffer.capacity(); ++i) {
            byte b = buffer.get( i );
            b *= 10;
            buffer.put( i, b );
        }


        readonly.position(0);
        readonly.limit(buffer.capacity());

        // 只读缓冲区的内容也随之改变
        while (readonly.remaining()>0) {
            System.out.println( readonly.get());
        }

        output("readOnly()", readonly) ;
    }

    public void writeFile() throws IOException {
        byte[] message = { 83, 111, 109, 101, 32,
                98, 121, 116, 101, 115, 46 };
        FileOutputStream fout = new FileOutputStream( "e:\\test.txt" );

        FileChannel fc = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate( 1024 );

        for (byte b : message) {
            buffer.put(b);
        }

        buffer.flip();

        fc.write(buffer);

        fout.close();
    }


    public void output(String step, Buffer buffer){
        System.out.println();
        System.out.println(step + ":");
        //容量，数组大小
        System.out.print("capacity: " + buffer.capacity() + ", ");
        //当前操作数据所在的位置，也可以叫做游标
        System.out.print("position: " + buffer.position() + ", ");
        //锁定值，flip，数据操作范围索引只能在position - limit 之间
        System.out.println("limit: " + buffer.limit());
    }


}
