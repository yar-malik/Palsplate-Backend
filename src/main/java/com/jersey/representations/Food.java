package com.jersey.representations;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "offer_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date offer_start;

    @Column(name = "offer_stop")
    @Temporal(TemporalType.TIMESTAMP)
    private Date offer_stop;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "price")
    @NotNull
    private Double price;

    @Column(name = "portion")
    @NotNull
    private Integer portion;

    @Column(name = "food_type")
    @NotNull
    private String food_type;

    @Column(name = "cuisine_type")
    @NotNull
    private String cuisine_type;

    @Column(name = "lat")
    @NotNull
    private Double lat;

    @Column(name = "lon")
    @NotNull
    private Double lon;

    @Column(name = "is_active")
    @NotNull
    private Boolean is_active;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private Date createdat;

    @Column(name = "updatedat")
    @UpdateTimestamp
    private Date updatedat;

    @JoinColumn(name = "cook_id")
    @NotNull
    private Long cook_id;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="food_id")
    private Set<Image> images;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="food_id")
    private Set<Review> reviews;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id")
    private Set<Reservation> reservations;

    public Food() {
    }

    public Food(Long id, String name, Date offer_start, Date offer_stop,
                String description, Double price, Integer portion,
                String food_type, String cuisine_type, Double lat,
                Double lon, Boolean is_active) {
        this.id = id;
        this.name = name;
        this.offer_start = offer_start;
        this.offer_stop = offer_stop;
        this.description = description;
        this.price = price;
        this.portion = portion;
        this.food_type = food_type;
        this.cuisine_type = cuisine_type;
        this.lat = lat;
        this.lon = lon;
        this.is_active = is_active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOffer_start() {
        return offer_start;
    }

    public void setOffer_start(Date offer_start) {
        this.offer_start = offer_start;
    }

    public Date getOffer_stop() {
        return offer_stop;
    }

    public void setOffer_stop(Date offer_stop) {
        this.offer_stop = offer_stop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPortion() {
        return portion;
    }

    public void setPortion(Integer portion) {
        this.portion = portion;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getCuisine_type() {
        return cuisine_type;
    }

    public void setCuisine_type(String cuisine_type) {
        this.cuisine_type = cuisine_type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Date getCreatedat() {return createdat;}

    public Date getUpdatedat() {return updatedat;}

    public Long getCook_id() {
        return cook_id;
    }

    public void setCook_id(Long cook_id) {
        this.cook_id = cook_id;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
