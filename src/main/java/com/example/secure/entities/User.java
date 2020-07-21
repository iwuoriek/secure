package com.example.secure.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String address;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Account> accounts;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_id",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id"))
    private Set<Role> roles;

    public User(){
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Role role) {
        this.roles = new HashSet<>();
        roles.add(role);
    }
}
