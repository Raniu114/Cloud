package org.raniu.project.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.project.domain.po
 * @className: ProjectTypePo
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/17 16:53
 * @version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project_type")
public class ProjectTypePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    private String typeName;

}
