package com.jersey.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
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

    @JoinColumn(name = "createdat")
    @CreationTimestamp
    private Date createdat;

    @JoinColumn(name = "updatedat")
    @UpdateTimestamp
    private Date updatedat;

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

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }

    public Date getCreatedat() { return createdat;}

    public void setCreatedat(Date createdat) { this.createdat = createdat; }

    public Date getUpdatedat() {return updatedat; }

    public void setUpdatedat(Date updatedat) {this.updatedat = updatedat;}
}
