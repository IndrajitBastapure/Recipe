package com.assignment.abn.recipe.search;

import javax.persistence.criteria.*;

import com.assignment.abn.recipe.model.Recipe;

import static com.assignment.abn.recipe.config.DatabaseAttributes.INGREDIENT_KEY;

public class SearchFilterContains implements SearchFilter {

    @Override
    public boolean couldBeApplied(SearchOperation opt) {
        return opt == SearchOperation.CONTAINS;
    }

	@Override
	public Predicate apply(CriteriaBuilder cb, String filterKey, String filterValue, Root<Recipe> root,
			Join<Object, Object> subRoot) {
		 if (filterKey.equals(INGREDIENT_KEY))
	            return cb.like(cb.lower(subRoot.get(filterKey).as(String.class)), "%" + filterValue + "%");

	        return cb.like(cb.lower(root.get(filterKey).as(String.class)), "%" + filterValue + "%");
	}
}
