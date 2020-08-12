package com.melody.spring.upload.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: Web页面相关的操作
 * @author: zq
 * @since: 2020/7/8
 * @see
 */
public class WebUtil {

    /**
     * 输出字符到页面
     * @param response
     * @param contentType
     * @param content
     */
    private static void out(HttpServletResponse response,String contentType,String content){
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType + "; charset=UTF-8");

        try {
            PrintWriter out = response.getWriter();
            out.println(content);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出json到页面
     * @param request
     * @param response
     * @param json
     */
    public static void outJsonpToView(HttpServletRequest request,HttpServletResponse response,String json){
        if(json == null) {
            json = "{\"data\":{}}";
        }

        String callbackName = request.getParameter("callback");
        String iframe = request.getParameter("iframe");
        String script = request.getParameter("script");

        StringBuffer returnStr = new StringBuffer();

        String domain = request.getParameter("domain");
        if(!(null == domain || "".equals(domain))){
            returnStr.append("window.document.domain=\"" + domain + "\";");
        }

        if("1".equals(iframe)){
            returnStr.append("<script type=\"text/javascript\">");
            returnStr.append("window.parent." + ((callbackName == null) ? "callback" : callbackName) + "(" + json + ");");
            returnStr.append("</script>");
            out(response,"text/html",returnStr.toString());
        } else if("1".equals(script)){

            ObjectMapper obj = new ObjectMapper();
            try{
                obj.writeValue(response.getWriter(), json);
            }catch(Exception e){
                out(response,"text/javascript",json);
                return;
            }
        } else {
            out(response,"application/json",((callbackName == null) ? json : (callbackName + "(" + json + ")")));
        }

    }
    /**
     * 输出字符串到页面
     * @param request
     * @param response
     * @param str
     */
    public static void outStringToView(HttpServletRequest request,HttpServletResponse response,String str){
        out(response,"text/text",str);
    }

    /**
     * 输出html到页面
     * @param request
     * @param response
     * @param html
     */
    public static void outHtmlToView(HttpServletRequest request, HttpServletResponse response, String html){
        out(response,"text/html",html);
    }

    /**
     * 获取domain
     * @param request
     * @param response
     * @return
     */
    public static String getDomain(HttpServletRequest request){
        String host = request.getServerName().toLowerCase();
        Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)$");
        Matcher matcher = pattern.matcher(host);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    /**
     * 获取真实 IP 地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){

        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
