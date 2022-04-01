package com.common.timeout.client.db.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.common.timeout.client.db.model.TimeoutTaskDTO;
import org.apache.ibatis.annotations.Update;


/**
 * TimeoutTaskMapper
 *
 * @author zhanghaojie
 */
public interface TimeoutTaskMapper extends BaseMapper<TimeoutTaskDTO> {

    @Update(" update timeout_task \n" +
            " set status = #{status} , update_time = #{updateTime}\n" +
            " where biz_type = #{bizType}\n" +
            " and biz_id = #{bizId}\n")
    int updateStatusByBizTypeAndBizId(Integer status, String bizType, String bizId, Long updateTime);

}