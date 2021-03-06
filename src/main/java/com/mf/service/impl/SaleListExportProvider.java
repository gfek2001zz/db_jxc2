package com.mf.service.impl;

import com.mf.entity.SaleList;
import com.mf.entity.SaleListGoods;
import com.mf.entity.SaleListPerson;
import com.mf.export.IExcelContext;
import com.mf.export.IExcelExportProvider;
import com.mf.service.SaleListGoodsService;
import com.mf.service.SaleListPersonService;
import com.mf.service.SaleListService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("saleListExportProvider")
public class SaleListExportProvider implements IExcelExportProvider {
    @Resource
    private SaleListService saleListService;
    @Resource
    private SaleListGoodsService saleListGoodsService;
    @Resource
    private SaleListPersonService saleListPersonService;


    @Override
    public void begin(IExcelContext context) {

    }

    @Override
    public Long getCount(Object obj) {
        SaleList saleList;
        if (obj != null) {
            saleList = (SaleList) obj;
        } else {
            saleList = new SaleList();
        }

        return saleListService.getCount(saleList);
    }

    @Override
    public List<?> batchData(Object obj, Integer page, Integer rows) {
        SaleList saleList;
        if (obj != null) {
            saleList = (SaleList) obj;
        } else {
            saleList = new SaleList();
        }


        List<SaleList> saleListList = saleListService.list(saleList, page, rows, Sort.Direction.ASC, "id");
        for(SaleList sl:saleListList){
            SaleListGoods saleListGoods = new SaleListGoods();
            saleListGoods.setSaleList(sl);

            List<SaleListGoods> slgList=saleListGoodsService.list(saleListGoods);
            sl.setSaleListGoodsList(slgList);

            List<SaleListPerson> saleListPeople = saleListPersonService.findListBySaleListId(sl);

            StringBuffer buffer = new StringBuffer();
            for (SaleListPerson saleListPerson : saleListPeople) {
                buffer.append(saleListPerson.getSalePerson().getName()).append(":")
                        .append(saleListPerson.getAmount() * 100 / 100.0F).append(";");
            }

            sl.setSaleListPerson(buffer.toString());
        }

        return saleListList;
    }

    @Override
    public void fail(IExcelContext context) {

    }

    @Override
    public void end(IExcelContext context) {

    }
}
