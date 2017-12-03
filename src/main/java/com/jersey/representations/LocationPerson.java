package com.jersey.representations;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "locationPerson")
public class LocationPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "street")
    private String street;

    @Column(name = "sublocality")
    private String sublocality;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postal_code;

    @JoinColumn(name = "person_id")
    @NotNull
    private Long person_id;

    public LocationPerson() {
    }

    public LocationPerson(Long id, String address, Double lat, Double lon, String street, String city, String country, String postal_code) {
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postal_code = postal_code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSublocality() { return sublocality; }

    public void setSublocality(String sublocality) { this.sublocality = sublocality; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }

    @Override
    public String toString() {
        return "LocationPerson{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postal_code='" + postal_code + '\'' +
                '}';
    }
}
