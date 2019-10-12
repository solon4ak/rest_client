package ru.solon4ak.jm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.solon4ak.jm.model.User;

@Service
public class UserService {

    private static final String url = "http://localhost:8080/rest";
    private RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User[] getAllUsers() {
        return restTemplate.getForObject(url, User[].class);
    }

    public User getUser(Long id) {
        return restTemplate.getForObject(url + "/{id}", User.class, id);
    }

    public User createUser(User user) {
        return restTemplate.postForObject(url, user, User.class);
    }

    public void updateUser(User user) {
        restTemplate.put(url + "/{id}", user, user.getId());
    }

    public void deleteUser(Long id) {
        restTemplate.delete(url + "/{id}", id);
    }
}
