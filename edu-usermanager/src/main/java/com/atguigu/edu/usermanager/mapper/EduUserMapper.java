package com.atguigu.edu.usermanager.mapper;

import com.atguigu.edu.bean.EduUser;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

import javax.persistence.ColumnResult;
import java.util.List;

/**                                  @Result 详 解
 *  因为数据库字段是下划线分隔,bean中的字段是驼峰命名的,如user_name和userName,导致无法匹配
 *  @Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类属性和数据库属性名保持一致，就不需要这个属性来修饰
 *  如果是通过xml文件来配置的话，只需要开启驼峰命名转换
 *  <setting name="mapUnderscoreToCamelCase" value="true"/>
 *  MyBatis支持使用注解来配置映射语句，不再需要在XML配置文件中配置
 */
public interface EduUserMapper extends Mapper<EduUser> {

    @Select("<script> " +
             " SELECT * FROM edu_user WHERE CONCAT(IFNULL(USER_NAME,\"\"),IFNULL(SHOW_NAME,\"\"),IFNULL(EMAIL,\"\"),IFNULL(MOBILE,\"\")) LIKE '%' #{keyword} '%'" +
             "<if test='startTime!=null and startTime!=\"\"'> and  create_time &gt;= str_to_date(#{startTime},'%Y-%m-%d %H:%i:%s')</if>" +
             "<if test='endTime!=null and endTime!=\"\"'> and  create_time &lt;= str_to_date(#{endTime},'%Y-%m-%d %H:%i:%s')</if>" +
             "</script>")
     @Results({
             @Result(column = "user_id",property = "userId"),
             @Result(column = "mobile",property = "mobile"),
             @Result(column = "email",property = "email"),
             @Result(column = "password",property = "password"),
             @Result(column = "user_Name",property ="userName" ),
             @Result(column = "show_Name",property = "showName"),
             @Result(column = "sex",property = "sex"),
             @Result(column = "age",property = "age"),
             @Result(column = "create_Time",property = "createTime"),
             @Result(column = "is_Avalible",property = "isAvalible"),
             @Result(column = "pic_Img",property = "picImg"),
             @Result(column = "banner_Url",property = "bannerUrl"),
             @Result(column = "msg_Num",property = "msgNum"),
             @Result(column = "sys_Msg_Num",property = "sysMsgNum"),
             @Result(column = "last_System_Time",property = "lastSystemTime")
     })
             // @SelectProvider(type = BaseSelectProvider.class, method = "dynamicSQL")
             List<EduUser> searchByKeyWord(@Param("keyword") String keyword,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select count(*) from edu_user where  MOBILE=#{mobile}")
    int selectByMobile(String mobile);

    @Select("select user_Id from edu_user where MOBILE=#{mobile}")
    Integer selectUserIdByMobile(String mobile);
}
