package com.ego.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Length(min = 4, max = 20, message = "用户名只能在4~20位之间")
    private String username;// 用户名
    @JsonIgnore
    @Length(min = 4,max = 16,message = "密码只能在4~16位之间")
    private String password;// 密码
    @Pattern(regexp = "^1[356789]\\d{9}$", message = "手机号格式不正确")
    private String phone;// 电话
    private LocalDateTime created;// 创建时间
}
