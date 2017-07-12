package com.jersey.representations;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties
@Entity(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="fileName")
    private String filename;

    @Column(name="fileLocation")
    private String fileLocation;

    @Column(name="data")
    private byte[] data;

    @JoinColumn(name = "food_id")
    @NotNull
    private Long food_id;

    public Image() {}

    public Image(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileLocation() { return fileLocation; }

    public void setFileLocation(String fileLocation) { this.fileLocation = fileLocation; }

    public Long getFood_id() {
        return food_id;
    }

    public void setFood_id(Long food_id) {
        this.food_id = food_id;
    }



}
