package com.a2z.backend.service;

import com.a2z.backend.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> getAllProducts();
}
