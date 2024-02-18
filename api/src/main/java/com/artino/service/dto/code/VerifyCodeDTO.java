package com.artino.service.dto.code;

import com.artino.service.entity.TCode;
import lombok.*;

@Data
@Builder
public class VerifyCodeDTO  {
    private String account;
    private TCode.EType type;
    private String code;
}
