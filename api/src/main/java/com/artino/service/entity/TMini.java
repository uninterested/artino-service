package com.artino.service.entity;

import com.artino.service.common.EDeleted;
import com.artino.service.common.EYesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TMini {
    /**
     * 小程序id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属管理员
     */
    private Long adminId;
    /**
     * 简介
     */
    private String describe;
    /**
     * 小程序头像
     */
    private String icon;
    /**
     * 小程序码
     */
    private String miniCode;
    /**
     * 小程序appId
     */
    private String appId;
    /**
     * 小程序appSecret
     */
    private String appSecret;
    /**
     * 原始id
     */
    private String originId;
    /**
     * request合法域名
     */
    private String requestList;
    /**
     * socket合法域名
     */
    private String socketList;
    /**
     * 上传文件合法域名
     */
    private String uploadList;
    /**
     * 下载文件合法域名
     */
    private String downloadList;
    /**
     * 打开地图选择位置
     */
    private EYesNo chooseLocation;
    /**
     * 打开POI列表选择位置
     */
    private EYesNo choosePoi;
    /**
     * 获取当前的地理位置、速度
     */
    private EYesNo getLocation;
    /**
     * 监听实时地理位置变化事件
     */
    private EYesNo onLocationChange;
    /**
     * 开启小程序进入前台时接收位置消息
     */
    private EYesNo startLocationUpdate;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 状态
     */
    private EYesNo status;
    /**
     * 是否删除
     */
    private EDeleted deleted;
}
