package com.menu.wantyou.dto;

import com.menu.wantyou.lib.enumeration.Key;
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

    public CheckExistsDTO(Key key, String value) {
        this.key = key;
        this.value = value;
    }
}