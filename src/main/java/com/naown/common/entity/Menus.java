package com.naown.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

/**
 * 动态路由菜单类
 * @author: chenjian
 * @since: 2021/3/2 14:17 周二
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@TableName("sys-menu")
public class Menus {
    /** ID */
    @TableField("menu_id")
    private Long id;
    /** 菜单栏标签 */
    private String label;
    /** 菜单栏路径 */
    private String path;
    /** 菜单栏图标 */
    private String icon;
    /** 菜单栏名字 */
    private String name;
    /** 菜单栏可访问的全路径 比如/home/Vue.vue */
    private String url;
    /** 菜单子菜单ID 其实应该是parentId */
    private Long childrenId;
    /** 是否包含二级菜单 */
    @TableField(exist = false)
    private List<Menus> children;
}
