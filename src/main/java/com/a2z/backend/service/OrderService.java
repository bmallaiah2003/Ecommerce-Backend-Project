package com.a2z.backend.service;

import com.a2z.backend.entity.Order;

public interface OrderService {

    Order checkout(String email);
}