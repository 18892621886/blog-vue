package com.naown.controller.admin;

import com.naown.common.entity.EChartData;
import com.naown.common.entity.Result;
import com.naown.common.entity.echart.PieData;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: chenjian
 * @since: 2021/3/21 0:32 周日
 **/
@RestController
@RequestMapping("/admin")
public class EChartController {

    @GetMapping("eChartData")
    public Result orderData(){
        /** 折线图数据 */
        List<Object> lineChartData = new ArrayList<>();
        List<Object> pieChartData = new ArrayList<>();
        String[] names = {"vue","javascript","mybatis","jpa","springboot","spring","shiro","mysql","mybatisPlus","github"};
        Random random = new Random();
        for (int i = 0; i <= 9; i++){
            PieDataList pieDataList = new PieDataList();
            pieDataList.setValue((random.nextInt(30)+1)*10);
            pieDataList.setName(names[i]);
            EChartData eChartData = new EChartData();
            eChartData.setVue(random.nextFloat()*10000);
            eChartData.setWeChat(random.nextFloat()*10000);
            eChartData.setJavaScript(random.nextFloat()*10000);
            eChartData.setRedis(random.nextFloat()*10000);
            eChartData.setJava(random.nextFloat()*10000);
            eChartData.setMyBatis(random.nextFloat()*10000);
            lineChartData.add(eChartData);
            pieChartData.add(pieDataList);
        }
        /** 饼图数据 */
        PieData pieData = new PieData();
        pieData.setData(pieChartData);
        EChartResult eChartResult = new EChartResult(lineChartData, pieData);
        return Result.succeed(eChartResult);
    }

    @Data
    class EChartResult{
        List<Object> lineChartData;
        PieData pieData;
        public EChartResult(List<Object> lineChartData,PieData pieData){
            this.lineChartData = lineChartData;
            this.pieData = pieData;
        }
    }

    @Data
    class PieDataList{
        private Integer value;
        private String name;
    }
}
