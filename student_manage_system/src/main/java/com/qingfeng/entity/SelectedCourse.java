package com.qingfeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 选课实体类
 *
 * @author 清风学Java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("s_selected_course")
public class SelectedCourse implements Serializable {

    /**
     * 选课 id 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生 id
     */
    private Integer studentId;

    /**
     * 课程 id
     */
    private Integer courseId;
    /**
     * 任课教师Id
     */
    private Integer teacherId;
}
