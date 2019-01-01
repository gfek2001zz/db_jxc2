package com.mf.export.impl;

import com.mf.export.IExcelExportConsumer;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SheetMeta {
    private String name;

    private String consumerBean;

    private List<ColumnMeta> columnMetas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsumerBean() {
        return consumerBean;
    }

    public void setConsumerBean(String consumerBean) {
        this.consumerBean = consumerBean;
    }

    public List<ColumnMeta> getColumnMetas() {
        if (CollectionUtils.isEmpty(columnMetas)) {
            columnMetas = new ArrayList<>();
        }

        return columnMetas;
    }

    public void setColumnMetas(List<ColumnMeta> columnMetas) {
        this.columnMetas = columnMetas;
    }
}
