package com.naown.controller.admin;

import com.naown.common.entity.Backlog;
import com.naown.common.entity.Result;
import com.naown.common.service.BacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: chenjian
 * @since: 2021/3/21 22:15 周日
 **/
@RestController
@RequestMapping("/admin")
public class BackController {

    @Autowired
    private BacklogService backlogService;

    /**
     * TODO 待办事件的获取api暂时写在登录controller后续需要再修改
     */
    @GetMapping("listBackLog")
    public Result listBackLogs(){
        List<Backlog> listBacklogs = backlogService.listBacklogs();
        return Result.succeed(listBacklogs);
    }

    /**
     * 对待办事件的持久化
     */
    @PostMapping("editBackLog")
    public Result edit(@RequestBody List<Backlog> backlogs){
        List<Backlog> listBacklogs = backlogService.listBacklogs();

        // 没有选中的
        List<Backlog> noChecked= getAddaListThanbList(listBacklogs,backlogs);
        // TODO 后续可以优化一下此方法
        backlogs.forEach(backlog -> {
            if (!backlog.getChecked()){
                backlog.setChecked(!backlog.getChecked());
                backlogService.updateBackLogById(backlog);
            }
        });
        noChecked.forEach(backlog -> {
            if (backlog.getChecked()){
                backlog.setChecked(!backlog.getChecked());
                backlogService.updateBackLogById(backlog);
            }
        });
        return Result.succeed(backlogs);
    }

    /**
     * @Description: 计算列表aList相对于bList的增加的情况，兼容任何类型元素的列表数据结构
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回增加的元素组成的列表
     */
    public static <E> List<E> getAddaListThanbList(List<E> aList, List<E> bList){
        List<E> addList = new ArrayList<E>();
        for (int i = 0; i < aList.size(); i++){
            if(!myListContains(bList, aList.get(i))){
                addList.add(aList.get(i));
            }
        }
        return addList;
    }

    /**
     * @Description: 计算列表aList相对于bList的减少的情况，兼容任何类型元素的列表数据结构
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回减少的元素组成的列表
     */
    public static <E> List<E> getReduceaListThanbList(List<E> aList, List<E> bList){
        List<E> reduceaList = new ArrayList<E>();
        for (int i = 0; i < bList.size(); i++){
            if(!myListContains(aList, bList.get(i))){
                reduceaList.add(bList.get(i));
            }
        }
        return reduceaList;
    }


    /**
     * @Description: 判断元素element是否是sourceList列表中的一个子元素
     * @param sourceList 源列表
     * @param element 待判断的包含元素
     * @return 包含返回 true，不包含返回 false
     */
    private static <E> boolean myListContains(List<E> sourceList, E element) {
        if (sourceList == null || element == null){
            return false;
        }
        if (sourceList.isEmpty()){
            return false;
        }
        for (E tip : sourceList){
            if(element instanceof Backlog && tip instanceof Backlog){
                if (((Backlog) element).getId().equals(((Backlog) tip).getId())){
                    return true;
                }
            }
        }
        return false;
    }
}
