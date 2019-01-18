package com.mf.controller.admin;

import com.mf.entity.SalePerson;
import com.mf.service.SalePersonSerivce;
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

@RestController
@RequestMapping("/admin/salePerson")
public class SalePersonAdminController {

    @Resource
    private SalePersonSerivce salePersonSerivce;

    @RequestMapping("/list")
    @RequiresPermissions(value={"销售管理"},logical= Logical.OR)
    public Map<String,Object> list(SalePerson salePerson, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows, HttpSession session)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();

        List<SalePerson> salePersonList = salePersonSerivce.list(salePerson, page, rows,
                Sort.Direction.DESC, "id");
        resultMap.put("rows", salePersonList);

        return resultMap;
    }
}
