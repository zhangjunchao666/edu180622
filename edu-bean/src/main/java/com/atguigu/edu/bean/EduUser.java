package com.atguigu.edu.bean;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class EduUser implements Serializable {
    @Id
    private Integer userId;
    //@Column
    private String mobile;

    private String email;

    private String password;

    private String userName;

    private String showName;

    private Boolean sex;

    private Byte age;
   // @JSONField(format = "yyyy-MM-dd HH:mm:ss")  //FastJson包使用注解
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //Jackson包使用注解
   // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")   //格式化前台日期参数注解
    private Date createTime;

    private Boolean isAvalible;

    private String picImg;

    private String bannerUrl;

    private Integer msgNum;

    private Integer sysMsgNum;
   // @JSONField(format = "yyyy-MM-dd HH:mm:ss")  //FastJson包使用注解
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //Jackson包使用注解
   // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")   //格式化前台日期参数注解
    private Date lastSystemTime;


}