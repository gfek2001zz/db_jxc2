package com.mf.export.impl;

import com.mf.export.IExcelContext;

import java.util.List;

public class ExcelContext implements IExcelContext {
    private List<SheetMeta> sheetMetas;

    private long taskId;

    private Object param;

    private String fileName;

    private String xmlFileName;

    private Integer page = 1;

    private Integer rows = 100;

    public void setSheetMetas(List<SheetMeta> sheetMetas) {
        this.sheetMetas = sheetMetas;
    }

    @Override
    public List<SheetMeta> getSheetMetas() {
        return sheetMetas;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
