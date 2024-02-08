package com.artino.service.dto.code;

import com.artino.service.entity.TCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendCodeDTO {
    private String account;
    private TCode.EType type;
}
