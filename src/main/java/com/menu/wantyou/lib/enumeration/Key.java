package com.menu.wantyou.lib.enumeration;

public enum Key {
    ID("id"),
    EMAIL("email");

    private static final Key[] VALUES;

    static {
        VALUES = values();
    }

    private final String title;

    Key(String title) {
        this.title = title;
    }

    public static Key titleOf(String title) {
        for (Key key : VALUES) {
            if (title.equals(key.title)) {
                return key;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + title + "]");
    }
}
