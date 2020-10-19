package com.jeesite.test;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;

public class TestExcell {

    private static final Integer F_CELL = 6;
    private static final Integer G_CELL = 7;
    private static final Integer H_CELL = 8;
    private static final Integer I_CELL = 9;
    private static final Integer J_CELL = 10;
    private static final Integer K_CELL = 11;

    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    private static XSSFWorkbook xssfWorkbook;

    private static Double getRandomDouble(Double min,Double max){
        return Math.random() * (max-min) + min;
    }

    private static Double getRandomDouble(){
        Double min = 10000d;
        Double max = 99999d;
        return getRandomDouble(min,max);
    }

    private static Double formatDouble(Double val){
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(val));
    }

    private static Double getNextRandomDouble(){
        return getRandomDouble();
    }

    private static void setCellValue(XSSFCell xssfCell,Double val){
        XSSFDataFormat format = xssfWorkbook.createDataFormat();
        xssfCell.getCellStyle().setDataFormat(format.getFormat("0.00")); //  两位小数
        xssfCell.setCellValue(formatDouble(val));
    }

    private static void dealA(XSSFSheet xssfSheet,Integer roxIndex){
        XSSFCell cellF = xssfSheet.getRow(roxIndex -1).createCell(F_CELL-1);
        XSSFCell cellH = xssfSheet.getRow(roxIndex -1).createCell(H_CELL-1);
        XSSFCell cellI = xssfSheet.getRow(roxIndex -1).createCell(I_CELL-1);
        XSSFCell cellJ = xssfSheet.getRow(roxIndex -1).createCell(J_CELL-1);
        XSSFCell cellK = xssfSheet.getRow(roxIndex -1).createCell(K_CELL-1);

        Double valF = getNextRandomDouble();
        Double valH = getNextRandomDouble();
        Double valI = getNextRandomDouble();
        Double valJ = (valF + valH) - valI;

        if (valJ < 0){
            setCellValue(cellK,-valJ);
        }else {
            setCellValue(cellJ,valJ);
        }
        setCellValue(cellF,valF);
        setCellValue(cellH,valH);
        setCellValue(cellI,valI);
    }



    private static void dealB(XSSFSheet xssfSheet,Integer roxIndex){
        XSSFCell cellG = xssfSheet.getRow(roxIndex -1).createCell(G_CELL-1);
        XSSFCell cellH = xssfSheet.getRow(roxIndex -1).createCell(H_CELL-1);
        XSSFCell cellI = xssfSheet.getRow(roxIndex -1).createCell(I_CELL-1);
        XSSFCell cellJ = xssfSheet.getRow(roxIndex -1).createCell(J_CELL-1);
        XSSFCell cellK = xssfSheet.getRow(roxIndex -1).createCell(K_CELL-1);

        Double valG = getNextRandomDouble();
        Double valH = getNextRandomDouble();
        Double valI = getNextRandomDouble();

        Double val = (valG +valI) - valH;
        if (val < 0){
            setCellValue(cellJ,-val);
        }else {
            setCellValue(cellK,val);
        }
        setCellValue(cellG,valG);
        setCellValue(cellH,valH);
        setCellValue(cellI,valI);
    }


    private static void dealC(XSSFSheet xssfSheet,Integer roxIndex){
        Double val = getNextRandomDouble();
        dealC(xssfSheet,roxIndex,val);
    }

    private static void dealC(XSSFSheet xssfSheet,Integer roxIndex,Double val){
        XSSFCell cellH = xssfSheet.getRow(roxIndex -1).createCell(H_CELL-1);
        XSSFCell cellI = xssfSheet.getRow(roxIndex -1).createCell(I_CELL-1);

        setCellValue(cellH,val);
        setCellValue(cellI,val);
    }


    public static void main(String[] args) throws Exception {

        for (Integer index=1; index<=10000; index++){
            InputStream inputStream = new FileInputStream("C:\\Users\\USR\\Desktop\\files\\科目余额表-模板.xlsx");
            xssfWorkbook = new XSSFWorkbook(inputStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("Sheet1");

            for (int roxIndex=3; roxIndex<=18; roxIndex++ ){
                if (roxIndex == 12) {
                    continue;
                }
                dealA(xssfSheet,roxIndex);
            }
            for (int roxIndex=19; roxIndex<=43; roxIndex++ ){
                if (roxIndex == 20 || roxIndex == 30 ||  roxIndex == 31) {
                    continue;
                }
                dealB(xssfSheet,roxIndex);
            }
            for (int roxIndex=45; roxIndex<=51; roxIndex++ ){
                dealC(xssfSheet,roxIndex);
            }

            // 第12行特殊处理
            dealB(xssfSheet,12);

            // 第44行特殊处理
            dealC(xssfSheet,44,getRandomDouble(70000d,999999d));

            XSSFCell xssfCell31R11C = xssfSheet.getRow(30).createCell(K_CELL-1);
            XSSFCell xssfCell31R9C = xssfSheet.getRow(30).createCell(I_CELL-1);
            XSSFCell xssfCell44R8C = xssfSheet.getRow(43).getCell(I_CELL-1);
            setCellValue(xssfCell31R11C,xssfCell44R8C.getNumericCellValue() * 0.13);
            setCellValue(xssfCell31R9C,xssfCell44R8C.getNumericCellValue() * 0.13);

            OutputStream outputStream  = new FileOutputStream(new File(String.format("C:\\Users\\USR\\Desktop\\files\\%s科目余额表.xlsx", index)));
            xssfWorkbook.write(outputStream);
            outputStream.close();
            inputStream.close();
        }
    }

}
