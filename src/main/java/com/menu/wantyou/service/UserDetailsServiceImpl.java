package com.menu.wantyou.service;

import com.menu.wantyou.domain.User;
import com.menu.wantyou.lib.exception.NotFoundException;
import com.menu.wantyou.repository.UserRepository;
import com.menu.wantyou.domain.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Not Found the User"));
        return new UserDetailsImpl(user);
    }
}
