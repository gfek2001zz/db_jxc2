package com.mf.export.impl;

import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportConsumer;
import com.mf.util.SpringContextUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class ExcelWriteStream {
    private static final Logger logger = LoggerFactory.getLogger(ExcelWriteStream.class);

    public File generateExcel(IExcelContext context) throws IOException {
        File file = new File(context.getFileName());

        Workbook wb = new XSSFWorkbook();

        List<SheetMeta> sheetMetas = context.getSheetMetas();
        for (SheetMeta sheetMeta : sheetMetas) {

            List<ColumnMeta> columnMetas = sheetMeta.getColumnMetas();
            Sheet sheet = wb.createSheet(sheetMeta.getName());

            createTitleRow(columnMetas, wb, sheet);

            IExcelExportConsumer consumer = getExcelExportConsumer(sheetMeta.getConsumerBean());
            consumer.begin(context);

            //计算总的页数
            Long cntNum = consumer.getCount(context.getParam());
            if (cntNum > 0) {
                Long totalPage = cntNum / context.getRows();
                if (cntNum % context.getRows() > 0)
                    totalPage = totalPage + 1;


                for (int page = 1; page <= totalPage; page ++) {
                    List<?> dataList = consumer.batchData(context.getParam(), page, context.getRows());

                    createDataRow(columnMetas, dataList, sheet);
                }
            } else {
                consumer.fail(context);
            }

            consumer.end(context);
        }


        OutputStream outputStream = new FileOutputStream(file);
        wb.write(outputStream);

        outputStream.flush();
        outputStream.close();

        return file;
    }

    /**
     * 创建标题行
     *
     * @param columnMetas
     * @param wb
     * @param sheet
     */
    private void createTitleRow(List<ColumnMeta> columnMetas, Workbook wb, Sheet sheet) {
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
        style.setVerticalAlignment(style.getVerticalAlignment().CENTER);// 垂直
        style.setAlignment(style.getAlignment().CENTER);// 水平

        style.setBorderTop(style.getBorderTop().THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(style.getBorderLeft().THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderRight(style.getBorderRight().THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderBottom(style.getBorderBottom().THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.index);

        Font font = wb.createFont();
        font.setColor(IndexedColors.BLACK.index);
        style.setFont(font);

        int cellIdx = 0;
        for (ColumnMeta columnMeta : columnMetas) {
            Cell cell = row.createCell(cellIdx);
            cell.setCellStyle(style);
            cell.setCellValue(columnMeta.getDisplayName());

            cellIdx ++;
        }
    }

    /**
     * 写入数据行
     *
     * @param columnMetas
     * @param dataList
     */
    private void createDataRow(List<ColumnMeta> columnMetas, List<?> dataList, Sheet sheet) {
        int rowIdx = sheet.getLastRowNum();
        for (Object obj : dataList) {
            Row row = sheet.createRow(rowIdx + 1);

            int cellIdx = 0;
            for (ColumnMeta columnMeta : columnMetas) {
                Cell cell = row.createCell(cellIdx);
                try {
                    cell.setCellValue(BeanUtils.getProperty(obj, columnMeta.getFieldName()));
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }

                cellIdx ++;
            }

            rowIdx ++;
        }
    }


    private IExcelExportConsumer getExcelExportConsumer(String consumerBean) {
        return SpringContextUtils.getBean(consumerBean, IExcelExportConsumer.class);
    }
}
