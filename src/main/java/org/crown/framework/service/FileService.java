package org.crown.framework.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService {

    private static final int UPLOAD_DIS_INDEX = 0;  // 上传到第几个磁盘下面
    private static final String UPLOAD_DIR = "/upload/";  // 上传的目录
    /**
     * 输出文件流
     */
     public void outputFile(String file, HttpServletResponse response) {
        // 判断文件是否存在
         System.out.println(file);
         File inFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], file);
        if (!inFile.exists()) {
            System.out.println("ddddddddddddddddddddd");

            PrintWriter writer;
            try {
                response.setContentType("text/html;charset=UTF-8");
                writer = response.getWriter();
                writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">Easy File Server</p>");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        // 获取文件类型
         String contentType = getFileType(inFile);
         String newName;
         if (contentType != null) {
            response.setContentType(contentType);
        } else {  // 没有类型弹出下载
            response.setContentType("application/force-download");
            try {
                newName = URLEncoder.encode(inFile.getName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
         newName = inFile.getName();
         response.setHeader("Content-Disposition", "attachment;fileName=" + newName);
         try {
             response.flushBuffer();
         } catch (IOException e) {
             e.printStackTrace();
         }
         // 输出文件流
        OutputStream os = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                System.out.println("cccccccccccccccccccccccccccccccc");
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前日期
     */
     public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        return sdf.format(new Date());
    }

    /**
     * 获取文件类型
     */
     public String getFileType(File file) {
        String contentType = null;
        try {
            contentType = new Tika().detect(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }

    /**
     * 是否是图片类型
     */
     public boolean isImgFile(File file) {
        String contentType = getFileType(file);
        if (contentType != null && contentType.startsWith("image/")) {
            return true;
        }
        return false;
    }

}
