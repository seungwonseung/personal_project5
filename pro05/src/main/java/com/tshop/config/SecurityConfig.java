package com.tshop.config;

import com.tshop.service.UserService;
import com.tshop.service.UserServiceImpl;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/", "/**", "/ex").permitAll()
                .antMatchers("/member/login", "/member/join", "/member/idCheck", "/member/emailCheck", "/member/joinPro").permitAll()
                //.mvcMatchers("/","/templates/**","/ex/**","/resource/**","/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/teacher/**").hasAnyRole("ADMIN","TEACHER")
                .antMatchers("/member/update", "/member/out", "/member/updatePro").hasAnyRole("USER", "TEACHER", "ADMIN")
                .anyRequest().authenticated();

        //login설정
        http
                .formLogin()
                .loginPage("/member/login") //GET - loginform 출력
                .loginProcessingUrl("/member/auth") // POST - 로그인 폼에서의 입력 정보 처리
                .usernameParameter("userId") // login에 필요한 id 값을 email로 설정
                .passwordParameter("password") // login에 필요한 password 값을 password로 설정
                .defaultSuccessUrl("/"); //로그인에 성공하면 인덱스 페이지로

        http
                .logout()
                .logoutUrl("/member/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/"); //로그아웃 성공 시 인덱스 페이지로

        //cors 방지 해제
        http.csrf().disable().cors().disable();
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        return source;
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}
