package com.hzsh.hzsh.controller;

import com.hzsh.hzsh.service.*;
import com.hzsh.zhks.service.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/ImportExcel")
public class ImportExcelController {

    @Resource
    private LiRunService liRunService;

    @Resource
    private XiaoShouShouRuService xiaoShouShouRuService;

    @Resource
    private XiaoShouChengBenService xiaoShouChengBenService;

    @Resource
    private JingYingFeiYongService jingYingFeiYongService;

    @Resource
    private ShuiJinService shuiJinService;

    @Resource
    private YuanCaiLiaoService yuanCaiLiaoService;

    @Resource
    private BanChengPinService banChengPinService;

    @Resource
    private FeiYongMingXiService feiYongMingXiService;

    @Resource
    private KaiShiLiRunService kaiShiLiRunService;

    //中海开氏-销售收入
    @Resource
    private KaiShiXiaoShouShouRuService kaiShiXiaoShouShouRuService;

    //中海开氏-产品成本
    @Resource
    private KaiShiChanPinChengBenService kaiShiChanPinChengBenService;

    //中海开氏-生产经营
    @Resource
    private KaiShiShengChanJingYinService kaiShiShengChanJingYinService;

    //中海开氏-费用
    @Resource
    private KaiShiFeiYongService kaiShiFeiYongService;

    //中海开氏-半成品  [ZHKS_BAN_CHENG_PIN]
    @Resource
    private KaiShiBanChengPinService kaiShiBanChengPinService;





    /**
     * 导入底稿入口页面
     */
    @GetMapping("/view")
    public String importDiGaoView(Model model) {
        return "hzsh/import-di-gao";
    }


    /**
     * 导入开氏数据入口页面
     */
    @GetMapping("/ksview")
    public String importkaiShiView() {
        return "hzsh/import-kai-shi";
    }





    /**
     * <h2 style="color:red">导入Excel底稿数据</h2>
     */
    @PostMapping(value = "importDiGao")
    @ResponseBody
    public String 导入Excel底稿数据(MultipartFile file, String fileType) {
        switch (fileType) {
            case "惠州石化":
                // 1. 利润表
                liRunService.importLiRun(file);

                // 2. 销售收入
                xiaoShouShouRuService.importXiaoShouShouRu(file);

                // 3. 销售成本
                xiaoShouChengBenService.importXiaoShouChengBen(file);

                // 4. 经营费用表
                jingYingFeiYongService.importJingYingFeiYong(file);

                // 5. 税金及附加
                shuiJinService.importShuiJin(file);

                // 6. 原材料表
                yuanCaiLiaoService.importYuanCaiLiao(file);

                // 7. 半成品
                banChengPinService.importBanChengPin(file);

                // 8. 费用明细
                feiYongMingXiService.importFeiYong(file);
                break;
            case "开氏":
                // 1. 开氏-利润表
                kaiShiLiRunService.importKaiShiLiRun(file);

                // 2. 开氏-销售收入
                kaiShiXiaoShouShouRuService.importKaiShiXiaoShouShouRu(file);

                // 3. 开氏-产品成本
                kaiShiChanPinChengBenService.importKaiShiChanPinChengBen(file);

                // 4. 开氏-生产经营
                kaiShiShengChanJingYinService.importKaiShiShengChanJingYin(file);

                // 5. 开氏-费用
                kaiShiFeiYongService.importKaiShiFeiYong(file);

                // 6. 开氏-半成品
                kaiShiBanChengPinService.importKaiShiBanChengPin(file);
                break;
            default:
                System.out.println("没有操作");
                return null;
        }

        System.out.println("导入完成");
        return "导入完成";
    }






    /**
     * <h2 style="color:orange">下载用于导数的Excel模板(用于提供数量,费用等数据的导入)</h2>
     */
    @GetMapping(value = "downloadTemplate")
    public void 下载导数模板(HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 利润表, 没有需要人工导入的数据, 不提供模板

        // 1. 费用明细
        feiYongMingXiService.返回导数模板(workbook);

        // 2. 半成品
        banChengPinService.返回导数模板(workbook);

        // 3. 原材料表
        yuanCaiLiaoService.返回导数模板(workbook);

        // 4. 税金及附加
        shuiJinService.返回导数模板(workbook);

        // 5. 销售成本
        xiaoShouChengBenService.返回导数模板(workbook);

        // 6. 销售收入 (引用销售成本的单元格公式)
        xiaoShouShouRuService.返回导数模板(workbook);

        // 7. 经营费用表
        // 包含引用以上某些单元格的公式, 放在最后
        jingYingFeiYongService.返回导数模板(workbook);


        // 8. 输出文件
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        String fileName = "效益测算导数模板 " + DateTime.now().toString("yyyy年MM月dd日HH时mm分ss秒") + ".xls";
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        workbook.write(os);
        os.flush();
        os.close();
        workbook.close();
    }

    /**
     * <h2 style="color:orange">人工导入数量等数据</h2>
     */
    @PostMapping(value = "import")
    @ResponseBody
    public String 导入数量等数据(MultipartFile file) {
        // 开始记录运行时长
        long startTime = System.currentTimeMillis();


        // 2.
        // 利润表, 没有需要人工导入的数据

        // 1. 费用明细
        feiYongMingXiService.从导数模板导入数据(file);

        // 2. 半成品
        banChengPinService.从导数模板导入数据(file);

        // 3. 原材料表
        yuanCaiLiaoService.从导数模板导入数据(file);

        // 4. 税金及附加
        shuiJinService.从导数模板导入数据(file);

        // 5. 销售成本
        xiaoShouChengBenService.从导数模板导入数据(file);

        // 6. 销售收入
        xiaoShouShouRuService.从导数模板导入数据(file);

        // 7. 经营费用表
        jingYingFeiYongService.从导数模板导入数据(file);


        // 结束记录运行时长
        long endTime = System.currentTimeMillis();
        String 耗时 = "运行耗时： " + (endTime - startTime) / 1000 + "秒";
        System.out.println("「导入数据」完成. " + 耗时);

        return "导入完成. " + 耗时;
    }






    /**
     * <h2 style="color:blue">下载用于开氏导数的Excel模板</h2>
     */
    @GetMapping(value = "downloadTemplateKS")
    public void 下载开氏导数模板(HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 1. 开氏 - 利润表
        kaiShiLiRunService.返回导数模板(workbook);

        // 2. 开氏 - 销售收入
        kaiShiXiaoShouShouRuService.返回导数模板(workbook);

        // 3. 开氏 - 产品成本
        kaiShiChanPinChengBenService.返回导数模板(workbook);

        // 4. 开氏 - 生产经营
        kaiShiShengChanJingYinService.返回导数模板(workbook);

        // 5. 开氏 - 费用
        kaiShiFeiYongService.返回导数模板(workbook);

        // 6. 开氏 - 半成品
        kaiShiBanChengPinService.返回导数模板(workbook);


        // 7. 输出文件
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        String fileName = "开氏效益测算导数模板 " + DateTime.now().toString("yyyy年MM月dd日HH时mm分ss秒") + ".xls";
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        workbook.write(os);
        os.flush();
        os.close();
        workbook.close();
    }

    /**
     * <h2 style="color:blue">导入开氏数据</h2>
     */
    @PostMapping(value = "importKS")
    @ResponseBody
    public String 导入开氏数据(MultipartFile file) {
        // 开始记录运行时长
        long startTime = System.currentTimeMillis();


        // 1. 开氏 - 利润表
        kaiShiLiRunService.从导数模板导入数据(file);

        // 2. 开氏 - 销售收入
        kaiShiXiaoShouShouRuService.从导数模板导入数据(file);

        // 3. 开氏 - 产品成本
        kaiShiChanPinChengBenService.从导数模板导入数据(file);

        // 4. 开氏 - 生产经营
        kaiShiShengChanJingYinService.从导数模板导入数据(file);

        // 5. 开氏 - 费用
        kaiShiFeiYongService.从导数模板导入数据(file);

        // 6. 开氏 - 半成品
        kaiShiBanChengPinService.从导数模板导入数据(file);


        // 结束记录运行时长
        long endTime = System.currentTimeMillis();
        String 耗时 = "运行耗时： " + (endTime - startTime) / 1000 + "秒";
        System.out.println("「导入开氏数据」完成. " + 耗时);

        return "导入完成. " + 耗时;
    }

}
