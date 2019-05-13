package top.imlgw.spike.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author imlgw.top
 * @date 2019/5/12 17:18
 */
public class LoginVo {

    @NotNull
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;

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
    @Override
    public String toString() {
        return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
    }
}
