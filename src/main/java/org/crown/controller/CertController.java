package org.crown.controller;

import com.aspose.words.*;
import com.aspose.words.net.System.Data.DataRow;
import com.aspose.words.net.System.Data.DataTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.crown.common.annotations.Resources;
import org.crown.enums.AuthTypeEnum;
import org.crown.framework.controller.SuperController;
import org.crown.framework.enums.ErrorCodeEnum;
import org.crown.framework.exception.ApiException;
import org.crown.framework.model.ErrorCode;
import org.crown.framework.responses.ApiResponses;
import org.crown.framework.service.FileService;
import org.crown.model.parm.Desc;
import org.crown.model.parm.Type32PARM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.*;
import java.util.List;

@Api(tags = {"Cert"}, description = "检验证书生成相关接口")
@RestController
@RequestMapping(value = "/cert")
@Validated
public class CertController extends SuperController {
    private static final int UPLOAD_DIS_INDEX = 0;  // 上传到第几个磁盘下面
    private static final String UPLOAD_DIR = "/alidata/tuv/upload/";  // 上传的目录

    @Autowired
    private License license;

    @Autowired
    private FileService fileService;


    @PostMapping("/type32")
    @Resources(auth = AuthTypeEnum.AUTH)
    @ApiOperation(value = "生成TYPE3.2证书")
    public ApiResponses<String> type32(@Validated @RequestBody Type32PARM type32parm, HttpServletResponse response) {
        String genfileName = type32parm.getCertificateNumber();
        if (!license.isLicensed()) {
            ApiResponses.failure(ErrorCodeEnum.SERVICE_UNAVAILABLE.convert(), new Exception());
        }
        System.out.println("LICENSED");


        return success(generateType32(type32parm,response));
    }

    private String generateType32(Type32PARM type32PARM,HttpServletResponse response) {
        InputStream fileInput;
        File outputFile=null;
        ByteArrayOutputStream outStream = null;
        boolean generated = false;


        try {

            long old = System.currentTimeMillis();
            fileInput = Thread.currentThread().getContextClassLoader().getResourceAsStream("type32.docx");
            outputFile = new File(File.listRoots()[UPLOAD_DIS_INDEX]+UPLOAD_DIR+type32PARM.getCertificateNumber()+".PDF");// 输出路径
            Document doc = new Document(fileInput);
            DocumentBuilder builder = new DocumentBuilder(doc);
            builder.moveToBookmark("certificateNumber");
            builder.write(type32PARM.getCertificateNumber());
            builder.moveToBookmark("clientName");
            builder.write(type32PARM.getClientName());
            builder.moveToBookmark("orderNumber");
            builder.write(type32PARM.getOrderNumber());
            builder.moveToBookmark("manufacturerName");
            builder.write(type32PARM.getManufacturerName());
            builder.moveToBookmark("plantLocation");
            builder.write(type32PARM.getPlantLocation());
            builder.moveToBookmark("specification");
            builder.write(type32PARM.getSpecification());
            builder.moveToBookmark("purchaserName");
            builder.write(type32PARM.getPurchaserName());
            //插入图片
            Shape shape = new Shape(doc, ShapeType.IMAGE);
            String imgurl=File.listRoots()[0].getPath()+UPLOAD_DIR+type32PARM.getImg();
            System.out.println(imgurl);
            shape.getImageData().setSourceFullName(imgurl);
            shape.setAspectRatioLocked(true);
            shape.setHeight(18);
            shape.setWrapType(WrapType.INLINE);
            shape.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            builder.moveToBookmark("image");
            builder.insertNode(shape);

            builder.moveToBookmark("deliveryCondition");
            builder.write(type32PARM.getDeliveryCondition());
            builder.moveToBookmark("additionalTest");
            builder.write(type32PARM.getAdditionalTest());

            DataTable descTable = new DataTable("descList");
            descTable.getColumns().add("lotNo");
            descTable.getColumns().add("heatNo");
            descTable.getColumns().add("dimension");
            descTable.getColumns().add("pcs");
            descTable.getColumns().add("weight");


            //数据表格
            List<Desc> descList = type32PARM.getDesc();


            for (int i = 0; i < descList.size(); i++) {
                Desc desc = descList.get(i);
                DataRow row = descTable.newRow();
                row.set(0, desc.getLotNo());
                row.set(1, desc.getHeatNo());
                row.set(2, desc.getDimension());
                row.set(3, desc.getPcs());
                row.set(4, desc.getWeight());
                descTable.getRows().add(row);
            }

            doc.getMailMerge().executeWithRegions(descTable);

            builder.moveToBookmark("inspectionDate");
            builder.write(type32PARM.getInspectionDate());
            builder.moveToBookmark("attachPages");
            builder.write(type32PARM.getAttachPages());
            builder.moveToBookmark("attachmentDesc");
            builder.write(type32PARM.getAttachmentDesc());
            builder.moveToBookmark("markings");
            builder.write(type32PARM.getMarkings());
            builder.moveToBookmark("comment");
            builder.write(type32PARM.getComment());
            builder.moveToBookmark("inspectorName");
            builder.write(type32PARM.getInspectorName());
            builder.moveToBookmark("position");
            builder.write(type32PARM.getPosition());

//            outStream = new ByteArrayOutputStream();
            FileOutputStream fileOS = new FileOutputStream(outputFile);
            fileOS.flush();
            doc.save(fileOS, SaveFormat.PDF);
            fileOS.close();
            long now = System.currentTimeMillis();
//            fileService.outputFile(outputFile,response);
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n");


        } catch (Exception e) {
            e.printStackTrace();
        }


        return outputFile.getName();


    }




}
