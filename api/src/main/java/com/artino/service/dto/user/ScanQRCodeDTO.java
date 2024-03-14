package com.artino.service.dto.user;

import com.artino.service.validator.required.Required;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScanQRCodeDTO {
    private Object data;
    private String token;
}
