package com.hzsh.util;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Service
public class ExcelUtil {

    /**
     * @Title: isExcel2003 @Description: 是否是2003的excel，返回true是2003 @param: @param
     * filePath @param: @return @return: boolean @throws
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @Title: isExcel2007 @Description: 是否是2007的excel，返回true是2007 @param: @param
     * filePath @param: @return @return: boolean @throws
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * @Title: validateExcel @Description: 验证EXCEL文件 @param: @param
     * filePath @param: @return @return: boolean @throws
     */
    public static boolean validateExcel(String filePath) {
        return filePath != null && (isExcel2003(filePath) || isExcel2007(filePath));
    }

    public Workbook openExcel(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (! fileName.matches("^.+\\.(?i)(xls)$") && ! fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return null;
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }

        Workbook wb = null;

        try {
            InputStream is = file.getInputStream();

            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            }
            else {
                wb = new XSSFWorkbook(is);
            }
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 列宽自适应
     *
     * @param sheet
     * @param columnCount 处理到第几列(从0开始)
     * @param firstRow    从第几行开始处理(从0开始)
     */
    public void autoSizeColumn(HSSFSheet sheet, int columnCount, int firstRow) {
        // 列宽自适应，只对英文和数字有效
        for (int i = 0; i <= columnCount; i++) {
            sheet.autoSizeColumn(i, true);
        }

        // 获取当前列的宽度，然后对比本列的长度，取最大值
        for (int columnNum = 0; columnNum <= columnCount; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = firstRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                }
                else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    try {
                        int length = currentCell.toString().getBytes("GBK").length;
                        if (columnWidth < length + 1) {
                            columnWidth = length + 1;
                        }
                    } catch (UnsupportedEncodingException ex) {
                    }
                }
            }
            try {
                sheet.setColumnWidth(columnNum, columnWidth * 256);
            } catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("忽略超出范围异常");
            }
        }
    }



    /**
     * 为所有的合并单元格设置其边框
     *
     * @param sheet
     */
    public void setBordersToMergedCells(HSSFSheet sheet) {
        int numMerged = sheet.getNumMergedRegions();
        for (int i = 0; i < numMerged; i++) {
            CellRangeAddress mergedRegions = sheet.getMergedRegion(i);
            RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegions, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegions, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegions, sheet);
        }
    }





    public HSSFCellStyle 标题样式(HSSFWorkbook workbook) {
        HSSFFont 标题字体 = workbook.createFont();
        标题字体.setFontName("宋体");
        标题字体.setBold(true);
        标题字体.setFontHeightInPoints((short) 18);
        HSSFCellStyle 标题样式 = workbook.createCellStyle();
        标题样式.setFont(标题字体);
        标题样式.setAlignment(HorizontalAlignment.CENTER);
        标题样式.setVerticalAlignment(VerticalAlignment.CENTER);
        标题样式.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        标题样式.setBorderBottom(BorderStyle.THIN);
        标题样式.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        标题样式.setBorderRight(BorderStyle.THIN);
        标题样式.setRightBorderColor(IndexedColors.BLACK.getIndex());
        return 标题样式;
    }





    public HSSFCellStyle 表头样式(HSSFWorkbook workbook) {
        HSSFFont 表头字体 = workbook.createFont();
        表头字体.setFontName("宋体");
        表头字体.setBold(true);
        表头字体.setFontHeightInPoints((short) 13);
        HSSFCellStyle 表头样式 = workbook.createCellStyle();
        表头样式.setAlignment(HorizontalAlignment.CENTER);
        表头样式.setVerticalAlignment(VerticalAlignment.CENTER);
        表头样式.setFont(表头字体);
        表头样式.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        表头样式.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        表头样式.setBorderBottom(BorderStyle.THIN);
        表头样式.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        表头样式.setBorderRight(BorderStyle.THIN);
        表头样式.setRightBorderColor(IndexedColors.BLACK.getIndex());
        表头样式.setBorderLeft(BorderStyle.THIN);
        表头样式.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        return 表头样式;
    }



    public HSSFCellStyle 数据行样式(HSSFWorkbook workbook) {
        HSSFFont 数据行字体 = workbook.createFont();
        数据行字体.setFontName("Consolas");
        数据行字体.setFontHeightInPoints((short) 11);
        HSSFCellStyle 数据行样式 = workbook.createCellStyle();
        数据行样式.setFont(数据行字体);
        数据行样式.setBorderBottom(BorderStyle.THIN);
        数据行样式.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        数据行样式.setBorderRight(BorderStyle.THIN);
        数据行样式.setRightBorderColor(IndexedColors.BLACK.getIndex());
        数据行样式.setBorderLeft(BorderStyle.THIN);
        数据行样式.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        return 数据行样式;
    }



    public HSSFCellStyle 数据行加粗样式(HSSFWorkbook workbook) {
        HSSFFont 数据行字体 = workbook.createFont();
        数据行字体.setBold(true);
        数据行字体.setFontName("Consolas");
        数据行字体.setFontHeightInPoints((short) 11);
        HSSFCellStyle 数据行样式 = workbook.createCellStyle();
        数据行样式.setFont(数据行字体);
        数据行样式.setBorderBottom(BorderStyle.THIN);
        数据行样式.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        数据行样式.setBorderRight(BorderStyle.THIN);
        数据行样式.setRightBorderColor(IndexedColors.BLACK.getIndex());
        数据行样式.setBorderLeft(BorderStyle.THIN);
        数据行样式.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        return 数据行样式;
    }


    public HSSFCellStyle 只读样式(HSSFWorkbook workbook) {
        HSSFFont 数据行字体 = workbook.createFont();
        数据行字体.setFontName("Consolas");
        数据行字体.setFontHeightInPoints((short) 11);
        HSSFCellStyle 只读样式 = workbook.createCellStyle();
        只读样式.setFont(数据行字体);
        只读样式.setBorderBottom(BorderStyle.THIN);
        只读样式.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        只读样式.setBorderRight(BorderStyle.THIN);
        只读样式.setRightBorderColor(IndexedColors.BLACK.getIndex());
        只读样式.setBorderLeft(BorderStyle.THIN);
        只读样式.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        只读样式.setLocked(true); // ←
        return 只读样式;
    }



    public HSSFCellStyle 合并的备注样式(HSSFWorkbook workbook) {
        HSSFFont 字体 = workbook.createFont();
        字体.setFontHeightInPoints((short) 11);
        HSSFCellStyle 样式 = workbook.createCellStyle();
        样式.setFont(字体);
        样式.setBorderBottom(BorderStyle.THIN);
        样式.setBottomBorderColor(IndexedColors.RED.getIndex());
        样式.setBorderRight(BorderStyle.THIN);
        样式.setRightBorderColor(IndexedColors.BLACK.getIndex());
        样式.setBorderLeft(BorderStyle.THIN);
        样式.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        样式.setVerticalAlignment(VerticalAlignment.CENTER);
        样式.setWrapText(true);
        return 样式;
    }




    public String getString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.toString().length() == 0) {
            return null;
        }

        if (cell.getCellType().name().equals("NUMERIC")) {
            return String.valueOf(Double.valueOf(cell.toString()).intValue());
        }
        else {
            return cell.getStringCellValue();
        }
    }


    public Double getDouble(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);

        if (cell == null || cell.toString().length() == 0) {
            return null;
        }

        Double d;
        try {
            //            d = Double.parseDouble(String.format("%.2f", cell.getNumericCellValue()));
            d = cell.getNumericCellValue();
        } catch (Exception exception) {
            return null;
        }

        return d;
    }


    public Integer getInteger(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);

        if (cell == null || cell.toString().length() == 0) {
            return null;
        }


        Double d;
        try {
            //            d = Double.parseDouble(String.format("%.2f", cell.getNumericCellValue()));
            d = cell.getNumericCellValue();
        } catch (Exception exception) {
            return null;
        }

        d = (double) Math.round(d);

        return d.intValue();
    }


}
