package com.jeesite.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestExcellExport {
    // 传入下拉列表数组值，构造信息模板
    public static Workbook generateExcel(String[] typeArrays) {
        Workbook wb = new XSSFWorkbook();
        // 创建模板工作表
        Sheet sheet = wb.createSheet("test");
        // 创建下拉列表值存储工作表并设置值
        genearteOtherSheet(wb, typeArrays);

        // 创建模板列信息并绑定下拉列表值
        Row row = sheet.createRow(0);
        // 设置列信息样式 -- 当前样式对于列信息未居中
        setStyle(wb, sheet, 0);
        setStyle(wb, sheet, 1);
        // 绑定列信息
        Cell nameCell = row.createCell((int) 0);
        nameCell.setCellValue("名称");
        Cell typeCell = row.createCell((int) 1);
        typeCell.setCellValue("类型");

        // 设置下拉列表直绑定对哪一页起作用
        sheet.addValidationData(SetDataValidation(wb, "typelist!$A$1:$A$" + typeArrays.length, 1, 0, typeArrays.length, 0));

        // 隐藏作为下拉列表值的Sheet
        wb.setSheetHidden(wb.getSheetIndex("typelist"), true);

        return wb;
    }

    // 创建下拉列表值存储工作表并设置值
    public static void genearteOtherSheet(Workbook wb, String[] typeArrays) {
        // 创建下拉列表值存储工作表
        Sheet sheet = wb.createSheet("typelist");
        // 循环往该sheet中设置添加下拉列表的值
        for (int i = 0; i < typeArrays.length; i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell((int) 0);
            cell.setCellValue(typeArrays[i]);
        }
    }

    // 设置列信息样式
    public static void setStyle(Workbook wb, Sheet sheet,int colNum) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        DataFormat format = wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));

        sheet.setDefaultColumnStyle(colNum, cellStyle);
    }

    // 设置并引用其他Sheet作为绑定下拉列表数据
    public static DataValidation SetDataValidation(Workbook wb, String strFormula, int firstRow, int firstCol, int endRow, int endCol) {
        // 表示A列1-59行作为下拉列表来源数据
        // String formula = "typelist!$A$1:$A$59" ;
        // 原顺序为 起始行 起始列 终止行 终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) wb.getSheet("typelist"));
        DataValidationConstraint formulaListConstraint = dvHelper.createFormulaListConstraint(strFormula);
        DataValidation dataValidation = dvHelper.createValidation(formulaListConstraint, regions);

        return dataValidation;
    }

    public static void main(String[] args) throws Exception {
        List<String> typelist = new ArrayList<String>();
        for (int i = 0; i < 200; i++) {
            typelist.add("T" + (0 + i));
        }
        String[] typeArrays = typelist.toArray(new String[typelist.size()]);
        Workbook wb = generateExcel(typeArrays);
        File tempFile = new File("C:\\Users\\USR\\Desktop\\test.xlsx");
        OutputStream os = new FileOutputStream(tempFile);

        wb.write(os);
        os.close();
    }

}
