package com.melody.jmx.dynamic;

/**
 * TODO
 * @author zqhuangc
 */
public class DataMBean implements Data {

    private String value;
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataMBean{" +
                "value='" + value + '\'' +
                '}';
    }
}
