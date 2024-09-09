package com.common.timeout.infrastructure;

import com.common.timeout.infrastructure.annotation.ToLog;
import com.common.timeout.infrastructure.db.model.TaskTypeMangerDTO;

import java.util.List;

/**
 * TaskTypeMangerService
 * 功能描述：任务类型管理服务
 *
 * @author zhanghaojie
 * @date 2022/3/17 15:32
 */
public interface TaskTypeMangerService {

    /**
     * 功能描述: 获取所有的任务类型对象
     *
     * @return List<TaskTypeMangerDTO>
     * @author zhanghaojie
     * @date 2022/3/17 15:32
     */
    List<TaskTypeMangerDTO> getAllTaskTypeMangerDTO();

    /**
     * 功能描述: 获取所有的任务类型
     *
     * @return List<TaskTypeMangerDTO>
     * @author zhanghaojie
     * @date 2022/3/17 15:32
     */
    List<String> getAllTaskType();

    /**
     * 功能描述: 根据BizType获取任务类型
     *
     * @return List<TaskTypeMangerDTO>
     * @author zhanghaojie
     * @date 2022/3/17 15:32
     */
    TaskTypeMangerDTO getTaskTypeByBizType(String bizType);

}
