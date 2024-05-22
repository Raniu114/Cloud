package org.raniu.device.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
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
    public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id")
      private String id;

    private String name;

    private String owner;

    private String protocol;

    private String transferKey;

    private Float collectionTime;


}
