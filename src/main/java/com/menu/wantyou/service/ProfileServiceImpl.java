package com.menu.wantyou.service;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.ProfileDTO;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.repository.ProfileRepository;
import com.menu.wantyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    public Profile findByUsername(String username) throws NotFoundException {
        return findUserByUsername(username).getProfile();
    }

    @Override
    @Transactional
    public Profile update(String username, ProfileDTO.Update updateProfileDTO) throws NotFoundException{
        Profile profile = findByUsername(username);
        if (updateProfileDTO.getName() != null) profile.updateName(updateProfileDTO.getName());
        if (updateProfileDTO.getBirthYear() != null) profile.updateBirthYear(updateProfileDTO.getBirthYear());
        if (updateProfileDTO.getBirthDay() != null) profile.updateBirthDay(updateProfileDTO.getBirthDay());
        if (updateProfileDTO.getHobby() != null) profile.updateHobby(updateProfileDTO.getHobby());
        return profile;
    }

    private User findUserByUsername(String username) throws NotFoundException{
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
    }
}
