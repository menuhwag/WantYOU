package com.menu.wantyou.service;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.dto.ProfileDTO;
import com.menu.wantyou.lib.exception.NotFoundException;

public interface ProfileService {
    Profile findByUsername(String username) throws NotFoundException;
    Profile update(String username, ProfileDTO.Update updateProfileDTO) throws NotFoundException;
}
