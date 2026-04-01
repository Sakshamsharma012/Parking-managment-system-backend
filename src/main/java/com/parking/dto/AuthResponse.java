package com.parking.dto;

import lombok.*;

/**
 * DTO for authentication responses containing JWT token and user info.
 */
public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String role;

    public AuthResponse() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Manual Builder
    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    public static class AuthResponseBuilder {
        private String token;
        private String email;
        private String name;
        private String role;

        public AuthResponseBuilder token(String t) { this.token = t; return this; }
        public AuthResponseBuilder email(String e) { this.email = e; return this; }
        public AuthResponseBuilder name(String n) { this.name = n; return this; }
        public AuthResponseBuilder role(String r) { this.role = r; return this; }
        public AuthResponse build() {
            AuthResponse ar = new AuthResponse();
            ar.setToken(token);
            ar.setEmail(email);
            ar.setName(name);
            ar.setRole(role);
            return ar;
        }
    }
}
