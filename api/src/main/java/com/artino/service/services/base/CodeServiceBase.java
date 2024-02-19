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


    @Value("${constant.code.verify}")
    private int verify;
    @Value("${constant.code.expired}")
    private int expired;

    @Autowired
    @Lazy
    private CodeMapper codeMapper;

    /**
     * 检测验证码是否有效
     *
     * @param dto -> account / type / code
     */
    public void verifyCode(TCode dto) {
        TCode code = codeMapper.findNewestOne(
                TCode.builder()
                        .account(dto.getAccount())
                        .type(dto.getType())
                        .build()
        );
        if (Objects.isNull(code))
            throw BusinessException.build(100002, "请先发送验证码");
        long now = DateUtils.timeSpan();
        long expiredAt = DateUtils.timespan(DateUtils.parseDate(code.getExpiredAt()));
        if (code.getVerified() > expired || expiredAt < now)
            throw BusinessException.build(100003, "验证码已失效或已使用，请重新发送");
        if (!code.getCode().equals(dto.getCode())) {
            TCode model = TCode.builder()
                    .id(code.getId())
                    .verified(code.getVerified() + 1)
                    .build();
            codeMapper.update(model);
            throw BusinessException.build(100004, "验证码与手机号不匹配");
        } else {
            TCode model = TCode.builder()
                    .id(code.getId())
                    .verified(verify + 1)
                    .build();
            codeMapper.update(model);
        }
    }

    /**
     * 新增验证码
     * @param entity entity
     * @return yes / no
     */
    public boolean newCode(TCode entity) {
        return codeMapper.insert(entity) > 0;
    }

    /**
     * 查询最新的发送的验证码的数据
     * @param account 账号
     * @param type 类型
     * @return model
     */
    public TCode findCodeBy(String account, TCode.EType type) {
        return codeMapper.findNewestOne(
                TCode.builder()
                        .account(account)
                        .type(type)
                        .build()
        );
    }
}
