package com.melody.spring.upload.controller;

import com.melody.spring.upload.listener.UploadListener;
import com.melody.spring.upload.utils.CustomConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.logging.Logger;


/**
 * @Description: TODO
 * @author: zq
 * @since: 2020/7/9
 * @see
 */
@Controller
@RequestMapping("/web")
public class UploadController extends BaseController{

    private static Logger LOG = Logger.getLogger(UploadController.class);

    private final MultipartResolver uploadResolver;

    @Autowired
    public UploadController(MultipartResolver uploadResolver) {
        this.uploadResolver = uploadResolver;
    }


    private String getBasePath(){
        return CustomConfig.getString("system.upload.store");
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
            RandomAccessFile raf = new RandomAccessFile(new File(getBasePath() + path), "r");
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

        JSONArray pathArrayObj = new JSONArray();
        //创建一个通用的多部分解析器
        try {
            if(uploadResolver.isMultipart(request)){  //判断 request 是否有文件上传,即多部分请求

                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  //转换成多部分request
                for(Entry<String, MultipartFile> entry : multiRequest.getFileMap().entrySet()){

//                	FileItem item = null;
//                	item.delete();//用来删除临时文件的方法

                    //用来判断是否是多文件上传
//                	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//                	ServletFileUpload upload = new ServletFileUpload();
//                	List<FileItem> list = upload.parseRequest(request);


                    MultipartFile item = entry.getValue();  //取得上传文件

                    String type = entry.getValue().getContentType();	//判断上传文件的类型



                    String progressId = request.getParameter("X-Progress-ID");
                    String myFileName = item.getOriginalFilename();  //取得当前上传文件的文件名称
                    String ext = ".file";//myFileName.substring(myFileName.lastIndexOf("."));
                    String fileName = progressId + "_" + System.currentTimeMillis() + ext;
                    JSONObject fileObj = new JSONObject();
                    fileObj.put("progressId", progressId);	//文件ID
                    String uploadDir = "/uploads";
                    String savePath = getBasePath() + uploadDir;//入库的路径
                    if(myFileName.trim() !=""){  //如果名称不为"",说明该文件存在，否则说明该文件不存在
                        File localFile = new File(savePath + "/" + fileName);
                        item.transferTo(localFile);
                    }
                    fileObj.put("size", item.getSize());	//文件大小
                    fileObj.put("name", myFileName);	//文件真实名
                    fileObj.put("path", uploadDir + "/" + fileName);	//文件路径
                    pathArrayObj.add(fileObj);

                }

                return super.callBackForJsonp(request, response, JSON.toJSONString(pathArrayObj));
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
    public ModelAndView progress(HttpServletRequest request, HttpServletResponse response){

        String progressId = request.getParameter("X-Progress-ID");

        UploadListener listenter = (UploadListener) request.getSession().getAttribute("process_" + progressId);
        if (listenter == null) {
            return super.callBackForJsonp(request, response, "{}");
        }
        return super.callBackForJsonp(request, response, JSON.toJSONString(listenter.progress));

    }

}
