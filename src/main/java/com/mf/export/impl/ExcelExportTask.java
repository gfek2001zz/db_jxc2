package com.mf.export.impl;


import java.util.List;

public class ExcelExportTask {

    private static final String CONFIG_NAME = "%s.export.xml";


    public void startExport(String exportType) {
        String xmlFile = String.format(CONFIG_NAME, exportType);

        ExcelExportParser parser = new ExcelExportParser();
        List<SheetMeta> sheetMetas = parser.parse(xmlFile);

        ExcelContext context = new ExcelContext();
        context.setSheetMetas(sheetMetas);


    }
}
