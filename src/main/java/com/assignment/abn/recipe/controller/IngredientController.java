package com.assignment.abn.recipe.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.abn.recipe.model.Ingredient;
import com.assignment.abn.recipe.request.CreateIngredientRequest;
import com.assignment.abn.recipe.response.CreateEntityResponse;
import com.assignment.abn.recipe.response.IngredientResponse;
import com.assignment.abn.recipe.service.IngredientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "IngredientController", tags = "Ingredient Controller", description = "Create, update, delete, list ingredients")
@RestController
@RequestMapping(value = "api/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService service;

    @ApiOperation(value = "Create an ingredient")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Ingredient created"),
            		        @ApiResponse(code = 400, message = "Bad input")})
    @PostMapping()
    public CreateEntityResponse createIngredient(@ApiParam(value = "Properties of the Ingredient", required = true) @Valid @RequestBody CreateIngredientRequest request) {
        Integer id = service.create(request);
        return new CreateEntityResponse(id);
    }
    
    @ApiOperation(value = "List all ingredients")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful request")})
    @GetMapping("/page/{page}/size/{size}")
    public List<IngredientResponse> getIngredientList(@PathVariable(name = "page") int page,
                                                      @PathVariable(name = "size") int size) {
        List<Ingredient> list = service.list(page, size);
        return list.stream().map(IngredientResponse::new).collect(Collectors.toList());
    }

    @ApiOperation(value = "List one ingredient by its ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful request"),
    						@ApiResponse(code = 404, message = "Ingredient not found by the given ID")
    })
    @GetMapping("/{id}")
    public IngredientResponse getIngredient(@ApiParam(value = "Ingredient ID", required = true) @PathVariable(name = "id") Integer id) {
        Ingredient ingredient = service.findById(id);
        return new IngredientResponse(ingredient);
    }

    @ApiOperation(value = "Delete the ingredient")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successful operation"),
    						@ApiResponse(code = 400, message = "Invalid input"),
    						@ApiResponse(code = 404, message = "Ingredient not found by the given ID")
    })
    @DeleteMapping()
    public void deleteIngredient(@ApiParam(value = "ingredient ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id) {
    	service.delete(id);
    }
}
