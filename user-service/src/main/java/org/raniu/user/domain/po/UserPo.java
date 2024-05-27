package org.raniu.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Date;

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

    private String creatUser;

    private Long creatTime;

    private String addr;
}
