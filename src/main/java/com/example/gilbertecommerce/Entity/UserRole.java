package com.example.gilbertecommerce.Entity;

public enum UserRole {

    USER_ROLE00(0,"Admin"),
    USER_ROLE01(1,"Gilbert Team"),
    USER_ROLE02(2,"Standard"),
    USER_ROLE03(3,"Guest"),
    USER_ROLE004(4,"Business");

    private final String VALUE;
    private final int code;
    private UserRole(int code, String role) {
        this.code = code;
        this.VALUE = role;
    }

    public String getRoleName() {
        return VALUE;
    }
    public int getCode() {
        return code;
    }
    public String getRoleByCode(int code) {
        for (UserRole role : UserRole.values()) {
            if (role.getCode() == code) {
                return role.getRoleName();
            }
        }
        return null;
    }

    public static UserRole fromCode(int code) {
        for (UserRole role : UserRole.values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid UserRole code: " + code);
    }

}
