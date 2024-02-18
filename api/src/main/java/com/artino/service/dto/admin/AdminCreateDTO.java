package com.artino.service.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCreateDTO {
    private String account;
    private String password;
    private String code;
    private String nickName;
}
