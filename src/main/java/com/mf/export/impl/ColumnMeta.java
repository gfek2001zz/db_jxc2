package com.mf.export.impl;


public class ColumnMeta {

    private String fieldName;

    private String displayName;

    private String type;

    private Integer width;

    private String entityBean;

    private Integer colIdx;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getColIdx() {
        return colIdx;
    }

    public void setColIdx(Integer colIdx) {
        this.colIdx = colIdx;
    }

    public String getEntityBean() {
        return entityBean;
    }

    public void setEntityBean(String entityBean) {
        this.entityBean = entityBean;
    }
}
