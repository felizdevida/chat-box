package com.box.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.box.implatform.entity.User;
import com.box.implatform.dto.LoginDTO;
import com.box.implatform.dto.RegisterDTO;
import com.box.implatform.vo.LoginVO;
import com.box.implatform.vo.UserVO;

import java.util.List;


public interface IUserService extends IService<User> {

    LoginVO login(LoginDTO dto);

    LoginVO refreshToken(String refreshToken);

    void register(RegisterDTO dto);

    User findUserByName(String username);

    void update(UserVO vo);

    List<UserVO> findUserByNickName(String nickname);

    List<Long> checkOnline(String userIds);

}
