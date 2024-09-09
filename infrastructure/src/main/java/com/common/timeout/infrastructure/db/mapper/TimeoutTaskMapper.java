package com.common.timeout.infrastructure.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.timeout.infrastructure.db.model.TimeoutTaskDTO;
import org.apache.ibatis.annotations.Update;


/**
 * TimeoutTaskMapper
 *
 * @author zhanghaojie
 */
public interface TimeoutTaskMapper extends BaseMapper<TimeoutTaskDTO> {

    @Update(" update timeout_task \n" +
            " set state = #{state} , update_time = #{updateTime} \n" +
            " where biz_type = #{bizType} \n" +
            " and biz_id = #{bizId} \n" +
            " and state = #{oldstate} \n" +
            "")
    int updateStateByBizTypeAndBizIdAndOldState(Integer state, String bizType, String bizId, Long updateTime, Integer oldstate);

    @Update(" update timeout_task \n" +
            " set state = #{state} , update_time = #{updateTime} \n" +
            " where biz_type = #{bizType} \n" +
            " and biz_id = #{bizId} \n" +
            "")
    int updateStateByBizTypeAndBizId(Integer state, String bizType, String bizId, Long updateTime);

    @Update(" update timeout_task \n" +
            " set retryCount = retryCount +1 , update_time = #{updateTime} \n" +
            " where biz_type = #{bizType} \n" +
            " and biz_id = #{bizId} \n" +
            "")
    int addTaskRetryCount(String bizType, String bizId, Long updateTime);

}