package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("city_menu")
public class HeBeiSmartMenu implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父菜单id")
    private String parentId;

    @ApiModelProperty(value = "icon")
    private String icon;

    @ApiModelProperty(value = "菜单类型 0:菜单,1:按钮")
    private Integer type;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "1:属于普通用户菜单 0;不属于普通用户菜单")
    private Integer common;

    @ApiModelProperty(value = "导航菜单超链接")
    private String href;
    /** 子菜单 */
    @TableField(exist = false)
    private List<HeBeiSmartMenu> children = new ArrayList<HeBeiSmartMenu>();
}
