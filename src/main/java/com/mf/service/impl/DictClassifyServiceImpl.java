package com.mf.service.impl;

import com.mf.entity.DictClassify;
import com.mf.repository.DictClassifyRepository;
import com.mf.service.DictClassifyService;
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

@Service(value = "dictClassifyService")
public class DictClassifyServiceImpl implements DictClassifyService {

    @Resource
    private DictClassifyRepository dictClassifyRepository;

    /**
     * 分页查询数据字典分类
     * @param dictClassify
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    @Override
    public List<DictClassify> list(DictClassify dictClassify, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page-1,pageSize);
        Page<DictClassify> dictClassifies = dictClassifyRepository.findAll(new Specification<DictClassify>() {
            @Override
            public Predicate toPredicate(Root<DictClassify> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (dictClassify != null) {
                    if (StringUtil.isNotEmpty(dictClassify.getClassifyCode())) {
                        predicate.getExpressions().add(cb.like(root.get("classify_code"), "%"+dictClassify.getClassifyCode()+"%"));
                    }
                }

                return predicate;
            }
        }, pageable);


        return dictClassifies.getContent();
    }

    /**
     * 获取分页数量
     * @param dictClassify
     * @return
     */
    @Override
    public Long getCount(DictClassify dictClassify) {

        Long count = dictClassifyRepository.count(new Specification<DictClassify>() {
            @Override
            public Predicate toPredicate(Root<DictClassify> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (dictClassify != null) {
                    if (StringUtil.isNotEmpty(dictClassify.getClassifyCode())) {
                        predicate.getExpressions().add(cb.like(root.get("classify_code"), "%"+dictClassify.getClassifyCode()+"%"));
                    }
                }

                return predicate;
            }
        });


        return count;
    }

    @Override
    public void save(DictClassify dictClassify) {
        dictClassifyRepository.save(dictClassify);
    }

    @Override
    public void delete(Integer id) {
        dictClassifyRepository.delete(id);
    }

    @Override
    public DictClassify findById(Integer id) {
        return dictClassifyRepository.findOne(id);
    }
}
