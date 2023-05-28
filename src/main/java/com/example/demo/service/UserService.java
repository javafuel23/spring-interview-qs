package com.example.demo.service;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Todo> getUserTodos(Long userId) {
        User user = null;

        try {
            user = restTemplate.getForObject("https://gorest.co.in/public/v2/users/" + userId, User.class);
            if (user != null) {
                List<Todo> todos = restTemplate.exchange(
                        "https://gorest.co.in/public/v2/todos?user_id={userId}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Todo>>() {
                        },
                        userId
                ).getBody();

                if (todos != null) {
                    user.setTodos(todos);
                }
            }
        } catch (Exception e) {
            System.out.println("exception" + e);
        }
        return user != null ? user.getTodos() : Collections.emptyList();
    }
}
