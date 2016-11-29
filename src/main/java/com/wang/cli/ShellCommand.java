package com.wang.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by paopao on 16/11/29.
 */
public class ShellCommand {

    private String name;
    private String abbrev;
    private String description;
    private Object handler;
    private Method method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Object invoke(Object... objects) {
        try {
            return this.method.invoke(handler, objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return "INVOKE ERROR";
    }
}
