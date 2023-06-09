package com.assignment.abn.recipe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.abn.recipe.config.MessageProvider;
import com.assignment.abn.recipe.exception.NotFoundException;
import com.assignment.abn.recipe.model.Ingredient;
import com.assignment.abn.recipe.model.Recipe;
import com.assignment.abn.recipe.repository.RecipeRepository;
import com.assignment.abn.recipe.request.CreateRecipeRequest;
import com.assignment.abn.recipe.request.RecipeSearchRequest;
import com.assignment.abn.recipe.request.SearchCriteriaRequest;
import com.assignment.abn.recipe.request.UpdateRecipeRequest;
import com.assignment.abn.recipe.response.RecipeResponse;
import com.assignment.abn.recipe.search.RecipeSpecificationBuilder;
import com.assignment.abn.recipe.search.SearchCriteria;

@Service
@Transactional
public class RecipeService {
	
	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private IngredientService ingredientService;
	@Autowired
	private MessageProvider messageProvider;
	
	public Integer createRecipe(CreateRecipeRequest createRecipeRequest) {
        Set<Ingredient> ingredients = Optional.ofNullable(createRecipeRequest.getIngredientIds())
                .map(ingredientService::getIngredientsByIds)
                .orElse(null);

        Recipe recipe = new Recipe();
        recipe.setName(createRecipeRequest.getName());
        recipe.setInstructions(createRecipeRequest.getInstructions());
        recipe.setType(createRecipeRequest.getType());
        recipe.setNumberOfServings(createRecipeRequest.getNumberOfServings());
        recipe.setRecipeIngredients(ingredients);

        Recipe createdRecipe = recipeRepository.save(recipe);

        return createdRecipe.getId();
    }

    public List<Recipe> getRecipeList(int page, int size) {
        Pageable pageRequest
                = PageRequest.of(page, size);
        return recipeRepository.findAll(pageRequest).getContent();
    }

    public Recipe getRecipeById(int id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageProvider.getMessage("recipe.notFound")));
    }

    public void updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
        Recipe recipe = recipeRepository.findById(updateRecipeRequest.getId())
                .orElseThrow(() -> new NotFoundException(messageProvider.getMessage("recipe.notFound")));

        Set<Ingredient> ingredients = Optional.ofNullable(updateRecipeRequest.getIngredientIds())
                .map(ingredientService::getIngredientsByIds)
                .orElse(null);

        recipe.setName(updateRecipeRequest.getName());
        recipe.setType(updateRecipeRequest.getType());
        recipe.setNumberOfServings(updateRecipeRequest.getNumberOfServings());
        recipe.setInstructions(updateRecipeRequest.getInstructions());

        if (Optional.ofNullable(ingredients).isPresent()) recipe.setRecipeIngredients(ingredients);

        recipeRepository.save(recipe);
    }

    public void deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            throw new NotFoundException(messageProvider.getMessage("recipe.notFound"));
        }

        recipeRepository.deleteById(id);
    }

    public List<RecipeResponse> findBySearchCriteria(RecipeSearchRequest recipeSearchRequest, int page, int size) {
        List<SearchCriteria> searchCriterionRequests = new ArrayList<>();
        RecipeSpecificationBuilder builder = new RecipeSpecificationBuilder(searchCriterionRequests);
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name")
                .ascending());

        Specification<Recipe> recipeSpecification = createRecipeSpecification(recipeSearchRequest, builder);
        Page<Recipe> filteredRecipes = recipeRepository.findAll(recipeSpecification, pageRequest);

        return filteredRecipes.toList().stream()
                .map(RecipeResponse::new)
                .collect(Collectors.toList());
    }

    private Specification<Recipe> createRecipeSpecification(RecipeSearchRequest recipeSearchRequest,
                                                            RecipeSpecificationBuilder builder) {
        List<SearchCriteriaRequest> searchCriteriaRequests = recipeSearchRequest.getSearchCriteriaRequests();

        if (Optional.ofNullable(searchCriteriaRequests).isPresent()) {
            List<SearchCriteria> searchCriteriaList = searchCriteriaRequests.stream()
                    .map(SearchCriteria::new)
                    .collect(Collectors.toList());

            if (!searchCriteriaList.isEmpty()) searchCriteriaList.forEach(criteria -> {
                criteria.setDataOption(recipeSearchRequest.getDataOption());
                builder.with(criteria);
            });
        }

        return builder
                .build()
                .orElseThrow(() -> new NotFoundException(messageProvider.getMessage("criteria.notFound")));
    }
	

}
