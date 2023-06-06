package com.assignment.abn.recipe.builder;

import com.assignment.abn.recipe.request.CreateIngredientRequest;

public class CreateIngredientRequestBuilder {
    private String name;

    public CreateIngredientRequest build() {
        return new CreateIngredientRequest(name);
    }

    public CreateIngredientRequestBuilder withName(String firstName) {
        this.name = firstName;
        return this;
    }


}
