package com.mf.export;

import java.util.List;

public interface IExcelExportProvider {

    void begin(IExcelContext context);

    Long getCount(Object obj);

    List<?> batchData(Object obj, Integer page, Integer rows);

    void fail(IExcelContext context);

    void end(IExcelContext context);
}
