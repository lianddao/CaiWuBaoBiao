package com.hzsh.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class R {
    private String code;
    private String message ;
    private List<Object> data;

    public static R sendMsg(String message){
        return new R("200",message,null);
    }

    public static R success(){
        return new R("200","操作成功！",null);
    }
    public static R success(Object data){
        return new R("200","操作成功",null).add(data);
    }

    public static R success(String msg,Object data){
        return new R("200",msg,null).add(data);
    }
    public static R error(){
        return new R("500","操作失败",null);
    }
    public static R error(Object data){
        return new R("500","操作失败",null).add(data);
    }

    public static R error(String msg,List data){
        return new R("500",msg,null).add(data);
    }

    public R add(Object data){
        if(this.data==null){
            this.data = new ArrayList<>();
        }
        this.data.add(data);
        return this;
    }


    public String getCode() {
        return code;
    }

    public R code(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public R msg(String message) {
        this.message = message;
        return this;
    }

    public List<Object> getData() {
        return data;
    }

    public R data(List<Object> data) {
        this.data = data;
        return this;
    }
}
