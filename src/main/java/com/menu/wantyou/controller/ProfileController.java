package com.menu.wantyou.controller;

import com.menu.wantyou.domain.UserDetailsImpl;
import com.menu.wantyou.dto.profile.ProfileReqDTO;
import com.menu.wantyou.dto.profile.ProfileResDTO;
import com.menu.wantyou.service.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ProfileResDTO> getMyProfile(@AuthenticationPrincipal UserDetailsImpl myAuth) {
        return ResponseEntity.ok(new ProfileResDTO(profileService.findByUsername(myAuth.getUsername())));
    }

    @PatchMapping(value = "/me", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ProfileResDTO> updateMyProfile(
            @AuthenticationPrincipal UserDetailsImpl myAuth,
            @Valid @RequestBody ProfileReqDTO updateProfileDTO) {
        return ResponseEntity.ok(new ProfileResDTO(profileService.update(myAuth.getUsername(), updateProfileDTO)));
    }

    @GetMapping(value = "/{username}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ProfileResDTO> getOtherProfile(@PathVariable("username") String username) {
        return ResponseEntity.ok(new ProfileResDTO(profileService.findByUsername(username)));
    }
}
