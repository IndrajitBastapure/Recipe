package com.assignment.abn.recipe.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.assignment.abn.recipe.model.Recipe;

import static com.assignment.abn.recipe.config.DatabaseAttributes.INGREDIENT_KEY;

public class SearchFilterEqual implements SearchFilter {

    @Override
    public boolean couldBeApplied(SearchOperation opt) {
        return opt == SearchOperation.EQUAL;
    }

	@Override
	public Predicate apply(CriteriaBuilder cb, String filterKey, String filterValue, Root<Recipe> root,
			Join<Object, Object> subRoot) {
		if (filterKey.equals(INGREDIENT_KEY))
            return cb.equal(subRoot.get(filterKey).as(String.class), filterValue);

        return cb.equal(root.get(filterKey).as(String.class), filterValue);
	}
}
