package com.mf.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.mf.entity.Role;
import com.mf.entity.User;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mf.entity.Log;
import com.mf.entity.Shop;
import com.mf.service.LogService;
import com.mf.service.ShopService;
import sun.nio.cs.ext.SJIS;

/**
 * 后台管理客户Controller
 * @author cj
 *
 */
@RestController
@RequestMapping("/admin/shop")
public class ShopAdminController {

	@Resource
	private ShopService shopService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 下拉框模糊查询
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	@RequiresPermissions(value={"销售出库","客户退货","销售单据查询","客户退货查询","客户统计"},logical=Logical.OR)
	public List<Shop> comboList(String q, HttpSession session)throws Exception{
		Role currentRole = (Role) session.getAttribute("currentRole");
		if(q==null){
			q="";
		}
		List<Shop> shops = shopService.findByName("%"+q+"%");
		if ("销售".equals(currentRole.getName())) {
			User user = (User) session.getAttribute("currentUser");
			shops = shops.stream()
					.filter(shopVO -> shopVO.getId().equals(user.getShop().getId())).collect(Collectors.toList());
		}

		return shops;
	}
	
	/**
	 * 分页查询客户信息
	 * @param shop
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> list(Shop shop,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<Shop> shopList=shopService.list(shop, page, rows, Direction.ASC, "id");
		Long total=shopService.getCount(shop);
		resultMap.put("rows", shopList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询客户信息"));
		return resultMap;
	}
	
	/**
	 * 添加或者修改客户信息
	 * @param shop
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> save(Shop shop)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		if(shop.getId()!=null){
			logService.save(new Log(Log.UPDATE_ACTION,"更新客户信息"+shop));
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加客户信息"+shop));
		}
		shopService.save(shop);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除客户信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> delete(String ids)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logService.save(new Log(Log.DELETE_ACTION,"删除客户信息"+shopService.findById(id)));
			shopService.delete(id);			
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	
}
