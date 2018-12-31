package com.mf.export;

import java.util.List;

public interface IExcelExportConsumer {

    long batchData(IExcelContext context, List<?> data);

}
