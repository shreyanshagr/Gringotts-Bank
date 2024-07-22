package com.gringotts.userservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyCloakProperties {

    @Value("${app.config.keycloak.server-url}")
    private String serverUrl;

    /**
     * -- GETTER --
     *  Returns the realm.
     *
     */
    @Getter
    @Value("${app.config.keycloak.realm}")
    private String realm;

    @Value("${app.config.keycloak.client-id}")
    private String clientId;

    @Value("${app.config.keycloak.client-secret}")
    private String clientSecret;

    private static Keycloak keycloakInstance = null;

    /**
     * Returns an instance of Keycloak.
     * If the instance is null, it creates a new instance using the provided configuration.
     *
     * @return The Keycloak instance
     */
    public Keycloak getKeycloakInstance() {

        if (keycloakInstance == null) {
            keycloakInstance = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType("client_credentials")
                    .build();
        }

        return keycloakInstance;
    }

}