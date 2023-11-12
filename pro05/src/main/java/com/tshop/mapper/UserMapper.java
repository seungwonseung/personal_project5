package com.tshop.mapper;

import com.tshop.entity.Role;
import com.tshop.entity.User;
import com.tshop.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //회원 가입
    void save(User user);

    //회원 전체 조회
    List<User> findAll();

    //회원 고유 번호 찾기
    User findById(Long id);

    //회원 아이디 찾기
    User findByUserId(String userId);

    //회원 이메일 찾기
    User findByEmail(String email);

    //최근 가입 회원 찾기
    User getLatestUser();

    //회원 정보 수정
    void update(User user);

    //회원 권한 설정
    void setUserRole(UserRole userRole);

    //회원 권한 가져오기
    UserRole getUserRole(Long id);
    Role getRole(Integer roleId);
}
