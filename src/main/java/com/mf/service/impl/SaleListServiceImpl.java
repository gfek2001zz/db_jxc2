package com.mf.service.impl;


import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.mf.entity.SaleListPerson;
import com.mf.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mf.entity.Goods;
import com.mf.entity.SaleList;
import com.mf.entity.SaleListGoods;
import com.mf.service.SaleListService;
import com.mf.util.StringUtil;

/**
 * 销售单Service实现类
 * @author Administrator
 *
 */
@Service("saleListService")
public class SaleListServiceImpl implements SaleListService{

	@Resource
	private SaleListRepository saleListRepository;
	
	@Resource
	private GoodsTypeRepository goodsTypeRepository;
	
	@Resource
	private GoodsRepository goodsRepository;
	
	@Resource
	private SaleListGoodsRepository saleListGoodsRepository;

	@Resource
	private SaleListPersonRepository saleListPersonRepository;

	@Override
	public String getTodayMaxSaleNumber() {
		return saleListRepository.getTodayMaxSaleNumber();
	}

	@Transactional
	public void save(SaleList saleList, List<SaleListGoods> saleListGoodsList, List<SaleListPerson> saleListPersonList) {
		for(SaleListGoods saleListGoods:saleListGoodsList){
			saleListGoods.setType(goodsTypeRepository.findOne(saleListGoods.getTypeId())); // 设置类别
			saleListGoods.setSaleList(saleList); // 设置销售单
			saleListGoodsRepository.save(saleListGoods);
			// 修改商品库存
			Goods goods=goodsRepository.findOne(saleListGoods.getGoodsId());
			goods.setInventoryQuantity(goods.getInventoryQuantity()-saleListGoods.getNum());
			goods.setState(2);
			goodsRepository.save(goods);
		}

		for(SaleListPerson person : saleListPersonList) {
			person.setSaleList(saleList);
			saleListPersonRepository.save(person);
		}

		saleListRepository.save(saleList); // 保存销售单
	}

	@Override
	public List<SaleList> list(SaleList saleList, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1,pageSize);
		Page<SaleList> saleLists = saleListRepository.findAll(new Specification<SaleList>() {
			
			@Override
			public Predicate toPredicate(Root<SaleList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(saleList!=null){
					if(StringUtil.isNotEmpty(saleList.getSaleNumber())){
						predicate.getExpressions().add(cb.like(root.get("saleNumber"), "%"+saleList.getSaleNumber().trim()+"%"));
					}
					if(saleList.getCustomer()!=null && saleList.getCustomer().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), saleList.getCustomer().getId()));
					}
					if(saleList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), saleList.getState()));
					}
					if(saleList.getbSaleDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("saleDate"), saleList.getbSaleDate()));
					}
					if(saleList.geteSaleDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("saleDate"), saleList.geteSaleDate()));
					}
				}
				return predicate;
			}
		}, pageable);

		return saleLists.getContent();
	}

	@Override
	public SaleList findById(Integer id) {
		return saleListRepository.findOne(id);
	}

	@Transactional
	public void delete(Integer id) {
		saleListGoodsRepository.deleteBySaleListId(id);
		saleListRepository.delete(id);
	}

	@Override
	public void update(SaleList saleList) {
		saleListRepository.save(saleList);
	}

	@Override
	public List<Object> countSaleByDay(String begin, String end) {
		return saleListRepository.countSaleByDay(begin, end);
	}

	@Override
	public List<Object> countSaleByMonth(String begin, String end) {
		return saleListRepository.countSaleByMonth(begin, end);
	}


	

}
