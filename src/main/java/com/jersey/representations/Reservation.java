package com.jersey.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties
@Entity(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active")
    @NotNull
    private Boolean is_active;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private Date createdat;

    @Column(name = "updatedat")
    @UpdateTimestamp
    private Date updatedat;

    @JoinColumn(name = "food_id")
    @NotNull
    private Long food_id;

    @JoinColumn(name = "customer_id")
    @NotNull
    private Long customer_id;

    public Reservation() {
    }

    public Reservation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodId() {
        return food_id;
    }

    public void setFoodId(Long foodId) {
        this.food_id = foodId;
    }

    public Long getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(Long customerId) {
        this.customer_id = customerId;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Date getCreatedat() { return createdat; }

    public void setCreatedat(Date createdat) { this.createdat = createdat;}

    public Date getUpdatedat() { return updatedat; }

    public void setUpdatedat(Date updatedat) { this.updatedat = updatedat; }
}
