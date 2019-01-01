package com.mf.export;

import com.mf.export.impl.SheetMeta;

import java.util.List;

public interface IExcelContext {

    List<SheetMeta> getSheetMetas();

    long getTaskId();

    String getFileName();

    Integer getPage();

    Integer getRows();
}
