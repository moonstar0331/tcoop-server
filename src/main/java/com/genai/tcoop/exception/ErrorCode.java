package com.genai.tcoop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_ACCOUNT_ID(HttpStatus.CONFLICT, "User Account Id is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    PLANNER_NOT_FOUND(HttpStatus.NOT_FOUND, "Planner not founded"),
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Plan not founded"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid");

    private HttpStatus status;
    private String message;
}
