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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExcelWriteStream {
    private static final Logger logger = LoggerFactory.getLogger(ExcelWriteStream.class);

    public File generateExcel(IExcelContext context) throws IOException {
        File file = new File(context.getFileName());

        Workbook wb = getWordbook(context);

        List<SheetMeta> sheetMetas = context.getSheetMetas();
        for (SheetMeta sheetMeta : sheetMetas) {

            List<ColumnMeta> totalColumnMetas = sheetMeta.getColumnMetas();

            Sheet sheet = wb.getSheet(sheetMeta.getName());
            if (null == sheet) {
                sheet = wb.createSheet(sheetMeta.getName());
            }

            //套用模板则不生成标题行
            if (StringUtil.isEmpty(context.getTemplate())) {
                createTitleRow(totalColumnMetas, sheetMeta.getTitleRow(), wb, sheet);
            }

            //查找所有type为list的column
            List<ColumnMeta> columnMetaByTypeList = totalColumnMetas.stream()
                    .filter(columnMeta -> "list".equals(columnMeta.getType()))
                    .collect(Collectors.toList());

            //查找剩余的column
            List<ColumnMeta> columnMetas = totalColumnMetas.stream()
                    .filter(columnMeta -> !"list".equals(columnMeta.getType()))
                    .collect(Collectors.toList());


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

                    createDataRow(context, sheetMeta, columnMetas, columnMetaByTypeList, dataList, sheet);
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
    private void createDataRow(IExcelContext context, SheetMeta sheetMeta, List<ColumnMeta> columnMetas,
                               List<ColumnMeta> columnMetaByTypeList, List<?> dataList, Sheet sheet) {
        int rowIdx = sheetMeta.getDataRow() - 1;

        for (Object obj : dataList) {
            Row row = sheet.createRow(rowIdx);

            if (CollectionUtils.isNotEmpty(columnMetaByTypeList)) {
                List<?> entityObj = EntityUtil.getProperty(obj, columnMetaByTypeList.get(0).getEntityBean());
                columnMetaByTypeList.stream().forEach(columnMeta -> setExcelData(context, sheet, row, columnMeta, entityObj));
                columnMetas.stream().forEach(columnMeta -> setExcelRegionData(context, sheet, row, entityObj.size() - 1, columnMeta, obj));

            } else {
                columnMetas.stream().forEach(columnMeta -> setExcelRegionData(context, sheet, row, 0, columnMeta, obj));
            }


            rowIdx ++;
        }
    }

    /**
     * 根据regionSize合并单元格
     * @param sheet
     * @param row
     * @param mergedSize
     * @param columnMeta
     * @param obj
     */
    private void setExcelRegionData(IExcelContext context, Sheet sheet, Row row, int mergedSize, ColumnMeta columnMeta, Object obj) {
        if (mergedSize > 0) {
            sheet.addMergedRegion(new CellRangeAddress(0, mergedSize, columnMeta.getColIdx(), columnMeta.getColIdx()));
        }

        setExcelData(context, sheet, row, columnMeta, obj);
    }

    /**
     * type为list的column直接逐行写入
     * @param sheet
     * @param row
     * @param columnMeta
     * @param entityObj
     */
    private void setExcelData(IExcelContext context, Sheet sheet, Row row, ColumnMeta columnMeta, List<?> entityObj) {
        try {
            int colIdx = columnMeta.getColIdx();
            if (StringUtil.isNotEmpty(context.getTemplate())) {
                colIdx += 1;
            }

            Cell cell = row.createCell(colIdx);
            for (Object obj : entityObj) {
                cell.setCellValue(BeanUtils.getProperty(obj, columnMeta.getFieldName()));
                if (columnMeta.getWidth() != null && columnMeta.getWidth() > 0) {
                    sheet.setColumnWidth(columnMeta.getColIdx(), columnMeta.getWidth() * 256);
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * type为list的column直接逐行写入
     * @param sheet
     * @param row
     * @param columnMeta
     * @param obj
     */
    private void setExcelData(IExcelContext context, Sheet sheet, Row row, ColumnMeta columnMeta, Object obj) {
        try {
            int colIdx = columnMeta.getColIdx();
            if (StringUtil.isNotEmpty(context.getTemplate())) {
                colIdx += 1;
            }

            Cell cell = row.createCell(colIdx);
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
