package com.cf.crs.entity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
@Data
public class TreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Integer code;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;



    public TreeSelect(CityOrganization dept)
    {
        this.code = dept.getCode();
        this.label = dept.getOrganization();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }




}
