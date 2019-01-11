package com.mf.service.impl;


import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mf.entity.Shop;
import com.mf.repository.ShopRepository;
import com.mf.service.ShopService;
import com.mf.util.StringUtil;

/**
 * 客户Service实现类
 * @author cj
 *
 */
@Service("shopService")
public class ShopServiceImpl implements ShopService{

	@Resource
	private ShopRepository shopRepository;


	@Override
	public List<Shop> list(Shop shop, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1,pageSize);
		Page<Shop> pageShop=shopRepository.findAll(new Specification<Shop>() {
			
			@Override
			public Predicate toPredicate(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(shop!=null){
					if(StringUtil.isNotEmpty(shop.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+shop.getName()+"%"));
					}
				}
				return predicate;
			}
		},pageable);
		return pageShop.getContent();
	}

	@Override
	public Long getCount(Shop shop) {
		Long count=shopRepository.count(new Specification<Shop>() {

			@Override
			public Predicate toPredicate(Root<Shop> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(shop!=null){
					if(StringUtil.isNotEmpty(shop.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+shop.getName()+"%"));
					}
				}
				return predicate;
			}
			
		});
		return count;
	}

	@Override
	public void save(Shop shop) {
		shopRepository.save(shop);
	}

	@Override
	public void delete(Integer id) {
		shopRepository.delete(id);
	}

	@Override
	public Shop findById(Integer id) {
		return shopRepository.findOne(id);
	}

	@Override
	public List<Shop> findByName(String name) {
		List<Shop> shops = shopRepository.findByName(name);
		for (Shop shop : shops) {
			shop.setName(shop.getName() + "-" + shop.getName2());
		}

		return shops;
	}
	


}
