package com.artino.service.services;

import com.artino.service.dto.user.NewUserDTO;
import com.artino.service.dto.user.UserCodeLoginDTO;
import com.artino.service.dto.user.UserLoginDTO;
import com.artino.service.vo.user.res.UserLoginResVO;

public interface IUserService {
    /**
     * 创建用户
     * @param dto
     * @return
     */
    boolean newUser(NewUserDTO dto);

    /**
     * 用户登录
     * @param dto
     * @return
     */
    UserLoginResVO userLogin(UserLoginDTO dto);

    /**
     * 同步信息
     * @return
     */
    UserLoginResVO sync();

    /**
     * 验证码登录
     * @param dto
     * @return
     */
    UserLoginResVO codeLogin(UserCodeLoginDTO dto);

    /**
     * 用户退出登录
     * @return
     */
    boolean out();
}
