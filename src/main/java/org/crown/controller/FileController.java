package org.crown.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.crown.common.result.JsonResult;
import org.crown.framework.controller.SuperController;
import org.crown.framework.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件服务器
 * Created by wangfan on 2018-12-24 下午 4:10.
 */
@Api(value = "文件管理", tags = "file")
@RestController
@RequestMapping(value = "/file")
public class FileController extends SuperController{
    private static final int UPLOAD_DIS_INDEX = 0;  // 上传到第几个磁盘下面
    private static final String UPLOAD_DIR = "/alidata/tuv/upload/";  // 上传的目录

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件")
    @ResponseBody
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) {
        // 文件原始名称
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        // 保存到磁盘
        String path = fileService.getDate() + UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
        File outFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + path);
        try {
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            long fileSize = file.getSize();
            if ((fileSize / 1024 / 1024) >= 1 && fileService.isImgFile(outFile)) {  // 图片超过1Mb压缩到1Mb以下
                Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(1f / (fileSize / 1024 / 1024)).toFile(outFile);
            } else {
                file.transferTo(outFile);
            }
            return JsonResult.ok("上传成功").put("url", path);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("上传失败").put("error", e.getMessage());
        }
    }

    /**
     * 查看原文件
     */
    @ApiOperation(value = "查看文件")
    @GetMapping("/{url}")
    public void file(@PathVariable("url") String url, HttpServletResponse response) {
        String filePath = url.replace("%2F","/");
        fileService.outputFile(UPLOAD_DIR + filePath, response);
    }



}
