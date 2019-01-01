package com.mf.service.impl;

import com.mf.entity.Goods;
import com.mf.entity.GoodsType;
import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportConsumer;
import com.mf.service.GoodsService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("mattressExportConsumer")
public class MattressExportConsumer implements IExcelExportConsumer {
    @Resource
    private GoodsService goodsService;

    @Override
    public void begin(IExcelContext context) {

    } @Override
    public Long getCount(Object obj) {
        Long resultCnt = 0L;

        if (obj != null) {
            Goods goods = (Goods) obj;
            GoodsType type = new GoodsType();
            type.setId(23);
            goods.setType(type);

            resultCnt = goodsService.getCount(goods);
        }

        return resultCnt;
    }

    @Override
    public List<?> batchData(Object obj, Integer page, Integer rows) {
        List<Goods> resultVOs = new ArrayList<>();

        if (obj != null) {
            Goods goods = (Goods) obj;
            GoodsType type = new GoodsType();
            type.setId(23);
            goods.setType(type);

            resultVOs = goodsService.list(goods, page, rows, Sort.Direction.ASC, "id");
        }

        return resultVOs;
    }

    @Override
    public void fail(IExcelContext context) {

    }

    @Override
    public void end(IExcelContext context) {

    }
}