package com.mf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.mf.entity.Card;

/**
 * 客户Repository接口
 * @author cj
 *
 */
public interface CardRepository extends JpaRepository<Card, Integer>,JpaSpecificationExecutor<Card>{

	/**
	 * 根据名称模糊查询客户信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_card where name like ?1",nativeQuery=true)
	public List<Card> findByName(String name);
}
