package org.raniu.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.json.JSONObject;

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
@TableName("user")
public class UserPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Integer permissions;

    private String auth;

    private String email;

    private String phone;

    private Long creatUser;

    private Long creatTime;

    private String addr;
}
