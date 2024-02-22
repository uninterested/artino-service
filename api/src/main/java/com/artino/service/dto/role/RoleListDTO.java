package com.artino.service.dto.role;

import com.artino.service.common.PageReq;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class RoleListDTO extends PageReq {
    private boolean showPermission;
    private String keyword;
    private List<String> createdAt;
}
