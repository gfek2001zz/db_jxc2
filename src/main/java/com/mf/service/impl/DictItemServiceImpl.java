package com.mf.service.impl;

import com.mf.entity.DictItem;
import com.mf.repository.DictItemRepository;
import com.mf.service.DictItemService;
import com.mf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service("dictItemService")
public class DictItemServiceImpl implements DictItemService {

    @Resource
    private DictItemRepository dictItemRepository;


    @Override
    public List<DictItem> list(DictItem dictItem) {

        return dictItemRepository.findAll(new Specification<DictItem> () {
            @Override
            public Predicate toPredicate(Root<DictItem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (dictItem != null) {
                    if (StringUtil.isNotEmpty(dictItem.getClassifyCode())) {
                        predicate.getExpressions().add(cb.equal(root.get("classifyCode"), dictItem.getClassifyCode()));
                    }
                }

                return predicate;
            }
        });
    }

    @Override
    public Long getCount(DictItem dictItem) {
        return dictItemRepository.count(new Specification<DictItem>() {
            @Override
            public Predicate toPredicate(Root<DictItem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (dictItem != null) {
                    if (StringUtil.isNotEmpty(dictItem.getClassifyCode())) {
                        predicate.getExpressions().add(cb.equal(root.get("classifyCode"), dictItem.getClassifyCode()));
                    }
                }

                return predicate;
            }
        });
    }

    @Override
    public void save(DictItem dictItem) {
        dictItemRepository.save(dictItem);
    }

    @Override
    public void delete(Integer id) {
        dictItemRepository.delete(id);
    }

    @Override
    public DictItem findById(Integer id) {
        return dictItemRepository.findOne(id);
    }

    @Override
    public DictItem findItemByItemCode(String classifyCode, String itemCode) {
        return dictItemRepository.findItemByItemCode(classifyCode, itemCode);
    }
}
