package com.mf.export.impl;

import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportProvider;
import com.mf.util.EntityUtil;
import com.mf.util.SpringContextUtils;
import com.mf.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ExcelWriteStream {
    private static final Logger logger = LoggerFactory.getLogger(ExcelWriteStream.class);

    public File generateExcel(IExcelContext context) throws IOException {
        File file = new File(context.getFileName());

        Workbook wb = getWordbook(context);

        List<SheetMeta> sheetMetas = context.getSheetMetas();
        for (SheetMeta sheetMeta : sheetMetas) {

            List<ColumnMeta> columnMetas = sheetMeta.getColumnMetas();

            Sheet sheet = wb.getSheet(sheetMeta.getName());
            if (null == sheet) {
                sheet = wb.createSheet(sheetMeta.getName());
            }

            //套用模板则不生成标题行
            if (StringUtil.isEmpty(context.getTemplate())) {
                createTitleRow(columnMetas, sheetMeta.getTitleRow(), wb, sheet);
            }

            IExcelExportProvider provider = getExcelExportProvider(sheetMeta.getProviderBean());
            provider.begin(context);

            //计算总的页数
            Long cntNum = provider.getCount(context.getParam());
            if (cntNum > 0) {
                Long totalPage = cntNum / context.getRows();
                if (cntNum % context.getRows() > 0)
                    totalPage = totalPage + 1;


                for (int page = 1; page <= totalPage; page ++) {
                    List<?> dataList = provider.batchData(context.getParam(), page, context.getRows());
                    createMergedDataRow(sheetMeta, columnMetas, dataList, sheet);
                    createDetailDateRow(sheetMeta, columnMetas, dataList, sheet);
                }
            } else {
                provider.fail(context);
            }

            provider.end(context);
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
    private void createTitleRow(List<ColumnMeta> columnMetas, Integer titleRow, Workbook wb, Sheet sheet) {
        Row row = sheet.createRow(titleRow - 1);
        CellStyle style = wb.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        style.setAlignment(HorizontalAlignment.CENTER);// 水平

        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        Font font = wb.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        int cellIdx = 0;
        for (ColumnMeta columnMeta : columnMetas) {
            Cell cell = row.createCell(cellIdx);
            cell.setCellStyle(style);
            cell.setCellValue(columnMeta.getDisplayName());

            cellIdx ++;
        }
    }


    private Workbook getWordbook(IExcelContext context) throws IOException {
        Workbook wb;
        if (StringUtil.isNotEmpty(context.getTemplate())) {
            File templateFile = ResourceUtils.getFile(String.format("classpath:%s", context.getTemplate()));
            InputStream is = new FileInputStream(templateFile);
            wb = new XSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook();
        }

        return wb;
    }

    /**
     * 写入数据行
     *
     * @param columnMetas
     * @param dataList
     */
    private void createDetailDateRow(SheetMeta sheetMeta, List<ColumnMeta> columnMetas, List<?> dataList, Sheet sheet) {
        int rowIdx = sheetMeta.getDataRow() - 1;

        for (Object obj : dataList) {

            List<ColumnMeta> columnMetasList = columnMetas.stream()
                    .filter(columnMeta -> "list".equals(columnMeta.getType())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(columnMetasList)) {
                List<?> entityObj = EntityUtil.getProperty(obj, columnMetasList.get(0).getEntityBean());

                for (Object entity : entityObj) {
                    Row row = sheet.getRow(rowIdx);
                    if (null == row) {
                        row = sheet.createRow(rowIdx);
                    }

                    for (ColumnMeta columnMeta : columnMetasList) {
                        int colIdx = columnMeta.getColIdx();
                        Cell cell = row.createCell(colIdx);

                        try {
                            cell.setCellValue(BeanUtils.getProperty(entity, columnMeta.getFieldName()));
                            if (columnMeta.getWidth() != null && columnMeta.getWidth() > 0) {
                                sheet.setColumnWidth(columnMeta.getColIdx(), columnMeta.getWidth() * 256);
                            }
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    }

                    rowIdx ++;
                }
            }

        }
    }


    private void createMergedDataRow(SheetMeta sheetMeta, List<ColumnMeta> columnMetas, List<?> dataList, Sheet sheet) {
        int rowIdx = sheetMeta.getDataRow() - 1;

        List<?> entityObj = new ArrayList<>();
        Optional<ColumnMeta> columnMetaOptional = columnMetas.stream()
                .filter(columnMeta -> "list".equals(columnMeta.getType())).findFirst();

        for (Object obj : dataList) {
            Row row = sheet.getRow(rowIdx);
            if (null == row) {
                row = sheet.createRow(rowIdx);
            }

            if (columnMetaOptional.isPresent()) {
                entityObj = EntityUtil.getProperty(obj, columnMetaOptional.get().getEntityBean());
            }

            int mergedSize = 0;
            if (entityObj.size() > 0) {
                mergedSize = entityObj.size() - 1;
            }

            int finalMergedSize = mergedSize;
            int finalRowIdx = rowIdx;
            Row finalRow = row;
            columnMetas.stream().filter(columnMeta -> !"list".equals(columnMeta.getType()))
                    .forEach(columnMeta -> setExcelCellData(sheet, finalRow, finalMergedSize, finalRowIdx, columnMeta, obj));

            rowIdx += mergedSize + 1;
        }
    }

    private void setExcelCellData(Sheet sheet, Row row, int mergedSize, int rowIdx, ColumnMeta columnMeta, Object obj){
        if (mergedSize > 0) {
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + mergedSize, columnMeta.getColIdx(), columnMeta.getColIdx()));
        }

        try {
            int colIdx = columnMeta.getColIdx();
            Cell cell = row.getCell(colIdx);
            if (null == cell) {
                cell = row.createCell(colIdx);
            }

            if (StringUtil.isNotEmpty(columnMeta.getEntityBean())) {
                obj = EntityUtil.getProperty(obj, columnMeta.getEntityBean());
            }

            cell.setCellValue(BeanUtils.getProperty(obj, columnMeta.getFieldName()));
            if (columnMeta.getWidth() != null && columnMeta.getWidth() > 0) {
                sheet.setColumnWidth(columnMeta.getColIdx(), columnMeta.getWidth() * 256);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }


    private IExcelExportProvider getExcelExportProvider(String consumerBean) {
        return SpringContextUtils.getBean(consumerBean, IExcelExportProvider.class);
    }
}
