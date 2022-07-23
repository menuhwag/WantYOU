package com.menu.wantyou.domain;

import com.menu.wantyou.dto.UpdateProfileDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Size(min = 4, max = 4)
    @Column(nullable = false)
    private String birthYear;

    @Size(min = 4, max = 4)
    @Column(nullable = false)
    private String birthDay;

    @Column
    private String hobby = "";

    public Profile(User user, String name, String year, String day) {
        // 날짜 유효성 검사
        validBirth(year, day);
        this.user = user;
        this.name = name;
        this.birthYear = year;
        this.birthDay = day;
    }

    public Profile update(UpdateProfileDTO updateProfileDTO) {
        String name = updateProfileDTO.getName();
        String year = updateProfileDTO.getYear();
        String day = updateProfileDTO.getDay();
        String hobby = updateProfileDTO.getHobby();

        // 날짜 유효성 검사
        if (year != null && day != null) validBirth(year, day);

        if (name != null) setName(name);
        if (year != null) setBirthYear(year);
        if (day != null) setBirthDay(day);
        if (hobby != null) setHobby(hobby);

        return this;
    }

    private void validBirth(String birthYear, String birthDay) throws IllegalArgumentException{
        try {
            String year = birthYear;
            String month = birthDay.substring(0,2);
            String day = birthDay.substring(2);
            String date = year + "-" + month + "-" + day;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("유효하지 않은 생일입니다.");
        }
    }
}
