package com.melody.jmx.spring;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * TODO
 * @author zqhuangc
 */
@Component
@ManagedResource
public class SpringMBean {

    String value = "s";

    @ManagedOperation
    public String getValue() {
        return value;
    }

    @ManagedOperation
    public void setValue(String value) {
        this.value = value;
    }
}
