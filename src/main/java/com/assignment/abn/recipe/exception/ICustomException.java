package com.assignment.abn.recipe.exception;

import org.springframework.http.HttpStatus;

public interface ICustomException {
    HttpStatus getStatus();
}