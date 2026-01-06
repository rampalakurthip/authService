package com.emis.auth_service.exceptions;

import lombok.Getter;

@Getter
public class SomethingWentWrongException extends RuntimeException {

    private final String apiPath;           // /api/v1/iam/roles
    private final String errMessage;       // "Failed to fetch roles"

    public SomethingWentWrongException(String apiPath, String errMessage) {
        super("API Error - Path: " + apiPath +", Msg: " + errMessage);
        this.apiPath = apiPath;
        this.errMessage = errMessage;
    }

}
