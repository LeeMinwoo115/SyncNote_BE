package com.syncnote.global.security;

import com.syncnote.global.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class SecurityUser extends User {

    private Long id;
    private String nickname;
    private UserRole role;

    public SecurityUser(
            Long id,
            String nickname,
            String password,
            UserRole role,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(nickname, password, authorities);
        this.id = id;
        this.nickname = nickname;
        this.role = role;
    }
}
