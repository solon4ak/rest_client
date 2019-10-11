package ru.solon4ak.jm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.solon4ak.jm.model.User;

@Service
public class UserService {

    public User[] getAllUsers(String url, RestTemplate restTemplate) {
        return restTemplate.getForObject(url, User[].class);
    }

    public User getUser(String url, RestTemplate restTemplate, Long id) {
        return restTemplate.getForObject(url + "/{id}", User.class, id);
    }

    public User createUser(String url, RestTemplate restTemplate, User user) {
        return restTemplate.postForObject(url, user, User.class);
    }

    public void updateUser(String url, RestTemplate restTemplate, User user) {
        restTemplate.put(url + "/{id}", user, user.getId());
    }

    public void deleteUser(String url, RestTemplate restTemplate, Long id) {
        restTemplate.delete(url + "/{id}", id);
    }
}
