package com.naown.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 待办事件
 * @author chenjian
 * @since 2021-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_backlog")
public class Backlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待办事件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 待办事件说明
     */
    private String label;

    /**
     * 状态:是否勾选
     */
    private Boolean checked;


}
