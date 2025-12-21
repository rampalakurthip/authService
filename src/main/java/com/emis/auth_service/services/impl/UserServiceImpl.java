package com.emis.auth_service.services.impl;

import com.emis.auth_service.dto.request.UserSignUpDTO;
import com.emis.auth_service.model.UserModel;
import com.emis.auth_service.repository.UserRepository;
import com.emis.auth_service.services.UserService;

import com.emis.auth_service.utils.PasswordEncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public UserSignUpDTO SignUp(UserSignUpDTO userSignUpDTO) {
        log.info("request to sign up user: {}", userSignUpDTO);
        var userModel = new UserModel();
        userModel.setUserId(UUID.randomUUID());
        mapUserDTOToEntity(userModel, userSignUpDTO);
        userRepository.save(userModel);
        return null;
    }

    private void mapUserDTOToEntity(UserModel userModel, UserSignUpDTO userSignUpDTO) {
        userModel.setFirstName(userSignUpDTO.getFirstName());
        userModel.setLastName(userSignUpDTO.getLastName());
        userModel.setEmail(userSignUpDTO.getEmail());
        userModel.setPasswordHash(PasswordEncryptionUtil.encryptPassword(userSignUpDTO.getPassword()));
    }
}
