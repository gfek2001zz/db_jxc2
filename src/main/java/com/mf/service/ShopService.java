package com.mf.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.mf.entity.Shop;

/**
 * 客户Service接口
 * @author cj
 *
 */
public interface ShopService {

	/**
	 * 根据名称模糊查询客户信息
	 * @param name
	 * @return
	 */
	public List<Shop> findByName(String name);
	
	/**
	 * 根据条件分页查询客户信息
	 * @param shop
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Shop> list(Shop shop,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param shop
	 * @return
	 */
	public Long getCount(Shop shop);
	
	/**
	 * 添加或者修改客户信息
	 * @param shop
	 */
	public void save(Shop shop);
	
	/**
	 * 根据id删除客户
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Shop findById(Integer id);
}
