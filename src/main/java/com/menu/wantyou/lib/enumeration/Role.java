package com.menu.wantyou.lib.enumeration;

import com.menu.wantyou.lib.exception.BadConstantException;

import java.util.ArrayList;

public enum Role {
    USER,
    ADMIN;

    private static final Role[] VALUES;

    static {
        VALUES = values();
    }

    public static Role of(String roleName) {
        for (Role role : VALUES) {
            if (roleName.equals(role.name())) {
                return role;
            }
        }
        throw new BadConstantException("No matching constant for [" + roleName + "]");
    }

    public static Role[] of(String[] roleNames) {
        ArrayList<Role> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            for (Role role : VALUES) {
                if (roleName.equals(role.name())) {
                    roles.add(role);
                }
            }
        }
        int size = roles.size();
        return roles.toArray(new Role[size]);
    }
}
