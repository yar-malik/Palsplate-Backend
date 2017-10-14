package com.jersey.representations;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties
@Entity(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="text")
    private String text;

    @Column(name="rating")
    private Integer rating;

    @JoinColumn(name = "food_id")
    @NotNull
    private Long food_id;

    @JoinColumn(name = "customer_id")
    @NotNull
    private Long customer_id;

    @JoinColumn(name = "created_timestamp")
    @CreationTimestamp
    private Date created_timestamp;

    @JoinColumn(name = "last_modified_timestamp")
    @UpdateTimestamp
    private Date last_modified_timestamp;

    public Review() {}

    public Review(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("rating can only be a number between 1 and 5 ");
        }

        this.rating = rating;
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Date getCreateTimestamp(){return this.created_timestamp;}

    public Date getLastModifiedTimestamp(){return this.last_modified_timestamp;}
}
