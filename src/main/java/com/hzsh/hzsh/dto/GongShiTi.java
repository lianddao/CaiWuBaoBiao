package com.hzsh.hzsh.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * 公式体
 */
@Data
public class GongShiTi {

    public GongShiTi(Object 已修改的对象, String 被修改的公式体列名称) {
        JSONObject one = JSON.parseObject(JSON.toJSONString(已修改的对象));
        tableFlag = one.getString("公式中表名");
        code = one.getString("code");
        this.被修改的公式体列名称 = 被修改的公式体列名称;
    }

    public String tableFlag;

    public String code;

    public String 被修改的公式体列名称;

    public String get已修改对象自身的公式体() {
        return String.format("{%s:%s:%s}", tableFlag, code, 被修改的公式体列名称);
    }
}
