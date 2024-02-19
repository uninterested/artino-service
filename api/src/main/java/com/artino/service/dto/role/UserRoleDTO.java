package com.artino.service.dto.role;

import com.artino.service.common.EDeleted;
import lombok.*;

@Data
@Builder
public class UserRoleDTO {
    private Long id;
    private String name;
    private String description;
    private Long sort;
    private String createdAt;
    private EDeleted deleted;
    private Long systemRole;
}
