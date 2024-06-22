package org.raniu.configuration.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("configuration")
public class ConfigurationPo implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String name;

    private String projectId;

    private String page;


}
