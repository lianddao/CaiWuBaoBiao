package com.hzsh.Quartz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;


/**
 * 定时任务
 */
@Data
@TableName("QUARTZ_DEPLOY")
public class DingShi {

    public String id;

    public String name;

    @TableField(value = "CREATETIME")
    public Date createTime;

    @TableField(value = "UPDATETIME")
    public Date updateTime;

    @TableField(value = "BIGENTIME")
    public Date beginTime;

    /**
     * 启动时间表达式
     */
    public String cron;


    /**
     * 运行状态
     */
    @TableField(value = "STATUS")
    public String state;

    public String remark;


    /**
     * 所属项目
     */
    @TableField(value = "PROJECTNAME")
    public String projectName;

    /**
     * 排序
     */
    public Integer sort;




    public String get表达式() {
        if (! StringUtils.isEmpty(cron)) {
            String[] strings = cron.split("\\s+");
            String 秒 = strings[0];
            String 分 = strings[1];
            String 时 = strings[2];
            String 日 = strings[3];

            日 = 日.equals("*") ? "每天" : ("每月" + 日 + "日");
            return 日 + 时 + "时" + 分 + "分" + 秒 + "秒";
        }

        return null;
    }


    public String get状态() {
        String text = null;
        switch (state) {
            case "0":
                text = "已停止";
                break;
            case "1":
                text = "已启动";
                break;
        }

        return text;
    }

}
