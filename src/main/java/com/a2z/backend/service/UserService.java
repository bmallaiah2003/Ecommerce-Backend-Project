package com.a2z.backend.service;

import com.a2z.backend.entity.User;

public interface UserService {

    User register(User user);

    User login(String email, String password);
}
