package com.hzsh.hzsh.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.JiaGeMapper;
import com.hzsh.hzsh.model.JiaGe;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 价格体系.价格表   [PRICE].[F_SUN_PRICE]
 */
@Service
@DS("jiaGe")
public class JiaGeServiceImpl extends ServiceImpl<JiaGeMapper, JiaGe> implements JiaGeService {

    @Resource
    private JdbcTemplate jdbcTemplate;



    /**
     * 获取(有数据的,最接近日期的)对象. 并保存到 [价格历史表]
     */
    @Override
    public JiaGe getLast(String priceCode) {
        String maxSql = String.format("SELECT MAX(UPDATETIME) FROM F_SUN_PRICE WHERE PRICECODE = '%s'", priceCode);
        Date maxDate = jdbcTemplate.queryForObject(maxSql, Date.class);
        String day = new DateTime(maxDate).toString("yyyy-MM-dd");

        // 1. 从价格表获取最近日期的数据
        String querySql = String.format("SELECT * FROM F_SUN_PRICE WHERE PRICECODE = '%s' AND TO_CHAR(UPDATETIME, 'yyyy-MM-dd') = '%s'", priceCode, day);
        System.out.println(querySql);
        JiaGe jiaGe = jdbcTemplate.queryForObject(querySql, ((rs, rowNum) -> {
            JiaGe one = new JiaGe();
            one.id = rs.getString("ID");
            one.priceCode = rs.getString("PRICECODE");
            one.name = rs.getString("ITEMNAME");
            one.day = rs.getDate("UPDATETIME");
            one.含税价 = rs.getDouble("CYTAXPRICE");
            one.裸税价 = rs.getDouble("CYNUDETAXPRICE");
            one.汇率 = rs.getDouble("MOUNTRMB");
            return one;
        }));




        return jiaGe;
    }

}
