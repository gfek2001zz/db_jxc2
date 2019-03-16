package com.mf.service.impl;

import com.mf.entity.SalePerson;
import com.mf.repository.SalePersonRepository;
import com.mf.service.SaleListPersonService;
import com.mf.service.SalePersonService;
import com.mf.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service(value = "salePersonService")
public class SalePersonServiceImpl implements SalePersonService {
    @Resource
    private SalePersonRepository salePersonRepository;

    @Override
    public List<SalePerson> list(SalePerson salePerson, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable=new PageRequest(page-1,pageSize);
        Page<SalePerson> salePersonPage = salePersonRepository.findAll(new Specification<SalePerson>() {
            @Override
            public Predicate toPredicate(Root<SalePerson> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return getPredicate(root, cb, salePerson);
            }
        }, pageable);


        return salePersonPage.getContent();
    }



    @Override
    public Long getCount(SalePerson salePerson) {
        return salePersonRepository.count(new Specification<SalePerson>() {
            @Override
            public Predicate toPredicate(Root<SalePerson> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return getPredicate(root, cb, salePerson);
            }
        });
    }

    @Override
    public void save(SalePerson salePerson) {
        salePersonRepository.save(salePerson);
    }

    @Override
    public void delete(SalePerson salePerson) {
        salePersonRepository.delete(salePerson.getId());
    }

    @Override
    public SalePerson findById(int id) {
        return salePersonRepository.findOne(id);
    }


    private Predicate getPredicate(Root<SalePerson> root, CriteriaBuilder cb, SalePerson salePerson) {
        Predicate predicate=cb.conjunction();

        if (StringUtil.isNotEmpty(salePerson.getName())) {
            predicate.getExpressions().add(cb.like(root.get("name"), "%" + salePerson.getName() + "%"));
        }

        if (salePerson.getShop() != null && salePerson.getShop().getId() != null) {
            predicate.getExpressions().add(cb.equal(root.get("shop").get("id"), salePerson.getShop().getId()));
        }

        return predicate;
    }
}
