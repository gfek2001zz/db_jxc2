package com.mf.controller.admin;

import com.mf.entity.DictClassify;
import com.mf.entity.DictItem;
import com.mf.entity.Log;
import com.mf.entity.User;
import com.mf.service.DictClassifyService;
import com.mf.service.DictItemService;
import com.mf.service.LogService;
import com.mf.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/dict")
public class DictAdminController {

    @Resource
    private DictClassifyService dictClassifyService;

    @Resource
    private DictItemService dictItemService;

    @Resource
    private LogService logService;

    /**
     * 分页查询
     * @param dictClassify
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> list(DictClassify dictClassify, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        List<DictClassify> goodsList = dictClassifyService.list(dictClassify, page, rows, Sort.Direction.ASC, "id");
        Long total=dictClassifyService.getCount(dictClassify);
        resultMap.put("rows", goodsList);
        resultMap.put("total", total);
        logService.save(new Log(Log.SEARCH_ACTION,"查询数据字典"));
        return resultMap;
    }

    /**
     * 新增或者修改
     * @param dictClassify
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value="数据字典")
    public Map<String,Object> save(DictClassify dictClassify, HttpSession session)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        User user = (User) session.getAttribute("currentUser");

        if(dictClassify.getId()!=null){
            logService.save(new Log(Log.UPDATE_ACTION,"更新数据字典"+dictClassify));
            dictClassify.setLastUpdatedBy(user.getId());
            dictClassify.setLastUpdateDate(new Date());

            DictClassify srcDictClassify = dictClassifyService.findById(dictClassify.getId());
            DictItem dictItem = new DictItem();
            dictItem.setClassifyCode(srcDictClassify.getClassifyCode());
            List<DictItem> dictItems = dictItemService.list(dictItem);

            if (dictItems.size() > 0) {
                List<DictItem> items = dictItems.stream()
                        .map(item -> item.cloneObject(dictClassify.getClassifyCode()))
                        .collect(Collectors.toList());

                for (DictItem item : items) {
                    itemSave(item, session);
                }
            }

        }else{
            logService.save(new Log(Log.ADD_ACTION,"添加数据字典"+dictClassify));
            dictClassify.setCreatedBy(user.getId());
            dictClassify.setCreationDate(new Date());
            dictClassify.setLastUpdatedBy(user.getId());
            dictClassify.setLastUpdateDate(new Date());
        }


        dictClassifyService.save(dictClassify);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value="数据字典")
    public Map<String,Object> delete(Integer id)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();

        DictClassify dictClassify = dictClassifyService.findById(id);
        DictItem dictItem = new DictItem();
        dictItem.setClassifyCode(dictClassify.getClassifyCode());
        Long dictItemsCnt = dictItemService.getCount(dictItem);

        if (dictItemsCnt > 0) {
            resultMap.put("success", false);
            resultMap.put("errorInfo", "该分类已经创建字典明细，不能删除");
        } else {
            logService.save(new Log(Log.DELETE_ACTION,"删除商品信息"+dictClassify));
            dictClassifyService.delete(id);
            resultMap.put("success", true);
        }

        return resultMap;
    }



    @RequestMapping("/item/list")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> itemList(DictItem dictItem) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();

        if (StringUtil.isNotEmpty(dictItem.getClassifyCode())) {
            List<DictItem> dictItems = dictItemService.list(dictItem);
            Long total = dictItemService.getCount(dictItem);
            resultMap.put("rows", dictItems);
            resultMap.put("total", total);
            logService.save(new Log(Log.SEARCH_ACTION,"查询数据字典明细"));
        } else {
            resultMap.put("rows", new ArrayList<>());
            resultMap.put("total", 0);
        }

        return resultMap;
    }

    /**
     * 新增或者修改
     * @param dictItem
     * @return
     * @throws Exception
     */
    @RequestMapping("/item/save")
    @RequiresPermissions(value="数据字典")
    public Map<String,Object> itemSave(DictItem dictItem, HttpSession session)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        User user = (User) session.getAttribute("currentUser");

        if(dictItem.getId()==null){
            logService.save(new Log(Log.ADD_ACTION,"添加数据字典"+dictItem));
            dictItem.setLastUpdatedBy(user.getId());
            dictItem.setLastUpdateDate(new Date());
            dictItem.setCreatedBy(user.getId());
            dictItem.setCreationDate(new Date());
        }else{
            logService.save(new Log(Log.UPDATE_ACTION,"更新数据字典"+dictItem));
            dictItem.setLastUpdatedBy(user.getId());
            dictItem.setLastUpdateDate(new Date());
        }
        dictItemService.save(dictItem);
        resultMap.put("success", true);
        return resultMap;
    }


    @RequestMapping("/item/delete")
    @RequiresPermissions(value="数据字典")
    public Map<String,Object> itemDelete(Integer id)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();

        DictItem dictItem = dictItemService.findById(id);
        logService.save(new Log(Log.DELETE_ACTION,"删除字典明细"+dictItem));
        dictClassifyService.delete(id);
        resultMap.put("success", true);

        return resultMap;
    }
}
