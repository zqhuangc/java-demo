package com.melody.spring.upload.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.*;


/**
 * @Description: 加载自定义配置
 * @author: zq
 * @since: 2020/7/8
 * @see PropertySourcesPlaceholderConfigurer
 */
public class CustomConfig extends PropertyPlaceholderConfigurer {

    private static Map<String, String> ctx;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        resolvePlaceholder(props);
        super.processProperties(beanFactoryToProcess, props);
        ctx = new HashMap<>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctx.put(keyStr, value);
        }
    }

    /**
     * 解析占位符
     *
     * @param properties
     */
    private void resolvePlaceholder(Properties properties) {
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Object, Object> entry = iterator.next();
            final Object value = entry.getValue();
            if (value != null && value instanceof String) {
                String resolved = resolvePlaceHolder(properties, String.class.cast(value));
                if(!value.equals(resolved)){
                    if(resolved == null){
                        iterator.remove();
                    }else{
                        entry.setValue(resolved);
                    }
                }
            }
        }

    }

    /**
     * 解析占位符具体操作(逐个字符处理)
     * @param property
     * @return
     */
    private String resolvePlaceHolder(Properties pros, String value) {
        if(value.indexOf(DEFAULT_PLACEHOLDER_PREFIX) < 0){
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int pos = 0; pos < chars.length; pos++) {
            if( chars[pos] == '$'){
                if( chars[pos+1] == '{'){
                    String key = "";
                    int tmp = pos + 2;
                    for (; tmp < chars.length && chars[tmp] != '}'; tmp++) {
                        key += chars[tmp];
                        if ( x == chars.length - 1 ) {
                            throw new IllegalArgumentException( "unmatched placeholder start [" + value + "]" );
                        }
                    }

                    String val = extractFromSystem(pros, key);
                    buffer.append( val == null ? "" : val );
                    pos = tmp + 1;
                    if ( pos >= chars.length ) {
                        break;
                    }
                }
            }
            buffer.append( chars[pos] );
        }
        String resolved = buffer.toString();
        return isEmpty( resolved ) ? null : resolved;
    }


    /**
     * 获得配置属性
     * @param systemPropertyName
     * @return
     */
    private String extractFromSystem(Properties pros, String key) {
        try {
            return pros.getProperty(key);
        }
        catch( Throwable t ) {
            return null;
        }

    }

    /**
     * 判断字符串的空(null或者.length=0)
     * @param string
     * @return
     */
    private boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * 获取已加载的配置信息
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return ctx.get(key);
    }


    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return ctx.get(key);
    }

    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return Integer.valueOf(ctx.get(key));
    }

    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return Boolean.valueOf(ctx.get(key));
    }

    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return Long.valueOf(ctx.get(key));
    }


    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static short getShort(String key) {
        return Short.valueOf(ctx.get(key));
    }


    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        return Float.valueOf(ctx.get(key));
    }


    /**
     * 获取已加载的配置信息
     *
     * @param key
     * @return
     */
    public static double getDouble(String key) {
        return Double.valueOf(ctx.get(key));
    }

    /**
     * 获取所有的key值
     *
     * @return
     */
    public static Set<String> getKeys() {
        return ctx.keySet();
    }
}
