package com.mf.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_dict_item_list")
public class DictItem implements Cloneable, Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 45)
    private String classifyCode;

    @ManyToOne(targetEntity = DictClassify.class)
    @JoinColumn(name = "classifyCode", referencedColumnName = "classifyCode", insertable = false, updatable = false)
    private DictClassify dictClassify;

    private String classifyName;

    @Column(length = 45)
    private String itemCode;

    @Column(length = 45)
    private String itemName;

    @Column
    private Integer createdBy;

    @Column
    private Date creationDate;

    @Column
    private Integer lastUpdatedBy;

    @Column
    private Date lastUpdateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(String classifyCode) {
        this.classifyCode = classifyCode;
    }

    public String getClassifyName() {
        if (dictClassify != null) {
            classifyName = dictClassify.getClassifyName();
        }

        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public DictClassify getDictClassify() {
        return dictClassify;
    }

    public void setDictClassify(DictClassify dictClassify) {
        this.dictClassify = dictClassify;
    }

    public DictItem cloneObject(String classifyCode) {
        DictItem item = null;
        try {
            item = (DictItem) this.clone();
            item.setClassifyCode(classifyCode);
        } catch (Exception ex) {

        } finally {

            return item;
        }
    }
}
