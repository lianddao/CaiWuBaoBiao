package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.LiRunMapper;
import com.hzsh.hzsh.model.LiRun;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 利润表 [HZSH_LI_RUN]
 */
@Service
public class LiRunServiceImpl extends ServiceImpl<LiRunMapper, LiRun> implements LiRunService {

    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private PeiZhiService peiZhiService;

    @Resource
    private GongShiService gongShiService;

    private final String CATEGORY = "利润表";



    /**
     * 从Excel底稿导入数据
     */
    @Override
    public List<LiRun> importLiRun(MultipartFile file) {
        // 1. 从文件中提取数据
        List<LiRun> list = _从Excel底稿文件中提取数据(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [HZSH_SHUIJIN]
        String day = list.get(0).getDayString();
        for (LiRun one : list) {
            insertOrUpdate(one);
        }


        return list;
    }


    /**
     * 指定日期的数据
     */
    @Override
    public List<LiRun> defaultList(String day) {
        LambdaQueryWrapper<LiRun> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(LiRun::getDay, DateTime.parse(day).toDate())
                .orderByAsc(LiRun::getSort);
        List<LiRun> list = baseMapper.selectList(queryWrapper);

        for (LiRun one : list) {
            PeiZhi peiZhi = peiZhiService.getUnique(one.code);
            one.peiZhi = peiZhi;
        }

        // 排除隐藏的行
        list = list.stream().filter(i -> i.peiZhi != null && i.peiZhi.isHidden == false).collect(Collectors.toList());


        return list;
    }



    /**
     * 获取唯一对象
     */
    @Override
    public LiRun getUnique(String day, String code) {
        LambdaQueryWrapper<LiRun> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(LiRun::getDay, DateTime.parse(day).toDate())
                .eq(LiRun::getCode, code);
        return baseMapper.selectOne(queryWrapper);
    }




    @Override
    public int edit(String name, String value, Long pk) {
        gongShiService.修改单元格数据(CATEGORY, pk, name, value);
        return 1;
    }





    @Override
    public int 复制最近日期的数据(String 待保存日期) {
        // 1. 最近有数据的日期
        QueryWrapper<LiRun> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("MAX(DAY)").lambda().ne(LiRun::getDay, DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toDate());
        Map<String, Object> map = getMap(queryWrapper1);
        Date date = new DateTime(map.get("MAX(DAY)")).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<LiRun> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(LiRun::getDay, date);
        List<LiRun> list = list(queryWrapper2);


        // 3. 复制
        List<LiRun> 待复制列表 = new ArrayList<>();
        for (LiRun one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            LiRun newOne = JSON.parseObject(jsonObject.toJSONString(), LiRun.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (LiRun one : 待复制列表) {
            insertOrUpdate(one);
        }


        return 待复制列表.size();
    }



    @Override
    public int 测试用_复制底稿数据到今天() {
        // 1. 底稿数据日期
        final String 底稿数据日期 = "2020-09-01";
        Date date = DateTime.parse(底稿数据日期).toDate();
        final String 待保存日期 = DateTime.now().toString("yyyy-MM-dd");


        // 2. 日期对应的数据
        LambdaQueryWrapper<LiRun> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(LiRun::getDay, date);
        List<LiRun> list = list(queryWrapper2);


        // 3. 复制
        List<LiRun> 待复制列表 = new ArrayList<>();
        for (LiRun one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            LiRun newOne = JSON.parseObject(jsonObject.toJSONString(), LiRun.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (LiRun one : 待复制列表) {
            insertOrUpdate(one);
        }


        return 待复制列表.size();
    }



    @Override
    public int 测试用_删除复制的当前日期的底稿数据() {
        // 1. 当前日期
        String 当前日期 = DateTime.now().toString("yyyy-MM-dd");
        Date date = DateTime.parse(当前日期).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<LiRun> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(LiRun::getDay, date);
        List<LiRun> list = list(queryWrapper2);


        // 3. 删除
        for (LiRun one : list) {
            removeById(one.id);
        }

        return 1;
    }





    private boolean insertOrUpdate(LiRun one) {
        LiRun dbOne = getUnique(one.getDayString(), one.code);
        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.getId();
            return super.updateById(one);
        }
    }





    private List<LiRun> _从Excel底稿文件中提取数据(MultipartFile file) {

        // 1. 打开文件
        Workbook wb;
        wb = excelUtil.openExcel(file);
        if (wb == null) {
            return null;
        }


        // 2. 获取指定表格
        Sheet sheet = wb.getSheet(CATEGORY);


        // 3. 获取月份
        Date day = DateTime.parse("2020-09-01").toDate();


        // 4. 定义数据行开始的位置
        int 数据行开始的位置 = 4;


        // 5. 获取数据
        int
                项目CellIndex = 0,
                当月测算CellIndex = 2;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<LiRun> fetchDataList = new ArrayList<>();

        int codeNumber = 1;
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 项目CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                LiRun one = new LiRun();
                one.day = day; //25000001
                one.code = PeiZhi.CATEGORY_利润表 + new DecimalFormat("000000").format(codeNumber++);
                one.category = CATEGORY;
                one.name = name.trim().replace(" ", "").replace(" ", ""); // 替换非常规空白符
                one.测算 = excelUtil.getDouble(row, 当月测算CellIndex);
                one.sort = row.getRowNum();
                one.isBold = row.getCell(0).getCellStyle().getFontIndexAsInt() > 0;
                one.isHidden = row.getZeroHeight();


                fetchDataList.add(one);
            }
        } catch (Exception exception) {
            String msg = "\n\n" + currentRowNum + " Row Error! \n" + exception + "\n\n";
            System.out.println(msg);
            throw new RuntimeException(msg);
        }


        // 6. 关闭文件
        try {
            wb.close();
        } catch (Exception exception) {
            throw new RuntimeException("关闭文件失败");
        }


        //
        return fetchDataList;
    }



}
