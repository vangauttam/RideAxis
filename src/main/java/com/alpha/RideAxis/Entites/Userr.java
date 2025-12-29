package com.alpha.RideAxis.Entites;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")   // ✅ NEVER use user / users
public class Userr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private long mobileno;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    public Userr() {}

    public Long getId() {
        return id;
    }

    public long getMobileno() {
        return mobileno;
    }

    public void setMobileno(long mobileno) {
        this.mobileno = mobileno;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
