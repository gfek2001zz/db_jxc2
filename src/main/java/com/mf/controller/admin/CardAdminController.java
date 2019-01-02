package com.mf.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mf.entity.Log;
import com.mf.entity.Card;
import com.mf.service.LogService;
import com.mf.service.CardService;

/**
 * 后台管理客户Controller
 * @author cj
 *
 */
@RestController
@RequestMapping("/admin/card")
public class CardAdminController {

	@Resource
	private CardService cardService;
	
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
	public List<Card> comboList(String q)throws Exception{
		if(q==null){
			q="";
		}
		return cardService.findByName("%"+q+"%");
	}
	
	/**
	 * 分页查询客户信息
	 * @param card
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> list(Card card,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<Card> cardList=cardService.list(card, page, rows, Direction.ASC, "id");
		Long total=cardService.getCount(card);
		resultMap.put("rows", cardList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询客户信息"));
		return resultMap;
	}
	
	/**
	 * 添加或者修改客户信息
	 * @param card
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="客户管理")
	public Map<String,Object> save(Card card)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		if(card.getId()!=null){
			logService.save(new Log(Log.UPDATE_ACTION,"更新客户信息"+card));
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加客户信息"+card));
		}
		cardService.save(card);
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
			logService.save(new Log(Log.DELETE_ACTION,"删除客户信息"+cardService.findById(id)));
			cardService.delete(id);			
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	
}
