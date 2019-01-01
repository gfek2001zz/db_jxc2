package com.mf.export;

import java.util.List;

public interface IExcelExportConsumer {

    long batchData(IExcelContext context, Object obj, Integer page, Integer rows);

}
