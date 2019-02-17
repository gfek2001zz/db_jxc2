package com.mf.controller.admin;

import com.mf.entity.Log;
import com.mf.entity.SalePerson;
import com.mf.service.LogService;
import com.mf.service.SaleListPersonService;
import com.mf.service.SalePersonService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/salePerson")
public class SalePersonAdminController {

    @Resource
    private SalePersonService salePersonService;

    @Resource
    private SaleListPersonService saleListPersonService;

    @Resource
    private LogService logService;

    @RequestMapping("/list")
    @RequiresPermissions(value={"销售管理"},logical= Logical.OR)
    public Map<String,Object> list(SalePerson salePerson, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows, HttpSession session)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();

        List<SalePerson> salePersonList = salePersonService.list(salePerson, page, rows,
                Sort.Direction.DESC, "id");
        resultMap.put("rows", salePersonList);

        return resultMap;
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = {"销售管理"})
    public Map<String, Object> save(SalePerson salePerson) {
        Map<String,Object> resultMap=new HashMap<>();
        salePersonService.save(salePerson);
        if(salePerson.getId()!=null){
            logService.save(new Log(Log.UPDATE_ACTION,"更新客户信息"+salePerson));
        }else{
            logService.save(new Log(Log.ADD_ACTION,"添加客户信息"+salePerson));
        }
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/totalAmount")
    @RequiresPermissions(value = {"销售管理"})
    public Map<String, Object> totalAmount(SalePerson salePerson, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows)throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        List<SalePerson> salePersonList = salePersonService.list(salePerson, page, rows,
                Sort.Direction.DESC, "id");


        salePersonList = salePersonList.stream()
                .map(salePersonVO -> this.setTotalAmount(salePersonVO)).collect(Collectors.toList());


        resultMap.put("rows", salePersonList);
        return resultMap;
    }


    private SalePerson setTotalAmount(SalePerson salePerson) {
        salePerson.setAmount(saleListPersonService.findSaleAmountBySalePerson(salePerson.getId()));

        return salePerson;

    }
}
