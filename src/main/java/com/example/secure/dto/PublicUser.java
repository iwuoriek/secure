package com.example.secure.dto;

import com.example.secure.auth.Role;

import java.util.HashSet;
import java.util.Set;

public class PublicUser {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDto> accounts;
    private Set<Role> roles;

    public PublicUser(){
        this.accounts = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountDto account) {
        this.accounts.add(account);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "CustomerShared{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
