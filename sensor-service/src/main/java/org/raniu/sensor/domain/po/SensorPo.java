package org.raniu.sensor.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sensor")
public class SensorPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String owner;

    private Integer type;

    private String unit;

    private String addr;

    private String start;

    private String len;

    private String con;

    private String coff;

    private String func;

    private String formula;
}
