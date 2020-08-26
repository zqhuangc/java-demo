package com.melody.spring.upload.resolver;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传进度响应
 * @author zqhuangc
 */
public class UploadResolver extends CommonsMultipartResolver {

    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        return super.parseRequest(request);
    }
}
