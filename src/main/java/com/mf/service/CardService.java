package com.mf.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.mf.entity.Card;

/**
 * 银行卡Service接口
 * @author cj
 *
 */
public interface CardService {

	/**
	 * 根据名称模糊查询客户信息
	 * @param name
	 * @return
	 */
	public List<Card> findByName(String name);
	
	/**
	 * 根据条件分页查询客户信息
	 * @param card
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Card> list(Card card,Integer page,Integer pageSize,Direction direction,String...properties);
	
	/**
	 * 获取总记录数
	 * @param card
	 * @return
	 */
	public Long getCount(Card card);
	
	/**
	 * 添加或者修改客户信息
	 * @param card
	 */
	public void save(Card card);
	
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
	public Card findById(Integer id);
}
