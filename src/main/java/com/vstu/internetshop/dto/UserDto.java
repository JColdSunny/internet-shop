package com.vstu.internetshop.dto;

public class UserDto {
    private final Integer id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String active;
    private final String roles;

    public UserDto(Integer id, String username, String firstname, String lastname, String active, String roles) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.active = active;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getActive() {
        return active;
    }

    public String getRoles() {
        return roles;
    }
}
