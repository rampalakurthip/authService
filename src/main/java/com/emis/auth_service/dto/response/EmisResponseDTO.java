package com.emis.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmisResponseDTO<T> {
    private int status;
    private String errorCode;
    private String message;
    private T data;
}
