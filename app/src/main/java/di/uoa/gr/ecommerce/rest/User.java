package di.uoa.gr.ecommerce.rest;

/**
 * Created by ecommerce on 13/5/2017.
 */

public class User {

    private String username;

//    private Role role;

    private String password;

    private String name;

    private String surname;

    private String email;

    private Long telephone;

    private String address;

    private String location;

    private String country;

    private Long afm;

    private Long rating;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getAfm() {
        return afm;
    }

    public void setAfm(Long afm) {
        this.afm = afm;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

}
