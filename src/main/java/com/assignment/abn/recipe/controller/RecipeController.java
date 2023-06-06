package com.assignment.abn.recipe.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.assignment.abn.recipe.model.Recipe;
import com.assignment.abn.recipe.request.CreateRecipeRequest;
import com.assignment.abn.recipe.request.RecipeSearchRequest;
import com.assignment.abn.recipe.request.UpdateRecipeRequest;
import com.assignment.abn.recipe.response.CreateEntityResponse;
import com.assignment.abn.recipe.response.RecipeResponse;
import com.assignment.abn.recipe.service.RecipeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "RecipeController", tags = "Recipe Controller", description = "Create, Update, Delete, List Recipes")
@RequestMapping("/api/recipe")
public class RecipeController {
	
	@Autowired
	private RecipeService service;
	
	@ApiOperation(value = "Create a recipe")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Recipe created"), @ApiResponse(code = 400, message = "Bad input")})
	@PostMapping()
	public CreateEntityResponse createRecipe(@ApiParam(value = "Properties of the recipe", required = true) @Valid @RequestBody CreateRecipeRequest request){
			Integer id = service.createRecipe(request);
			 return new CreateEntityResponse(id);
	}
	
	@ApiOperation(value = "List one recipe by its ID", response = RecipeResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful request"),@ApiResponse(code = 404, message = "Recipe not found by the given ID")})
	@GetMapping("/{id}")
	public RecipeResponse getRecipe(@ApiParam(value = "Recipe ID", required = true) @PathVariable(name = "id") Integer id) {
		Recipe recipe = service.getRecipeById(id);
		 return new RecipeResponse(recipe);
	}
	
	@ApiOperation(value = "List all Recipes")
	 @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful request")})
	@GetMapping("/page/{page}/size/{size}")
	public List<RecipeResponse> getRecipeList(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size){	
		List<Recipe> list = service.getRecipeList(page, size);
        return list.stream().map(RecipeResponse::new).collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Update the recipe")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Ingredient created"),@ApiResponse(code = 400, message = "Bad input")})
	@PutMapping()
	public void updateRecipe(@ApiParam(value = "Properties of the recipe", required = true) @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest){	
			service.updateRecipe(updateRecipeRequest);
	}
	
	 @ApiOperation(value = "Delete the recipe")
	    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful operation"),
	            				@ApiResponse(code = 400, message = "Invalid input"),
	            				@ApiResponse(code = 404, message = "Recipe not found by the given ID")})
	@DeleteMapping()
	public void deleteRecipe(@ApiParam(value = "Recipe ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id){
	
			service.deleteRecipe(id);
	}
	 
	 @ApiOperation(value = "Search recipes by given parameters")
	    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful request"),
	    					    @ApiResponse(code = 404, message = "Different error messages related to criteria and recipe")
	    })
	    @PostMapping("/search")
	    public List<RecipeResponse> searchRecipe(@RequestParam(name = "page", defaultValue = "0") int page,
	                                             @RequestParam(name = "size", defaultValue = "10") int size,
	                                             @ApiParam(value = "Properties of the the search")
	                                             @RequestBody @Valid RecipeSearchRequest recipeSearchRequest) {
	        return service.findBySearchCriteria(recipeSearchRequest, page, size);
	    }
}
