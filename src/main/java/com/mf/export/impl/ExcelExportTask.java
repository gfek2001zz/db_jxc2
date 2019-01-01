package com.mf.export.impl;


import com.mf.export.IExcelContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class ExcelExportTask {

    private static final String CONFIG_NAME = "classpath:%s.export.xml";

    /**
     * 分批导出
     * @param exportType
     * @param page
     * @param rows
     * @throws FileNotFoundException
     */
    public void startExport(String exportType, Integer page, Integer rows) throws FileNotFoundException {
        IExcelContext context = generateContext(exportType, page, rows);


    }

    /**
     * 构建上下文
     * @param exportType
     * @param page
     * @param rows
     * @return
     * @throws FileNotFoundException
     */
    private IExcelContext generateContext(String exportType, Integer page, Integer rows) throws FileNotFoundException {
        ExcelContext context = new ExcelContext();

        context.setPage(page);
        context.setRows(rows);

        String xmlFile = ResourceUtils.getFile(String.format(CONFIG_NAME, exportType)).getAbsolutePath();
        context.setFileName(xmlFile);

        ExcelExportParser parser = new ExcelExportParser();
        List<SheetMeta> sheetMetas = parser.parse(xmlFile);
        context.setSheetMetas(sheetMetas);

        return context;

    }
}
