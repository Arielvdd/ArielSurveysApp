package com.example.arielsurveysapp.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private UserRole role;
    private String fullName;
    private String phoneNumber;
    private boolean isActive;
    private long lastLogin;
    private List<String> assignedSurveys;

    public enum UserRole {
        TEACHER,
        STUDENT
    }
    public User(String id, String username, String email, String profilePictureUrl,
                UserRole role, String fullName, String phoneNumber, boolean isActive,
                long lastLogin, List<String> assignedSurveys) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.role = role;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.assignedSurveys = assignedSurveys;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<String> getAssignedSurveys() {
        return assignedSurveys;
    }

    public void setAssignedSurveys(List<String> assignedSurveys) {
        this.assignedSurveys = assignedSurveys;
    }

    public void updateLastLogin() {
        this.lastLogin = System.currentTimeMillis();
    }

    public boolean isTeacher() {
        return this.role == UserRole.TEACHER;
    }

    public boolean isStudent() {
        return this.role == UserRole.STUDENT;
    }

    public void deactivateUser() {
        this.isActive = false;
    }

    public void activateUser() {
        this.isActive = true;
    }
}
