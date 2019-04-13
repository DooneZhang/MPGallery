package com.beiing.gifmaker.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by chenliu on 2016/7/1.<br/>
 * 描述：
 * </br>
 */
public class FileUtil {

    /**
     * @param filepath
     *            文件路径
     * @return byte数组
     */
    public static byte[] getFileBytes(String filepath) {
        File file = new File(filepath);
        ByteBuffer bytebuffer = null;
        FileInputStream fileInputStream = null;
        FileChannel channel = null;
        try {
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                channel = fileInputStream.getChannel();
                bytebuffer = ByteBuffer.allocate((int) channel.size());
                bytebuffer.clear();
                channel.read(bytebuffer);
                return bytebuffer.array();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close(channel);
                close(fileInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 关闭流
     * @param stream
     */
    public static void close(Object stream){
        if(stream != null){
            try{
                if(stream instanceof InputStream){
                    ((InputStream) stream).close();
                } else if(stream instanceof OutputStream){
                    ((OutputStream) stream).close();
                } else if(stream instanceof Reader){
                    ((Reader) stream).close();
                } else if(stream instanceof Writer){
                    ((Writer) stream).close();
                } else if(stream instanceof HttpURLConnection){
                    ((HttpURLConnection) stream).disconnect();
                } else if(stream instanceof FileChannel){
                    ((FileChannel) stream) .close();
                }
            }catch(Exception e){

            }
        }
    }

}
