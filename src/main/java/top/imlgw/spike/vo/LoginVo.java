package top.imlgw.spike.vo;

import org.hibernate.validator.constraints.Length;
import top.imlgw.spike.validator.IsMobile;

import javax.validation.constraints.NotNull;

/**
 * @author imlgw.top
 * @date 2019/5/12 17:18
 */
public class LoginVo {

    @NotNull(message = "手机号不能为空")
    @IsMobile(groups = Test1.class)
    private String mobile;

    @NotNull(message = "密码不能为空")
    @Length(min=6 ,groups = Test2.class,message = "密码长度过短")
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

    public LoginVo(@NotNull(message = "手机号不能为空") @IsMobile(groups = Test1.class) String mobile, @NotNull(message = "密码不能为空") @Length(min = 6, groups = Test2.class, message = "密码长度过短") String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public interface Test1{}

    public interface Test2{}
}
