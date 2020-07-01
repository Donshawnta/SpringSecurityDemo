package com.example.demo.service;

import com.example.demo.bo.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("demoClientDetailsService")
public class AppClientDetailsService implements ClientDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        UserBO userBO = userService.get(clientId);
        return new ClientDetails() {
            @Override
            public String getClientId() {
                return userBO.getUsername();
            }

            @Override
            public Set<String> getResourceIds() {
                return Collections.emptySet();
            }

            @Override
            public boolean isSecretRequired() {
                return true;
            }

            @Override
            public String getClientSecret() {
                return userBO.getPassword();
            }

            @Override
            public boolean isScoped() {
                return true;
            }

            @Override
            public Set<String> getScope() {
                return Arrays.asList("read", "write").stream().collect(Collectors.toSet());
            }

            @Override
            public Set<String> getAuthorizedGrantTypes() {
                return new HashSet<>(Arrays.asList("client_credentials", "password"));
            }

            @Override
            public Set<String> getRegisteredRedirectUri() {
                return Collections.emptySet();
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return userBO.getRoles().stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s.toString())).collect(Collectors.toList());
            }

            @Override
            public Integer getAccessTokenValiditySeconds() {
                return 100;
            }

            @Override
            public Integer getRefreshTokenValiditySeconds() {
                return 100;
            }

            @Override
            public boolean isAutoApprove(String s) {
                return false;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return Collections.emptyMap();
            }
        };
    }
}
