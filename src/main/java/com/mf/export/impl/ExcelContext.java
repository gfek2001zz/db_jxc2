package com.mf.export.impl;

import com.mf.export.IExcelContext;

import java.util.List;

public class ExcelContext implements IExcelContext {
    private List<SheetMeta> sheetMetas;

    private long taskId;

    private String fileName;

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
}
