package com.mf.service.impl;

import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportConsumer;

import javax.annotation.Resource;

@Resource(name = "bedExportConsumer")
public class BedExportConsumer implements IExcelExportConsumer {
    @Override
    public long batchData(IExcelContext context, Object obj, Integer page, Integer rows) {
        return 0;
    }
}
