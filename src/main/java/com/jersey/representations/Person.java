package com.jersey.representations;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "granted_role")
    @NotNull
    private String granted_role;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;

    @Column(name = "address")
    @NotNull
    private String address;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "is_photo_public")
    @NotNull
    private boolean isPhotoPublic;

    @Column(name = "photo_name")
    public String photoName;

    @Column(name = "photo_public_id")
    public String photoPublicId;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private Date createdat;

    @Column(name = "updatedat")
    @UpdateTimestamp
    private Date updatedat;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="person_id")
    private Set<Cook> cook;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="person_id")
    private Set<Customer> customer;

    public Person() {
    }

    public Person(Long id, String email, String firstName, String lastName,
                String phoneNumber, String address, String description,
                boolean isPhotoPublic, String photoName, String photoPublicId) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
        this.isPhotoPublic = isPhotoPublic;
        this.photoName = photoName;
        this.photoPublicId = photoPublicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsPhotoPublic() {
        return isPhotoPublic;
    }

    public void setIsPhotoPublic(boolean isPhotoPublic) {
        this.isPhotoPublic = isPhotoPublic;
    }

    public boolean isPhotoPublic() {
        return isPhotoPublic;
    }

    public void setPhotoPublic(boolean photoPublic) {
        isPhotoPublic = photoPublic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return granted_role;
    }

    public void setRoles(String granted_role) {
        this.granted_role = granted_role;
    }

    public Set<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(Set<Customer> customer) {
        this.customer = customer;
    }

    public Set<Cook> getCook() {
        return cook;
    }

    public void setCook(Set<Cook> cook) {
        this.cook = cook;
    }

    public String getPhotoPublicId() {
        return photoPublicId;
    }

    public void setPhotoPublicId(String photoPublicId) {
        this.photoPublicId = photoPublicId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public Date getCreatedat() { return createdat; }

    public void setCreatedat(Date createdat) { this.createdat = createdat; }

    public Date getUpdatedat() { return updatedat; }

    public void setUpdatedat(Date updatedat) { this.updatedat = updatedat; }
}
