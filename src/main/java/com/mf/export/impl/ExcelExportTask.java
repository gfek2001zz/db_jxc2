package com.mf.export.impl;


import com.mf.export.IExcelContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelExportTask {

    private static final String CONFIG_NAME = "classpath:%s.export.xml";

    @Resource
    private ExcelWriteStream excelWriteStream;

    /**
     * 分批导出
     *
     * @param exportType
     * @param param
     * @return
     * @throws IOException
     */
    public File startExport(String exportType, Object param) throws IOException {
        IExcelContext context = generateContext(exportType, param);

        return excelWriteStream.generateExcel(context);
    }

    /**
     * 构建上下文
     * @param exportType
     * @param param
     * @return
     * @throws FileNotFoundException
     */
    private IExcelContext generateContext(String exportType, Object param) throws FileNotFoundException {
        ExcelContext context = new ExcelContext();

        String xmlFile = ResourceUtils.getFile(String.format(CONFIG_NAME, exportType)).getAbsolutePath();
        context.setXmlFileName(xmlFile);

        ExcelExportParser parser = new ExcelExportParser();
        List<SheetMeta> sheetMetas = parser.parse(context);
        context.setSheetMetas(sheetMetas);

        context.setParam(param);

        return context;
    }
}
