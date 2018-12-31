package com.mf.service.impl;

import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportConsumer;

import javax.annotation.Resource;
import java.util.List;

@Resource(name = "mattressExportConsumer")
public class MattressExportConsumer implements IExcelExportConsumer {
    @Override
    public long batchData(IExcelContext context, List<?> data) {
        return 0;
    }
}
