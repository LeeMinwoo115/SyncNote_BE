package com.syncnote.global.enums;

public enum UserRole {
    NORMAL,
    ADMIN;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
