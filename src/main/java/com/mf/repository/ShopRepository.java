package com.mf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.mf.entity.Shop;

/**
 * 门店Repository接口
 * @author cj
 *
 */
public interface ShopRepository extends JpaRepository<Shop, Integer>,JpaSpecificationExecutor<Shop>{

	/**
	 * 根据名称模糊查询门店信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_shop where name like ?1",nativeQuery=true)
	public List<Shop> findByName(String name);
}
