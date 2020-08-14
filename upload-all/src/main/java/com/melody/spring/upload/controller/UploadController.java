package com.melody.spring.upload.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.melody.spring.upload.listener.UploadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;

/**
 *  TODO
 * @author zqhuangc
 */
@Controller
@RequestMapping("/web")
public class UploadController extends BaseController{

    private static Logger LOG = LoggerFactory.getLogger(UploadController.class);

    private final MultipartResolver multipartResolver;

    @Value("${system.upload.store}")
    private String basePath;

    @Autowired
    public UploadController(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    /**
     * 文件预览
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/preview/uploads/**.file")
    public ModelAndView preview(HttpServletRequest request, HttpServletResponse response){
        try {
            String path = request.getRequestURI().replaceAll("/web/preview/", "");
            RandomAccessFile raf = new RandomAccessFile(new File(basePath + path), "r");
            OutputStream write = response.getOutputStream();
            int b;
            while(-1 != (b = raf.read())){
                write.write(b);
            }
            raf.close();
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //理解文件上传机制
    @RequestMapping("/upload.json")
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        //创建一个通用的多部分解析器
        try {
            if(multipartResolver.isMultipart(request)){  //判断 request 是否有文件上传,即多部分请求

                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  //转换成多部分request
                for(Map.Entry<String, MultipartFile> entry : multiRequest.getFileMap().entrySet()){

//                	FileItem item = null;
//                	item.delete();//用来删除临时文件的方法

                    //用来判断是否是多文件上传
//                	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//                	ServletFileUpload upload = new ServletFileUpload();
//                	List<FileItem> list = upload.parseRequest(request);

                    MultipartFile item = entry.getValue();  //取得上传文件

                    String type = entry.getValue().getContentType();	//判断上传文件的类型

                    String myFileName = item.getOriginalFilename();  //取得当前上传文件的文件名称
                    String ext = ".file";//myFileName.substring(myFileName.lastIndexOf("."));
                    String progressId = request.getParameter("X-Progress-ID");
                    String fileName = progressId + "_" + System.currentTimeMillis() + ext;

                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("progressId", progressId);	//文件ID
                    String uploadDir = "/uploads";
                    String savePath = basePath + uploadDir;//入库的路径
                    if(myFileName.trim() !=""){  //如果名称不为"",说明该文件存在，否则说明该文件不存在
                        File localFile = new File(savePath + "/" + fileName);
                        item.transferTo(localFile);
                    }
                    objectNode.put("size", item.getSize());	//文件大小
                    objectNode.put("name", myFileName);	//文件
                    objectNode.put("path", uploadDir + "/" + fileName);	//文件路径
                    arrayNode.add(objectNode);
                }

                return super.callBackForJsonp(request, response, objectMapper.writeValueAsString(arrayNode));
            }else{
                LOG.error("上传文件出错！");
                return null;
            }
        } catch (Exception e) {
            LOG.error("上传文件出错！", e);
            return null;
        }
    }


    /**
     * 获取上传进度
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/upload/progress.json")
    public ModelAndView progress(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        String progressId = request.getParameter("X-Progress-ID");

        UploadListener listenter = (UploadListener) request.getSession().getAttribute("process_" + progressId);
        if (listenter == null) {
            return super.callBackForJsonp(request, response, "{}");
        }
        return super.callBackForJsonp(request, response, new ObjectMapper().writeValueAsString(listenter.progress));

    }

}
