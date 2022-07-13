package com.menu.wantyou.repository;

import com.menu.wantyou.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String username = "jack01";
    private final String password = "passw0rd";
    private final String email = "jack01@gmail.com";
    private final String nickname = "jackson";

    private User user;

    @BeforeEach
    public void createUser() {
        this.user = new User(username, password, email, nickname);
    }

    @Test
    @DisplayName("유저 db 저장")
    public void saveTest() {
        User saved = userRepository.save(user);

        assertEquals(saved, user);
        assertNotNull(saved.getId());
        assertEquals(saved.isEnabeled(), user.isEnabeled());
        assertEquals(saved.getRole(), user.getRole());
        assertEquals(saved.getUsername(), user.getUsername());
        assertEquals(saved.getPassword(), user.getPassword());
        assertEquals(saved.getEmail(), user.getEmail());
        assertEquals(saved.getNickname(), user.getNickname());
    }
    @Nested
    @DisplayName("조회")
    class FindBy {
        @Nested
        @DisplayName("아이디")
        class Username {
            @Test
            @DisplayName("존재할 시 User 반환")
            void isNotNull() {
                User saved = userRepository.save(user);

                Optional<User> findUser = userRepository.findByUsername(user.getUsername());
                assertNotNull(findUser);
                assertEquals(Optional.ofNullable(saved), findUser);
            }

            @Test
            @DisplayName("존재하지 않을 시 Optional.empty 반환")
            void isNull() {
                Optional<User> findUser = userRepository.findByUsername(user.getUsername());

                assertEquals(Optional.empty(), findUser);
            }
        }

        @Nested
        @DisplayName("이메일")
        class Email {
            @Test
            @DisplayName("존재할 시 User 반환")
            void isNotNull() {
                User saved = userRepository.save(user);

                Optional<User> findUser = userRepository.findByEmail(user.getEmail());
                assertNotNull(findUser);
                assertEquals(Optional.ofNullable(saved), findUser);
            }

            @Test
            @DisplayName("존재하지 않을 시 Optional.empty 반환")
            void isNull() {
                Optional<User> findUser = userRepository.findByEmail(user.getEmail());

                assertEquals(Optional.empty(), findUser);
            }
        }
    }

    @Nested
    @DisplayName("중복체크")
    class CheckExists {
        @Nested
        @DisplayName("아이디")
        class CheckExistsUsername {
            @Test
            @DisplayName("존재하지 않을 시 false 반환")
            public void checkUsernameNotExists() {
                assertFalse(userRepository.existsByUsername(username));
            }

            @Test
            @DisplayName("존재할 시 true 반환")
            public void checkUsernameExists() {
                User saved = userRepository.save(user);
                assertTrue(userRepository.existsByUsername(saved.getUsername()));
            }
        }

        @Nested
        @DisplayName("이메일")
        class CheckExistsEmail {
            @Test
            @DisplayName("존재하지 않을 시 false 반환")
            public void checkEmailNotExists() {
                assertFalse(userRepository.existsByEmail(email));
            }

            @Test
            @DisplayName("존재할 시 true 반환")
            public void checkEmailExists() {
                User saved = userRepository.save(user);
                assertTrue(userRepository.existsByEmail(saved.getEmail()));
            }
        }
    }
}
