package org.raniu.device.config.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.*;


/**
 * @projectName: IOTCloud
 * @package: org.raniu.device.config.sharding
 * @className: TimeShardingAlgorithm
 * @author: Raniu
 * @description: Sharding数据库分表分库配置
 * @date: 2024/7/15 18:05
 * @version: 1.0
 */
public class TimeShardingAlgorithm implements StandardShardingAlgorithm<Date> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        // TODO 分表逻辑
        return "";
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        return List.of();
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return "";
    }
}
