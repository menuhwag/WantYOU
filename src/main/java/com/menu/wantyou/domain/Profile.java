package com.menu.wantyou.domain;

import com.menu.wantyou.dto.UserDTO;
import com.menu.wantyou.dto.ProfileDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @Size(min = 4, max = 4)
    @Column(nullable = false)
    private String birthYear;

    @Size(min = 4, max = 4)
    @Column(nullable = false)
    private String birthDay;

    @Column
    private String hobby;

    public Profile(UserDTO.SignUp.CreateProfile createProfileDTO) {
        String birthYear = createProfileDTO.getBirthYear();
        String birthDay = createProfileDTO.getBirthDay();
        validBirth(birthYear, birthDay);
        this.name = createProfileDTO.getName();
        this.birthYear = birthYear;
        this.birthDay = birthDay;
        this.hobby = parseHobby(createProfileDTO.getHobby());
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateBirthYear(String year) {
        validBirth(year, this.birthDay);
        this.birthYear = year;
    }

    public void updateBirthDay(String day) {
        validBirth(this.birthYear, day);
        this.birthDay = day;
    }

    public void updateHobby(List<String> hobby) {
        if (hobby == null || hobby.size() == 0) return;
        String hobbyToStr = parseHobby(hobby);
        this.hobby = hobbyToStr;
    }

    private void validBirth(String birthYear, String birthDay) throws IllegalArgumentException{
        if (!existBirthYear(birthYear)) return;
        if (!existBirthDay(birthDay)) return;
        parseDate(toStringDate(birthYear, birthDay));
    }

    private boolean existBirthYear(String birthYear) {
        return birthYear != null;
    }

    private boolean existBirthDay(String birthDay) {
        return birthDay != null;
    }

    private void parseDate(String date) throws IllegalArgumentException {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("유효하지 않은 생일입니다.");
        }
    }

    private String toStringDate(String birthYear, String birthDay) {
        String month = parseMonth(birthDay);
        String day = parseDay(birthDay);
        return birthYear + "-" + month + "-" + day;
    }

    private String parseMonth(String birthDay) {
        return birthDay.substring(0,2);
    }

    private String parseDay(String birthDay) {
        return birthDay.substring(2);
    }

    private String parseHobby(List<String> hobby) {
        if (hobby == null) return null;
        return String.join(";", hobby);
    }
}
