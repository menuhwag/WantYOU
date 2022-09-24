package com.menu.wantyou.service;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.dto.profile.ProfileReqDTO;
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
    public Profile update(String username, ProfileReqDTO updateProfileDTO) throws NotFoundException{
        Profile profile = findByUsername(username);

        profile.updateName(updateProfileDTO.getName());
        profile.updateBirthYear(updateProfileDTO.getBirthYear());
        profile.updateBirthDay(updateProfileDTO.getBirthDay());
        profile.updateHobby(updateProfileDTO.getHobby());

        return profile;
    }

    private User findUserByUsername(String username) throws NotFoundException{
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
    }
}
