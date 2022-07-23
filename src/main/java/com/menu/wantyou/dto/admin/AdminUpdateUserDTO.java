package com.menu.wantyou.dto.admin;

import com.menu.wantyou.lib.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserDTO {
    @NotBlank
    @Size(min = 6, max = 16)
    private String username;

    private Boolean enabled;

    private Role role;
}
