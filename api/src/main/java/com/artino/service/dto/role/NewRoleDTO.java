package com.artino.service.dto.role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewRoleDTO {
    private String name;
    private String description;
}
