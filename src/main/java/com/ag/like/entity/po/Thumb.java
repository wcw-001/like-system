package com.ag.like.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName thumb
 */
@TableName(value ="thumb")
@Data
public class Thumb implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    @TableField(value = "userId")
    private Long userid;

    /**
     *
     */
    @TableField(value = "blogId")
    private Long blogid;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
