package com.gringotts.userservice.service;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {

    Integer createUser(UserRepresentation userRepresentation);

    List<UserRepresentation> readUserByEmail(String emailId);

    List<UserRepresentation> readUsers(List<String> authIds);

    UserRepresentation readUser(String authId);

    void updateUser(UserRepresentation userRepresentation);
}
