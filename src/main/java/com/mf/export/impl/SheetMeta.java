package com.mf.export.impl;

import com.mf.export.IExcelExportConsumer;

import java.util.List;

public class SheetMeta {
    private String sheetName;

    private IExcelExportConsumer consumerBean;

    private List<ColumnMeta> columnMetas;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public IExcelExportConsumer getConsumerBean() {
        return consumerBean;
    }

    public void setConsumerBean(IExcelExportConsumer consumerBean) {
        this.consumerBean = consumerBean;
    }

    public List<ColumnMeta> getColumnMetas() {
        return columnMetas;
    }

    public void setColumnMetas(List<ColumnMeta> columnMetas) {
        this.columnMetas = columnMetas;
    }
}
