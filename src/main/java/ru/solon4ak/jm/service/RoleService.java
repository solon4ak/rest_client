package ru.solon4ak.jm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.solon4ak.jm.model.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class RoleService {

    private static final String url = "http://localhost:8080/rest";

    private RestTemplate restTemplate;

    @Autowired
    public RoleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    public Role[] getAllUserRoles() {
//        return restTemplate.getForObject(url + "/roles", Role[].class);
//    }

    public List<Role> getAllUserRoles() {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(url + "/roles", Role[].class)));
    }


}
