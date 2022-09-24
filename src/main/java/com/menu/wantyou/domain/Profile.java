package com.menu.wantyou.domain;

import com.menu.wantyou.lib.util.DateValidator;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
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

    public void updateName(String name) {
        if (name == null) return;
        this.name = name;
    }

    public void updateBirthYear(String year) {
        if (year == null) return;
        DateValidator.valid(year, this.birthDay);
        this.birthYear = year;
    }

    public void updateBirthDay(String day) {
        if (day == null) return;
        DateValidator.valid(this.birthYear, day);
        this.birthDay = day;
    }

    public void updateHobby(List<String> hobby) {
        if (hobby == null || hobby.size() == 0) return;
        this.hobby = parseHobby(hobby);
    }

    private String parseHobby(List<String> hobby) {
        if (hobby == null) return null;
        return String.join(";", hobby);
    }
}
