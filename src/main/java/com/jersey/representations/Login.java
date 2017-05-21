package com.jersey.representations;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="login_id")
    private Set<Person> persons;

    public Login() {
    }

    public Login(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> products) {
        this.persons = persons;
    }

}
