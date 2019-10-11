package ru.solon4ak.jm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.solon4ak.jm.model.Role;

@Service
public class RoleService {

    public Role[] getAllUserRoles(String url, RestTemplate restTemplate) {
        url = url + "/roles";
        return restTemplate.getForObject(url, Role[].class);
    }


}
