package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user")
public class User  implements Serializable {

    private Integer id;
    private String userName;
    private String passWord;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String date;
    private String nickName;

}
