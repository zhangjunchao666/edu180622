package com.atguigu.edu.usermanager.controller;

import com.atguigu.edu.bean.EduUser;
import com.atguitu.edu.EduUserService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private EduUserService eduUserService;


    @RequestMapping("index")
    public String gotostudent(){
        return "index";
    }



    @RequestMapping("welcome")
    public String toWelcome(){
        return "welcome";
    }

    @RequestMapping("article-list")
    public String articlelist(HttpServletRequest request){
        //从后台取全部学生数据
        List<EduUser> eduUserList = eduUserService.selectAll();
        request.setAttribute("eduUserList",eduUserList);
        request.setAttribute("maxtotal",eduUserList.size());
        return "article-list";
    }


    @RequestMapping("getAll")
    @ResponseBody
    private List<EduUser> getAll(){
        return eduUserService.selectAll();
    }

    @RequestMapping("updataStatus")
    @ResponseBody
    public EduUser updataStatus(Integer userId){
        EduUser eduUser = eduUserService.updataStatusById(userId);
        return eduUser;
    }

    @RequestMapping("a")
    public String updatePasswd(HttpServletRequest request,Integer userId){
        //把userId存到域中,传到修改密码页面
        request.setAttribute("userId",userId);
        return "a";
    }

    //修改密码后台代码
    @RequestMapping("upPW")
    @ResponseBody
    public String upPW(String pw, Integer userId) {

        //根据userId去更新数据库密码
        Boolean aBoolean = eduUserService.updataPasswdById(pw, userId);
        if (aBoolean) {
            return "result";
        } else {
            return  pw;
        }
    }

    //根据查询
    @RequestMapping("serchKeWord")
    public String serchKeWord(HttpServletRequest request, String keyword, String logmin,String logmax){
        List<EduUser> eduUserList = eduUserService.serchKeWord(keyword,logmin,logmax);
       request.setAttribute("eduUserList",eduUserList);
        return "article-list::tablerefsh";
    }

    //导入数据 import Exc
    @RequestMapping(value = "/import")
    public String exImport(@RequestParam(value = "filename") MultipartFile file, HttpSession session) {
        boolean a = false;
        String filename = file.getOriginalFilename();
       try {
           a = eduUserService.batchImport(filename,file);
       }catch (Exception e){
          e.printStackTrace();
       }
        return "redirect:article-list";
    }
    //导出数据 export Exc
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(HttpServletResponse response) throws IOException {
        List<EduUser> users = eduUserService.selectAll();

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("获取excel测试表格");

        HSSFRow row = null;
        row = sheet.createRow(0);//创建第一个单元格
        row.setHeight((short) (26.25 * 20));
        row.createCell(0).setCellValue("用户信息列表");//为第一行单元格设值
        /*为标题设计空间
        * firstRow从第1行开始
        * lastRow从第0行结束
        *从第1个单元格开始
        * 从第3个单元格结束
        */
        CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
        sheet.addMergedRegion(rowRegion);
        /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
         sheet.addMergedRegion(columnRegion);*/
        /*
        * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where table_name='user' and table_schema='test'
        * 第一个table_name 表名字
        * 第二个table_name 数据库名称
        */
        row = sheet.createRow(1);
        row.setHeight((short) (22.50 * 20));//设置行高
        row.createCell(0).setCellValue("用户ID");//为第0个单元格设值
        row.createCell(1).setCellValue("电话号码");//为第1个单元格设值
        row.createCell(2).setCellValue("邮箱");//为第2个单元格设值
        row.createCell(3).setCellValue("密码");//为第3个单元格设值
        row.createCell(4).setCellValue("用户名");//为第4个单元格设值
        row.createCell(5).setCellValue("昵称");//为第5个单元格设值
        row.createCell(6).setCellValue("SEX");//为第6个单元格设值
        row.createCell(7).setCellValue("年龄");//为第7个单元格设值
        row.createCell(8).setCellValue("创建时间");//为第8个单元格设值
        row.createCell(9).setCellValue("状态");//为第9个单元格设值
        row.createCell(10).setCellValue("PIC_IMG");//为第10个单元格设值
        row.createCell(11).setCellValue("BANNER_URL");//为第11个单元格设值
        row.createCell(12).setCellValue("MSG_NUM");//为第12个单元格设值
        row.createCell(13).setCellValue("SYS_MSG_NUM");//为第13个单元格设值
        row.createCell(14).setCellValue("LAST_SYSTEM_TIME");//为第14个单元格设值

        for (int i = 0; i < users.size(); i++) {
            row = sheet.createRow(i + 2);
            EduUser user = users.get(i);
            row.createCell(0).setCellValue(user.getUserId());//获取第0个单元格值
            row.createCell(1).setCellValue(user.getMobile());//获取第1个单元格值
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getPassword());
            row.createCell(4).setCellValue(user.getUserName());
            row.createCell(5).setCellValue(user.getShowName());
            row.createCell(6).setCellValue(user.getSex()==false?"女":"男");
            row.createCell(7).setCellValue(user.getAge());
            row.createCell(8).setCellValue((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(user.getCreateTime()));
            row.createCell(9).setCellValue(user.getIsAvalible()==false?"冻结":"正常");
            row.createCell(10).setCellValue(user.getPicImg());
            row.createCell(11).setCellValue(user.getBannerUrl());
            row.createCell(12).setCellValue(user.getMsgNum());
            row.createCell(13).setCellValue(user.getSysMsgNum());
            row.createCell(14).setCellValue((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(user.getLastSystemTime()));

        }
            sheet.setDefaultRowHeight((short) (16.5 * 20));
           //列宽自适应
            for (int i = 0; i <= 13; i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            OutputStream os = response.getOutputStream();
            response.setHeader("Content-disposition", "attachment;filename=user.xls");//默认Excel名称
            wb.write(os);
            os.flush();
            os.close();
        }





        }





