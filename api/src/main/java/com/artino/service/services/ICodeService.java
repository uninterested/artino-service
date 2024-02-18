package com.artino.service.services;

import com.artino.service.dto.code.SendCodeDTO;
import com.artino.service.dto.code.VerifyCodeDTO;

public interface ICodeService {
    /**
     * 发送验证码
     * @param dto dto
     * @return 是否发送成功
     */
    boolean sendCode(SendCodeDTO dto);
}
