package com.mf.export.impl;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SheetMeta {
    private String name;

    private String providerBean;

    private List<ColumnMeta> columnMetas;

    private Integer titleRow;

    private Integer dataRow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderBean() {
        return providerBean;
    }

    public void setProviderBean(String providerBean) {
        this.providerBean = providerBean;
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

    public Integer getTitleRow() {
        return titleRow;
    }

    public void setTitleRow(Integer titleRow) {
        this.titleRow = titleRow;
    }

    public Integer getDataRow() {
        return dataRow;
    }

    public void setDataRow(Integer dataRow) {
        this.dataRow = dataRow;
    }
}
