package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * User entity representing registered users of the parking system.
 * Users can have either USER or ADMIN roles.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    // Manual Builder
    public static UserBuilder builder() { return new UserBuilder(); }
    public static class UserBuilder {
        private String name;
        private String email;
        private String password;
        private Role role;

        public UserBuilder name(String n) { this.name = n; return this; }
        public UserBuilder email(String e) { this.email = e; return this; }
        public UserBuilder password(String p) { this.password = p; return this; }
        public UserBuilder role(Role r) { this.role = r; return this; }
        public User build() {
            User u = new User();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(password);
            u.setRole(role);
            return u;
        }
    }
}
