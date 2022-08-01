package com.menu.wantyou.controller;

import com.menu.wantyou.domain.UserDetailsImpl;
import com.menu.wantyou.dto.ProfileResponseDTO;
import com.menu.wantyou.dto.UpdateProfileDTO;
import com.menu.wantyou.service.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/profiles")
public class ProfileController {
    private final ProfileServiceImpl profileService;

    @GetMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ProfileResponseDTO> getMyProfile(@AuthenticationPrincipal UserDetailsImpl myAuth) {
        return new ResponseEntity<>(new ProfileResponseDTO(profileService.findByUsername(myAuth.getUsername())), HttpStatus.OK);
    }

    @PatchMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ProfileResponseDTO> updateMyProfile(@AuthenticationPrincipal UserDetailsImpl myAuth, @Valid @RequestBody UpdateProfileDTO updateProfileDTO) {
        return new ResponseEntity<>(new ProfileResponseDTO(profileService.update(myAuth.getUsername(), updateProfileDTO)), HttpStatus.OK);
    }

    @GetMapping(value = "/{username}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ProfileResponseDTO> getOtherProfile(@PathVariable("username") String username) {
        return new ResponseEntity<>(new ProfileResponseDTO(profileService.findByUsername(username)), HttpStatus.OK);
    }
}
