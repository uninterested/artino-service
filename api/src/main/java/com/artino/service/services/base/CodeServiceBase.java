package com.artino.service.services.base;

import com.artino.service.base.BusinessException;
import com.artino.service.entity.TCode;
import com.artino.service.mapper.CodeMapper;
import com.artino.service.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CodeServiceBase {


    @Value("${config.code.verify}")
    private int verify;
    @Value("${config.code.expired}")
    private int expired;

    @Autowired
    @Lazy
    private CodeMapper codeMapper;

    /**
     * 检测验证码是否有效
     *
     * @param dto
     * @return
     */
    public boolean verifyCode(TCode dto) {
        TCode code = codeMapper.findNewestOne(
                TCode.builder().code(dto.getCode()).build()
        );
        if (Objects.isNull(code))
            throw BusinessException.build(100002, "请先发送验证码");
        long now = DateUtils.timeSpan();
        long expiredAt = DateUtils.timespan(DateUtils.parseDate(code.getExpiredAt()));
        if (code.getVerified() > expired || expiredAt > now)
            throw BusinessException.build(100003, "验证码已失效或已使用，请重新发送");
        if (!code.getCode().equals(dto.getCode())) {
            codeMapper.update(
                    TCode.builder()
                            .id(code.getId())
                            .verified(code.getVerified() + 1)
                            .build()
            );
            return false;
        } else {
            codeMapper.update(
                    TCode.builder()
                            .id(code.getId())
                            .verified(verify + 1)
                            .build()
            );
            return true;
        }
    }
}
