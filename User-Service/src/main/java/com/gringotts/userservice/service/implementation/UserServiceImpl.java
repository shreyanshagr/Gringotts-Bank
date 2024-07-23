package com.gringotts.userservice.service.implementation;

import com.gringotts.userservice.exception.EmptyFields;
import com.gringotts.userservice.exception.ResourceConflictException;
import com.gringotts.userservice.exception.ResourceNotFound;
import com.gringotts.userservice.external.AccountService;
import com.gringotts.userservice.mapper.UserMapper;
import com.gringotts.userservice.model.dto.CreateUser;
import com.gringotts.userservice.model.dto.UserDto;
import com.gringotts.userservice.model.dto.UserUpdate;
import com.gringotts.userservice.model.dto.UserUpdateStatus;
import com.gringotts.userservice.model.dto.response.Response;
import com.gringotts.userservice.model.entity.User;
import com.gringotts.userservice.model.entity.UserProfile;
import com.gringotts.userservice.model.external.Account;
import com.gringotts.userservice.payload.Status;
import com.gringotts.userservice.repository.UserRepository;
import com.gringotts.userservice.service.KeycloakService;
import com.gringotts.userservice.service.UserService;
import com.gringotts.userservice.utils.FieldChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final AccountService accountService;

    private final UserMapper userMapper = new UserMapper();

    @Value("${spring.application.success}")
    private String responseCodeSuccess;

    @Value("${spring.application.not_found}")
    private String responseCodeNotFound;

    @Override
    public Response createUser(CreateUser userDto) {

        List<UserRepresentation> userRepresentations = keycloakService.readUserByEmail(userDto.getEmailId());
        if(!userRepresentations.isEmpty()) {
            log.error("This emailId is already registered as a user");
            throw new ResourceConflictException("This emailId is already registered as a user");
        }

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDto.getEmailId());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(false);
        userRepresentation.setEmail(userDto.getEmailId());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userDto.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        Integer userCreationResponse = keycloakService.createUser(userRepresentation);

        if (userCreationResponse.equals(201)) {

            List<UserRepresentation> representations = keycloakService.readUserByEmail(userDto.getEmailId());
            UserProfile userProfile = UserProfile.builder()
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName()).build();

            User user = User.builder()
                    .emailId(userDto.getEmailId())
                    .contactNo(userDto.getContactNumber())
                    .status(Status.PENDING).userProfile(userProfile)
                    .authId(representations.getFirst().getId())
                    .identificationNumber(UUID.randomUUID().toString()).build();

            userRepository.save(user);
            return Response.builder()
                    .responseMessage("User created successfully")
                    .responseCode(responseCodeSuccess).build();
        }
        throw new RuntimeException("User with identification number not found");
    }

    @Override
    public List<UserDto> readAllUsers() {

        List<User> users = userRepository.findAll();

        Map<String, UserRepresentation> userRepresentationMap = keycloakService.readUsers(users.stream().map(user -> user.getAuthId()).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(UserRepresentation::getId, Function.identity()));

        return users.stream().map(user -> {
            UserDto userDto = userMapper.convertToDto(user);
            UserRepresentation userRepresentation = userRepresentationMap.get(user.getAuthId());
            userDto.setUserId(user.getUserId());
            userDto.setEmailId(userRepresentation.getEmail());
            userDto.setIdentificationNumber(user.getIdentificationNumber());
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDto readUser(String authId) {

        User user = userRepository.findUserByAuthId(authId).
                orElseThrow(() -> new ResourceNotFound("User not found on the server"));

        UserRepresentation userRepresentation = keycloakService.readUser(authId);
        UserDto userDto = userMapper.convertToDto(user);
        userDto.setEmailId(userRepresentation.getEmail());
        return userDto;
    }

    @Override
    public Response updateUserStatus(Long id, UserUpdateStatus userUpdate) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("User not found on the server"));

        if (FieldChecker.hasEmptyFields(user)) {
            log.error("User is not updated completely");
            throw new EmptyFields("please updated the user", responseCodeNotFound);
        }

        if(userUpdate.getStatus().equals(Status.APPROVED)){
            UserRepresentation userRepresentation = keycloakService.readUser(user.getAuthId());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);
            keycloakService.updateUser(userRepresentation);
        }

        user.setStatus(userUpdate.getStatus());
        userRepository.save(user);

        return Response.builder()
                .responseMessage("User updated successfully")
                .responseCode(responseCodeSuccess).build();
    }

    @Override
    public UserDto readUserById(Long userId) {

        return userRepository.findById(userId)
                .map(userMapper::convertToDto)
                .orElseThrow(() -> new ResourceNotFound("User not found on the server"));
    }

    @Override
    public Response updateUser(Long id, UserUpdate userUpdate) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found on the server"));

        user.setContactNo(userUpdate.getContactNo());
        BeanUtils.copyProperties(userUpdate, user.getUserProfile());
        userRepository.save(user);

        return Response.builder()
                .responseCode(responseCodeSuccess)
                .responseMessage("user updated successfully").build();
    }

    @Override
    public UserDto readUserByAccountId(String accountId) {

        ResponseEntity<Account> response = accountService.readByAccountNumber(accountId);
        if(Objects.isNull(response.getBody())){
            throw new ResourceNotFound("account not found on the server");
        }
        Long userId = response.getBody().getUserId();
        return userRepository.findById(userId)
                .map(user -> userMapper.convertToDto(user))
                .orElseThrow(() -> new ResourceNotFound("User not found on the server"));
    }

}