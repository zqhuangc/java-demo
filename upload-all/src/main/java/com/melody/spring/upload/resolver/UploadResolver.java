package com.melody.spring.upload.resolver;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 文件上传进度响应
 * @author: zq
 * @since: 2020/7/8
 * @see
 */
public class UploadResolver extends CommonsMultipartResolver {

    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        return super.parseRequest(request);
    }
}
