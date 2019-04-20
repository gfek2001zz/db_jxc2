package com.mf.controller.admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mf.entity.*;
import com.mf.export.impl.ExcelExportTask;
import com.mf.service.*;
import com.mf.util.DownloadUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mf.util.DateUtil;
import com.mf.util.MathUtil;
import com.mf.util.StringUtil;

/**
 * 后台管理销售单Controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/admin/saleList")
public class SaleListAdminController {

	@Resource
	private SaleListService saleListService;
	
	@Resource
	private SaleListGoodsService saleListGoodsService;

	@Resource
    private SaleListPersonService saleListPersonService;

	@Resource
    private SaleListPaymentService saleListPaymentService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private LogService logService;

	@Resource
	private ExcelExportTask excelExportTask;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	/**
	 * 获取销售单号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/genCode")
	@RequiresPermissions(value="销售出库")
	public String genCode()throws Exception{
		StringBuffer code=new StringBuffer("XS");
		code.append(DateUtil.getCurrentDateStr());
		String saleNumber=saleListService.getTodayMaxSaleNumber();
		if(saleNumber!=null){
			code.append(StringUtil.formatCode(saleNumber));
		}else{
			code.append("0001");
		}
		return code.toString();
	}
	
	/**
	 * 添加销售单 以及所有销售单商品
	 * @param saleList
	 * @param goodsJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value="销售出库")
	public Map<String,Object> save(SaleList saleList,String goodsJson, String salePersonsJson)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		saleList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson=new Gson();
		List<SaleListGoods> plgList=gson.fromJson(goodsJson,new TypeToken<List<SaleListGoods>>(){}.getType());
		List<SaleListPerson> psnList = gson.fromJson(salePersonsJson, new TypeToken<List<SaleListPerson>>(){}.getType());

		saleListService.save(saleList, plgList, psnList);
		logService.save(new Log(Log.ADD_ACTION,"添加销售单"));
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 根据条件查询所有销售单信息
	 * @param saleList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value={"销售单据查询","客户统计"},logical=Logical.OR)
	public Map<String,Object> list(SaleList saleList, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows, HttpSession session)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		Role currentRole = (Role) session.getAttribute("currentRole");

		if ("销售".equals(currentRole.getName())) {
			User user = (User) session.getAttribute("currentUser");
			Shop shop = new Shop();
			shop.setId(user.getShop().getId());
			saleList.setShop(shop);
		}

		List<SaleList> saleListList=saleListService.list(saleList, page, rows, Direction.DESC, "saleDate");
		for (SaleList saleList1 : saleListList) {
			List<SaleListPerson> saleListPeople = saleListPersonService.findListBySaleListId(saleList1);

			StringBuffer buffer = new StringBuffer();
			for (SaleListPerson saleListPerson : saleListPeople) {
				buffer.append(saleListPerson.getSalePerson().getName()).append(":")
						.append(saleListPerson.getAmount() * 10000 / 100.0F).append(";");
			}

			saleList1.setSaleListPerson(buffer.toString());
		}

		resultMap.put("rows", saleListList);
		resultMap.put("total", saleListService.getCount(saleList));
		logService.save(new Log(Log.SEARCH_ACTION,"销售单查询"));
		return resultMap;
	}
	
	/**
	 * 根据条件获取商品销售信息
	 * @param saleList
	 * @param saleListGoods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listCount")
	@RequiresPermissions(value="商品销售统计")
	public Map<String,Object> listCount(SaleList saleList,SaleListGoods saleListGoods, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<SaleList> saleListList=saleListService.list(saleList, page, rows, Direction.DESC, "saleDate");
		for(SaleList sl:saleListList){
			saleListGoods.setSaleList(sl);
			List<SaleListGoods> slgList=saleListGoodsService.list(saleListGoods);
			sl.setSaleListGoodsList(slgList);
		}
		resultMap.put("rows", saleListList);
		resultMap.put("total", saleListService.getCount(saleList));
		logService.save(new Log(Log.SEARCH_ACTION,"商品销售统计查询"));
		return resultMap;
	}
	
	/**
	 * 根据销售单id查询所有销售单商品
	 * @param saleListId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listGoods")
	@RequiresPermissions(value="销售单据查询")
	public Map<String,Object> listGoods(Integer saleListId)throws Exception{
		if(saleListId==null){
			return null;
		}
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("rows", saleListGoodsService.listBySaleListId(saleListId));
		logService.save(new Log(Log.SEARCH_ACTION,"销售单商品查询"));
		return resultMap;
	}
	
	/**
	 * 删除销售单 以及销售单里的商品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value="销售单据查询")
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();

		saleListGoodsService.delete(id);
		saleListPersonService.delete(id);
		saleListPaymentService.delete(id);

		saleListService.delete(id);
		logService.save(new Log(Log.DELETE_ACTION,"删除销售单信息："+saleListService.findById(id)));
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 修改销售单的支付状态
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update")
	@RequiresPermissions(value="客户统计")
	public Map<String,Object> update(Integer id)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		SaleList saleList=saleListService.findById(id);
		saleList.setState(1);
		saleListService.update(saleList);
		resultMap.put("success", true);
		return resultMap;
	}


	@RequestMapping("/updateAmount")
	@RequiresPermissions(value = "销售单据查询")
	public Map<String,Object> updateAmount(SaleListPayment saleListPayment)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
        if (saleListPaymentService.add(saleListPayment)) {
            resultMap.put("success", true);
        } else {
            resultMap.put("success", false);
            resultMap.put("errorInfo", "已付金额不能大于实收全款");
        }
		return resultMap;
	}

	@RequestMapping("/updateReceiverGoods")
	@RequiresPermissions(value = "销售单据查询")
	public Map<String, Object> updateReceiverGoods(SaleList srcSaleList) throws Exception {
		Map<String,Object> resultMap=new HashMap<>();
		SaleList saleList=saleListService.findById(srcSaleList.getId());
		saleList.setReceiverGoods(1);
		saleList.setDeliveryDate(srcSaleList.getDeliveryDate());
		saleListService.update(saleList);

		//修改商品库存
		saleListService.refreshGoodsNum(saleList);

		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 按日统计分析
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/countSaleByDay")
	@RequiresPermissions(value="按日统计分析")
	public Map<String,Object> countSaleByDay(String begin,String end)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<SaleCount> scList=new ArrayList<>();
		List<String> dates=DateUtil.getRangeDates(begin, end);
		List<Object> ll=saleListService.countSaleByDay(begin, end);
		for(String date:dates){
			SaleCount sc=new SaleCount();
			sc.setDate(date);
			boolean flag=false;
			for(Object o:ll){
				Object []oo=(Object[]) o;
				String dd=oo[2].toString().substring(0,10);
				if(dd.equals(date)){ // 存在
					sc.setAmountCost(MathUtil.format2Bit(Float.parseFloat(oo[0].toString()))); // 成本总金额
					sc.setAmountSale(MathUtil.format2Bit(Float.parseFloat(oo[1].toString()))); // 销售总金额
					sc.setAmountProfit(MathUtil.format2Bit(sc.getAmountSale()-sc.getAmountCost())); // 销售利润
					flag=true;
				}
			}
			if(!flag){
				sc.setAmountCost(0);
				sc.setAmountSale(0);
				sc.setAmountProfit(0);
			}
			scList.add(sc);
		}
		resultMap.put("rows", scList);
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 按月统计分析
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/countSaleByMonth")
	@RequiresPermissions(value="按月统计分析")
	public Map<String,Object> countSaleByMonth(String begin,String end)throws Exception{
		Map<String,Object> resultMap=new HashMap<>();
		List<SaleCount> scList=new ArrayList<>();
		List<String> dates=DateUtil.getRangeMonths(begin, end);
		List<Object> ll=saleListService.countSaleByMonth(begin, end);
		for(String date:dates){
			SaleCount sc=new SaleCount();
			sc.setDate(date);
			boolean flag=false;
			for(Object o:ll){
				Object []oo=(Object[]) o;
				String dd=oo[2].toString().substring(0,7);
				if(dd.equals(date)){ // 存在
					sc.setAmountCost(MathUtil.format2Bit(Float.parseFloat(oo[0].toString()))); // 成本总金额
					sc.setAmountSale(MathUtil.format2Bit(Float.parseFloat(oo[1].toString()))); // 销售总金额
					sc.setAmountProfit(MathUtil.format2Bit(sc.getAmountSale()-sc.getAmountCost())); // 销售利润
					flag=true;
				}
			}
			if(!flag){
				sc.setAmountCost(0);
				sc.setAmountSale(0);
				sc.setAmountProfit(0);
			}
			scList.add(sc);
		}
		resultMap.put("rows", scList);
		resultMap.put("success", true);
		return resultMap;
	}


	@RequestMapping("/export")
	@RequiresPermissions(value = "销售单据查询")
	public void export(SaleList saleList, HttpServletResponse httpResponse)
			throws Exception {
		File excelFile = excelExportTask.startExport("sale", saleList);

		DownloadUtil.response(excelFile, httpResponse);
	}

	@RequestMapping("/confirmReceiverGoods")
	@RequiresPermissions(value = "销售单据查询")
	public void confirmReceiverGoods() {

	}

}
