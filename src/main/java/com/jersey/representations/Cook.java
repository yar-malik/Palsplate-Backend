package com.jersey.representations;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Cook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="person_id")
    private Person person;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Set<Food> foods;

    public Cook() {
    }

    public Cook(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Person getPerson() {
        return person;
    }

    public void setUser_id(Person person) {
        this.person = person;
    }
}
