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

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "granted_role")
    @NotNull
    private String roles;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="login_id")
    private Set<Person> persons;

    public Login() {
    }

    public Login(Long id, String email, String password, String roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles  = roles;
    }
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Set<Person> getPersons()
    {
        return persons;
    }

    public void setPersons(Set<Person> products)
    {
        this.persons = persons;
    }

    public void setRoles(String roles)
    {
        this.roles = roles;
    }

    public String getRoles()
    {
        return roles;
    }

}
