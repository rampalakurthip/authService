package com.emis.auth_service.services;

import com.emis.auth_service.dto.request.UserSignUpDTO;

public interface UserService {
    UserSignUpDTO SignUp(UserSignUpDTO userSignUpDTO);
}
