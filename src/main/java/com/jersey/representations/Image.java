package com.jersey.representations;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties
@Entity(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fileName")
    private String filename;

    @Column(name="cloudinary_public_id")
    private String cloudinary_public_id;

    @JoinColumn(name = "food_id")
    @NotNull
    private Long food_id;

    @JoinColumn(name = "created_timestamp")
    @CreationTimestamp
    private Date created_timestamp;

    @JoinColumn(name = "last_modified_timestamp")
    @UpdateTimestamp
    private Date last_modified_timestamp;

    public Image() {}

    public Image(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateTimestamp(){return this.created_timestamp;}

    public Date getLastModifiedTimestamp(){return this.last_modified_timestamp;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }

    public String getCloudinary_public_id() {
        return cloudinary_public_id;
    }

    public void setCloudinary_public_id(String cloudinary_public_id) {
        this.cloudinary_public_id = cloudinary_public_id;
    }
}
