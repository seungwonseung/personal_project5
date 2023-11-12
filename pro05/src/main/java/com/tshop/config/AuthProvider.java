package com.tshop.config;

import com.tshop.entity.User;
import com.tshop.entity.UserRole;
import com.tshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//인증 관리자
@Component
public class AuthProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = (String) authentication.getPrincipal(); // 로그인 창에 입력한 회원 아이디
        String password = (String) authentication.getCredentials(); // 로그인 창에 입력한 password

        PasswordEncoder passwordEncoder = userService.passwordEncoder();
        UsernamePasswordAuthenticationToken token;
        User user = userService.findByUserId(userId);
        UserRole userRole = userService.getUserRole(user.getId());

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            List<GrantedAuthority> roles = new ArrayList<>();
            if(userRole.getRoleId()==1){
                roles.add(new SimpleGrantedAuthority("ADMIN")); //관리자 권한 부여
            } else if (userRole.getRoleId()==2) {
                roles.add(new SimpleGrantedAuthority("TEACHER")); //선생님 권한 부여
            } else {
                roles.add(new SimpleGrantedAuthority("USER")); //일반 회원 권한 부여
            }

            // userId - 로그인 시 사용할 아이디를 principal로 사용할 때
            token = new UsernamePasswordAuthenticationToken(user.getUserId(), null, roles);

            // id - 개인 토큰으로 principal로 사용할 때
            // token = new UsernamePasswordAuthenticationToken(user.getId(), null, rolse);
            return token;
        }
        throw new BadCredentialsException("No such user or wrong password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
