package top.imlgw.spike.vo;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author imlgw.top
 * @date 2019/5/13 13:16
 */
public class RegisterVo {
    @NotNull
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;

    private String nickname;

    public RegisterVo(@NotNull String mobile, @NotNull @Length(min = 32) String password, String nickname) {
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
