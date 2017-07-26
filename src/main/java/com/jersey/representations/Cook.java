package com.jersey.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonIgnoreProperties
@Entity(name = "cook")
public class Cook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "person_id")
    @NotNull
    private Long person_id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "cook_id")
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

    public Set<Food> getFoods() {
        return foods;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }

//    public Set<Person> getPersons() {
//        return persons;
//    }
//
//    public void setPersons(Set<Person> persons) {
//        this.persons = persons;
//    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }
}
