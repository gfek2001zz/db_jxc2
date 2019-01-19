package com.mf.repository;

import com.mf.entity.SaleListPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SaleListPersonRepository extends JpaRepository<SaleListPerson, Integer>, JpaSpecificationExecutor<SaleListPerson> {


    /**
     * 根据销售单id删除所有销售员
     * @param saleListId
     * @return
     */
    @Query(value="select * FROM t_sale_list_person t, t_sale_person u WHERE t.sale_person_id = u.id AND sale_id=?1",nativeQuery=true)
    @Modifying
    public List<SaleListPerson> findListBySaleListId(Integer saleListId);
    /**
     * 根据销售单id删除所有销售单商品
     * @param saleListId
     * @return
     */
    @Transactional
    @Query(value="delete FROM t_sale_list_person WHERE sale_id=?1",nativeQuery=true)
    @Modifying
    public void deleteBySaleListId(Integer saleListId);
}
