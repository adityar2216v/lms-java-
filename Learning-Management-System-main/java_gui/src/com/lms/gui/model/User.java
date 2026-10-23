package com.lms.gui.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Role {
        ADMIN, FACULTY, STUDENT;

        public static Role fromString(String str) {
            if (str == null) return STUDENT;
            for (Role r : Role.values()) {
                if (r.name().equalsIgnoreCase(str.trim())) return r;
            }
            return STUDENT;
        }
    }

    private int id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private Role role;
    private int status; // 1 = Active, 0 = Inactive / Pending
    private String department;

    public User() {}

    public User(int id, String name, String email, String mobile, String password, Role role, int status, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.role = role;
        this.status = status;
        this.department = department;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public boolean isActive() { return status == 1; }

    @Override
    public String toString() {
        return name + " (" + email + ") - " + role;
    }
}
