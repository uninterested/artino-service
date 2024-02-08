package com.artino.service.services.base;


import com.artino.service.base.BusinessException;
import com.artino.service.entity.TAdmin;
import com.artino.service.mapper.AdminMapper;
import com.artino.service.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminServiceBase {
    @Autowired
    @Lazy
    private AdminMapper adminMapper;


    /**
     * 检测帐户是否被注册
     * @param account 帐户
     * @return 帐户是否被注册
     */
    public boolean checkAccountExists(String account) {
        boolean isEmail = RegexUtils.isEmail(account);
        boolean isPhone = RegexUtils.isPhone(account);
        if (!isEmail && !isPhone) throw BusinessException.build(100002, "请输入邮箱或者手机号");
        TAdmin admin = null;
        if (isEmail) admin = adminMapper.findByEmail(account);
        else admin = adminMapper.findByPhone(account);
        return Objects.nonNull(admin);
    }
}
