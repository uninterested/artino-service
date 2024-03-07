package com.artino.service.services;

import com.artino.service.dto.admin.AdminCodeLoginDTO;
import com.artino.service.dto.admin.AdminCreateDTO;
import com.artino.service.dto.admin.AdminLoginDTO;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import com.artino.service.vo.admin.res.AdminMenuListResVO;

import java.util.List;

public interface IAdminService {
    /**
     * 创建管理员用户
     * @param dto dto
     * @return 是否创建成功
     */
    boolean adminCreate(AdminCreateDTO dto);

    /**
     * 管理员登录
     * @param dto dto
     * @return 管理员信息
     */
    AdminLoginResVO adminLogin(AdminLoginDTO dto);

    /**
     * 同步用户信息
     * @return 管理员信息
     */
    AdminLoginResVO sync();

    /**
     * 验证码登录
     * @param dto dto
     * @return 管理员信息
     */
    AdminLoginResVO codeLogin(AdminCodeLoginDTO dto);

    /**
     * 用户退出登录
     * @return 是否退出成功
     */
    boolean out();
}
