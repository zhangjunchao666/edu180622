package com.atguitu.edu;

import com.atguigu.edu.bean.EduUser;
import org.springframework.web.multipart.MultipartFile;
import org.apache.ibatis.annotations.Select;
import java.util.Date;
import java.util.List;

public interface EduUserService {

    //查询所有
    List<EduUser> selectAll();
    //根据userId更改状态
    EduUser updataStatusById(Integer userId);

    //根据userId去更新数据库密码
    Boolean updataPasswdById(String pw, Integer userId);


    //根据keword查询
    List<EduUser> serchKeWord(String keyword, String logmin, String logmax);

    //批量导入 import Excess
    boolean batchImport(String filename, MultipartFile file) throws Exception;
}
