package com.mf.service.impl;

import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportConsumer;

import javax.annotation.Resource;
import java.util.List;

@Resource(name = "bedExportConsumer")
public class BedExportConsumer implements IExcelExportConsumer {
    @Override
    public long batchData(IExcelContext context, List<?> data) {
        return 0;
    }
}
