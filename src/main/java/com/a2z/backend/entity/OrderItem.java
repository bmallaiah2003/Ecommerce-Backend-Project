package com.a2z.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many items belong to one order
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    // Many order items refer to one product
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // Price at the time of purchase (historical)
    @Column(nullable = false)
    private Double price;

    public OrderItem() {}

    public OrderItem(Product product, Integer quantity, Double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    // getters & setters

    public void setOrder(Order order) {
        this.order = order;
    }
}
