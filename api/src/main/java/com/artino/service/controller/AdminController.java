package com.artino.service.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Api(value = "AdminController", tags = {"管理员用户模块"})
public class AdminController {

}
