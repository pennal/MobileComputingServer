package org.letsride.server.models;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Account {
    private String username;
    private String password;

    private String role;
    private Boolean isExpired;
    private Boolean isLocked;
    private Boolean credentialsExpired;
    private Boolean isDisabled;

    public Account() {}

    public Account(String username, String password) {
        this.username = username;
        this.password = password;

        this.role = "ROLE_USER";
        this.isExpired = false;
        this.isLocked = false;
        this.credentialsExpired = false;
        this.isDisabled = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(Boolean disabled) {
        isDisabled = disabled;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
