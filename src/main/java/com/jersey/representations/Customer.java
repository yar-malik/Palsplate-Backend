package com.jersey.representations;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Customer() {
    }

    public Customer(Long id, String name, String currency, Double regularPrice, Double discountPrice) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
