package com.naown.common.entity.echart;

import lombok.Data;

import java.util.List;

/**
 * 图方便直接创建内部类然后进行初始化
 * @author: chenjian
 * @since: 2021/3/21 16:25 周日
 **/
@Data
public class PieData {
    private String type = "pie";
    private Integer[] radius = {10,65};
    private String[] center = {"50%","50%"};
    private String roseType = "area";
    private List<Object> data;
    private ItemStyle itemStyle = new ItemStyle();

    @Data
    class ItemStyle{
        private Integer borderRadius = 3;
    }
}
