package com.artino.service.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginDTO {
    private String account;
    private String password;
}
