package com.mf.export;

import com.mf.export.impl.SheetMeta;

import java.util.List;

public interface IExcelContext {

    List<SheetMeta> getSheetMetas();

    long getTaskId();

    String getFileName();

    String getXmlFileName();

    Integer getPage();

    Integer getRows();

    Object getParam();

    String getTemplate();
}
