package com.hzsh.util;

import lombok.Data;

import java.util.List;

@Data
public class ColumnDTO {

    public ColumnDTO(String 公式体) {
        String[] strings = 公式体.split(":");
        tableName = strings[0].split("\\{")[1];
        code = strings[1];
        columnName = strings[2].split("}")[0];
    }

    public String tableName;

    public String code;

    public String columnName;


}
