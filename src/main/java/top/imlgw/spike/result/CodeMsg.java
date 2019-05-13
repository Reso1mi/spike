package top.imlgw.spike.result;

/**
 * @author imlgw.top
 * @date 2019/5/11 18:42
 * 封装Code和msg
 */
public class CodeMsg {
    private Integer code;
    private String msg;

    //通用
    public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static final CodeMsg REGISTER_SUCCESS = new CodeMsg(1, "注册成功!!");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(-1, "server error !");
    //登陆，注册异常
    public static final CodeMsg SESSION_ERROR = new CodeMsg(500210, "session不存在或者已经失效");
    public static final CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static final CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "号码不能为空");
    public static final CodeMsg MOBILE_ERROR = new CodeMsg(500213, "号码格式错误");
    public static final CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "号码不存在");
    public static final CodeMsg PASSWORD_ERROR= new CodeMsg(500215, "密码错误");
    public static final CodeMsg NICKNAME_EMPTY = new CodeMsg(500216, "昵称不能为空");

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

/*    public CodeMsg fillArgs(Object...args){
        int code=this.code;
    }*/
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
