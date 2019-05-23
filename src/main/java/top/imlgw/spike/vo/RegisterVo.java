package top.imlgw.spike.vo;


import org.hibernate.validator.constraints.Length;
import top.imlgw.spike.validator.IsMobile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author imlgw.top
 * @date 2019/5/13 13:16
 */
public class RegisterVo {
    @NotNull(message = "手机号不能为空")
    @IsMobile
    private String mobile;

    @NotNull(message = "密码不能为空")
    @Length(min=6,message = "密码设置过短")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    public RegisterVo(@NotNull String mobile, @NotNull @Length(min = 6) String password, String nickname) {
        this.mobile = mobile;
        this.password = password;
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "RegisterVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
