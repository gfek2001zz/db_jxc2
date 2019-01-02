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

import com.mf.entity.Card;
import com.mf.repository.CardRepository;
import com.mf.service.CardService;
import com.mf.util.StringUtil;

/**
 * 银行卡Service实现类
 * @author cj
 *
 */
@Service("cardService")
public class CardServiceImpl implements CardService{

	@Resource
	private CardRepository cardRepository;


	@Override
	public List<Card> list(Card card, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1,pageSize);
		Page<Card> pageCard=cardRepository.findAll(new Specification<Card>() {
			
			@Override
			public Predicate toPredicate(Root<Card> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(card!=null){
					if(StringUtil.isNotEmpty(card.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+card.getName()+"%"));
					}
				}
				return predicate;
			}
		},pageable);
		return pageCard.getContent();
	}

	@Override
	public Long getCount(Card card) {
		Long count=cardRepository.count(new Specification<Card>() {

			@Override
			public Predicate toPredicate(Root<Card> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(card!=null){
					if(StringUtil.isNotEmpty(card.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+card.getName()+"%"));
					}
				}
				return predicate;
			}
			
		});
		return count;
	}

	@Override
	public void save(Card card) {
		cardRepository.save(card);
	}

	@Override
	public void delete(Integer id) {
		cardRepository.delete(id);
	}

	@Override
	public Card findById(Integer id) {
		return cardRepository.findOne(id);
	}

	@Override
	public List<Card> findByName(String name) {
		return cardRepository.findByName(name);
	}
	


}
