package com.atguigu.edu.usermanager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.edu.usermanager.common.MyException;
import com.atguigu.edu.usermanager.mapper.EduUserMapper;
import com.atguigu.edu.util.MD5Util;
import com.atguigu.edu.bean.EduUser;
import com.atguitu.edu.EduUserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class EduUserServiceImpl implements EduUserService {

    @Autowired
    private EduUserMapper eduUserMapper;


    @Override
    public List<EduUser> selectAll() {
        return eduUserMapper.selectAll();
    }

    @Override
    public EduUser updataStatusById(Integer userId) {
        //根据userId查询数据库用户的状态
        EduUser eduUser = eduUserMapper.selectByPrimaryKey(userId);
        //判断:如果状态是true,更改为false,反之
        eduUser.setIsAvalible(!eduUser.getIsAvalible());
        eduUserMapper.updateByPrimaryKeySelective(eduUser);
        return eduUser;
    }
    //修改密码  更新数据库
    @Override
    public Boolean updataPasswdById(String pw, Integer userId) {

        String newPasswd = MD5Util.digest(pw);
        EduUser eduUser = eduUserMapper.selectByPrimaryKey(userId);
        if (newPasswd.equals(eduUser.getPassword())){
            return false;
        }else {
            //如果不一致,把MD5转换后的密码set进对象里,然后把对象存入数据库
            eduUser.setPassword(newPasswd);
           eduUserMapper.updateByPrimaryKey(eduUser);
        }
        return true;
    }
    //根据keword查询
    @Override
    public List<EduUser> serchKeWord(String keyword, String logmin, String logmax) {
        List<EduUser> eduUsers = eduUserMapper.searchByKeyWord(keyword,logmin,logmax);
        return eduUsers;
    }

    //批量导入 import Excess
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {
        boolean notNull = false;
        List<EduUser> userList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }
        EduUser eduUser;
        for (int r = 2; r <= sheet.getLastRowNum(); r++) {//r = 1 表示从第2行开始循环 如果你的第2行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null) {
                continue;
            }
            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException
            eduUser = new EduUser();

            if (row.getCell(0).getCellType() != 1) {//循环时，得到每一行的单元格进行判断
                throw new MyException("导入失败(第" + (r + 1) + "行,用户名请设为文本格式)");
            }

            String userId = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值
            if (userId == null || userId.isEmpty()) {//判断是否为空
                throw new MyException("导入失败(第" + (r + 1) + "行,用户id未填写)");
            }

            row.getCell(1).setCellType(1);//得到每一行的 第二个单元格的值
            String mobile = row.getCell(1).getStringCellValue();

            if (mobile == null || mobile.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 1) + "行,电话未填写)");
            }


            row.getCell(2).setCellType(1);//得到每一行的 第三个单元格的值
            String email = row.getCell(2).getStringCellValue();

            if (email == null || email.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 2) + "行,邮箱未填写)");
            }

            row.getCell(3).setCellType(1);//得到每一行的 第四个单元格的值
            String password = row.getCell(3).getStringCellValue();

            if (password == null || password.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 3) + "行,密码未填写)");
            }

            row.getCell(4).setCellType(1);//得到每一行的 第五个单元格的值
            String userName = row.getCell(4).getStringCellValue();

            if (userName == null || userName.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 4) + "行,用户名未填写)");
            }

            row.getCell(5).setCellType(1);//得到每一行的 第六个单元格的值
            String showName = row.getCell(5).getStringCellValue();

            if (showName == null || showName.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 5) + "行,用户名昵称未填写)");
            }

            row.getCell(6).setCellType(1);//得到每一行的 第七个单元格的值
            String sex = row.getCell(6).getStringCellValue();

            if (sex == null || sex.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 6) + "行,用户名性别未填写)");
            }

            row.getCell(7).setCellType(1);//得到每一行的 第八个单元格的值
            String age = row.getCell(7).getStringCellValue();

            if (age == null || age.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 7) + "行,用户名性别未填写)");
            }

            row.getCell(8).setCellType(1);//得到每一行的 第九个单元格的值
            String createTime = row.getCell(8).getStringCellValue();

            if (createTime == null || createTime.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 8) + "行,创建时间未填写)");
            }

            row.getCell(9).setCellType(1);//得到每一行的 第十个单元格的值
            String isAvalible = row.getCell(9).getStringCellValue();

            if (isAvalible == null || isAvalible.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 9) + "行,创建时间未填写)");
            }

            row.getCell(10).setCellType(1);//得到每一行的 第十一个单元格的值
            String picImg = row.getCell(10).getStringCellValue();

            if (picImg == null || picImg.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 10) + "行,创建时间未填写)");
            }

            row.getCell(11).setCellType(1);//得到每一行的 第十二个单元格的值
            String bannerUrl = row.getCell(11).getStringCellValue();

            if (bannerUrl == null || bannerUrl.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 11) + "行,图片地址未填写)");
            }

            row.getCell(12).setCellType(1);//得到每一行的 第十三个单元格的值
            String msgNum = row.getCell(12).getStringCellValue();

            if (msgNum == null || msgNum.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 12) + "行,msgNum未填写)");
            }

            row.getCell(13).setCellType(1);//得到每一行的 第十四个单元格的值
            String sysMsgNum = row.getCell(13).getStringCellValue();

            if (sysMsgNum == null || sysMsgNum.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 13) + "行,sysMsgNum未填写)");
            }

            row.getCell(14).setCellType(1);//得到每一行的 第十五个单元格的值
            String lastSystemTime = row.getCell(14).getStringCellValue();

            if (lastSystemTime == null || lastSystemTime.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 14) + "行,lastSystemTime未填写)");
            }

            int nuserId = Integer.parseInt(userId);

            Boolean nsex = sex.equals("男") ? true : false;

            Byte nage = Byte.valueOf(age);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(createTime);
            Date ncreateTime = new java.sql.Date(date.getTime());

            boolean nisAvalible = "1".equals(isAvalible) ? true : false;

            int nmsgNum = Integer.parseInt(msgNum);

            int nsysMsgNum = Integer.parseInt(sysMsgNum);

            Date date1 = format.parse(lastSystemTime);
            Date nlastSystemTime = new java.sql.Date(date1.getTime());
            //完整的循环一次 就组成了一个对象
            eduUser.setUserId(null);
            eduUser.setMobile(mobile);
            eduUser.setEmail(email);
            eduUser.setPassword(password);
            eduUser.setUserName(userName);
            eduUser.setShowName(showName);
            eduUser.setSex(nsex);
            eduUser.setAge(nage);
            eduUser.setCreateTime(ncreateTime);
            eduUser.setIsAvalible(nisAvalible);
            eduUser.setPicImg(picImg);
            eduUser.setBannerUrl(bannerUrl);
            eduUser.setMsgNum(nmsgNum);
            eduUser.setSysMsgNum(nsysMsgNum);
            eduUser.setLastSystemTime(nlastSystemTime);

            //把数据放入list集合
            userList.add(eduUser);

        }
        for (EduUser userResord : userList) {
            //获取表格电话
            String mobile = userResord.getMobile();
            //获取数据库电话
           int cnt = eduUserMapper.selectByMobile(mobile);
           if (cnt == 0){
               eduUserMapper.insertSelective(userResord);
              System.out.println("插入"+userResord);
           }else {
               Integer userId = eduUserMapper.selectUserIdByMobile(mobile);
               userResord.setUserId(userId);
               eduUserMapper.updateByPrimaryKey(userResord);
               System.out.println("更新"+userResord);
           }
        }
            return notNull;
    }


}
