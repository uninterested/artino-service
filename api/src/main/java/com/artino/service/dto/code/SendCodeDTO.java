package com.artino.service.dto.code;

import com.artino.service.entity.TCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendCodeDTO {
    private String account;
    private TCode.EType type;
}
