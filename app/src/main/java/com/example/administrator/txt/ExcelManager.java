package com.example.administrator.txt;

import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelManager {
    private String[] cnName = {"单号", "公司名称"};
    private String[] fieldName = {"number", "name"};
    private int[] colNum = {-1, -1};


    public ExcelManager() {
    }

    /**
     * 判断打开的文件是否为Excel文件
     */
    public boolean isEXL(String excelFileName) {
        boolean isExl = false;
//		System.out.println(excelFileName);
        File f1 = new File(excelFileName);
//		System.out.println(f1.exists());
        if (f1.exists()) {
            try {
                // 打开Excel文件
                Workbook book = Workbook.getWorkbook(f1);
//				System.out.println("文件被打开");
                // 获得第一个工作表对象
                Sheet sheet = book.getSheet(0);
                // 得到第一列第一行的单元格
                Cell cell1 = sheet.getCell(0, 2);
//				System.out.println("提取了数据");
                String result = cell1.getContents();
                book.close();
                colNum = getFieldCol(excelFileName, colNum);
                for (int i = 0; i < 2; i++) {
                    if (colNum[i] == -1) {
                        isExl = false;
                        break;
                    } else {
                        isExl = true;
                    }
                }


            } catch (Exception e) {
                Log.d("feng", e + "=====");
                isExl = false;
            }
        }
        return isExl;
    }

    /**
     * 查找字段所在EXCEL文件中所在的列
     *
     * @param excelFileName EXCEL文件名
     */
    private int[] getFieldCol(String excelFileName, int[] colNum) {
        int[] col = colNum;
        File f1 = new File(excelFileName);
        if (f1.exists()) {
            try {
                // 打开Excel文件
                Workbook book = Workbook.getWorkbook(f1);
//				System.out.println("文件被打开");
                // 获得第一个工作表对象
                Sheet sheet = book.getSheet(0);
//				System.out.println("第一张表被打开");
                for (int i = 0; i < 2; i++) {
                    // 得到第一列第一行的单元格
                    Cell cell1 = sheet.findCell(cnName[i]);
                    String s = cell1.getContents();
//                    Cell cell1 = sheet.getCell(cnName[i]);
                    if (cell1.getType() == CellType.EMPTY) {
                        col[i] = -1;
                    } else {
                        col[i] = cell1.getColumn();
                    }
                }
                book.close();
            } catch (Exception e) {
            }
        }
        return col;
    }

    /**
     * 通过反射获取一条记录
     *
     * @param <T>
     * @param findContent
     * @param excelFileName
     * @param cls
     * @return
     */
//    public <T> T findOneExcelRecord(String findContent, String excelFileName, Class<T> cls) {
//        T resultObject = null;
//        java.io.File f1 = new java.io.File(excelFileName);
//        if (f1.exists()) {
//            colNum = getFieldCol(excelFileName, colNum);
////			for(int j=0;j<6;j++)
////            	System.out.println(colNum[j]);
//            if (colNum[0] != -1) {
//                try {
//                    Workbook book = Workbook.getWorkbook(f1);
//                    Sheet sheet = book.getSheet(0);
//                    int rows = sheet.getRows();
//                    Cell cell = sheet.findCell(findContent, colNum[0], 0, colNum[0], rows, true);
//                    if (cell.getType() != CellType.EMPTY) {
//                        int row = cell.getRow();
//                        resultObject = cls.newInstance();
//                        for (int i = 0; i < 6; i++) {
//                            Object col_val = null;
//                            if (colNum[i] != -1) {
//                                Cell cell1 = sheet.getCell(colNum[i], row);
//                                if (cell1.getType() == CellType.EMPTY) {
//                                    col_val = "";
//                                } else {
//                                    col_val = cell1.getContents();
//                                }
//                            } else {
//                                col_val = "";
//                            }
//                            Field field = cls.getDeclaredField(fieldName[i]);
//                            field.setAccessible(true);
//                            field.set(resultObject, col_val);
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//        }
//        return resultObject;
//    }

    /**
     * 通过反射获取全部记录
     */
    public <T> List<T> findMereExcelRecord(String excelFileName, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        File f1 = new File(excelFileName);
        if (f1.exists()) {
            //获得列名所对应的位置
            colNum = getFieldCol(excelFileName, colNum);
            //判断"条码"列名是否存在
            if (colNum[0] != -1) {
                try {
                    Workbook book = Workbook.getWorkbook(f1);
                    Sheet sheet = book.getSheet(0);
                    //返回EXCEL的表格总行数
                    int rows = sheet.getRows();
                    //返回EXCEL的表格总列数
                    int Columns = sheet.getColumns();

                    for (int iRd = 1; iRd < rows; iRd++) {
                        Cell cell = sheet.getCell(0, iRd);
                        if (cell.getType() != CellType.EMPTY) {
                            T resultObject = cls.newInstance();
                            for (int i = 0; i < 2; i++) {
                                Object col_val = null;
                                if (colNum[i] != -1) {
                                    Cell cell1 = sheet.getCell(colNum[i], iRd);
                                    if (cell1.getType() == CellType.EMPTY) {
                                        col_val = "";
                                    } else {
                                        col_val = cell1.getContents();
                                    }
                                } else {
                                    col_val = "";
                                }
                                Field field = cls.getDeclaredField(fieldName[i]);
                                field.setAccessible(true);
                                field.set(resultObject, col_val);
                            }
                            list.add(resultObject);
                        }
                    }
                } catch (Exception e) {
                }
            }


        }
        return list;
    }

    /**
     * 保存数据
     *
     * @param data          要保存的数据
     * @param excelFileName
     * @return 保存成功返回true，否则返回true
     */
    public boolean savedata(List<String[]> data, String excelFileName) {
        boolean isSave = false;
        File f1 = new File(excelFileName);
        //File f1=getFilePath("/nmt/sdcard", "/入库记录");
        try {
            // 打开文件
            //WritableWorkbook book = Workbook.createWorkbook(f1);
            WritableWorkbook book = Workbook.createWorkbook(f1);

            // 生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("sheet1", 0);
            for (int i = 0; i < 6; i++) {
                // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
                // 以及单元格内容为cnName[i]
                Label label = new jxl.write.Label(i, 0, cnName[i]);
                // 将定义好的单元格添加到工作表中
                sheet.addCell(label);
            }
            int i = 1;
            for (String[] data1 : data) {
                for (int j = 0; j < 6; j++) {
                    // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
                    // 以及单元格内容为data1[j]
                    Label label = new jxl.write.Label(j, i, data1[j]);
                    // 将定义好的单元格添加到工作表中
                    sheet.addCell(label);
                }
                i++;
            }
            book.write();
            book.close();
            isSave = true;
        } catch (Exception e) {
            System.out.println("_______________----exception:" + e);
            isSave = false;
        }
        return isSave;
    }

    public static File getFilePath(String filePath,
                                   String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
// TODO Auto-generated catch block  
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }
}
