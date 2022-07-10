package com.menu.wantyou.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckExistsDTO {
    private Key key;
    private String value;
    private boolean exists;

    public CheckExistsDTO(String key, String value) {
        this.key = Key.valueOf(key.toUpperCase());
        this.value = value;
    }

    public enum Key {
        ID, EMAIL
    }
}