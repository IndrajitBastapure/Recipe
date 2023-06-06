package com.assignment.abn.recipe.search;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.assignment.abn.recipe.model.Recipe;

public interface SearchFilter  {
    boolean couldBeApplied(SearchOperation opt);
    Predicate apply(CriteriaBuilder cb, String filterKey, String filterValue, Root<Recipe> root, Join<Object, Object> subRoot);
}