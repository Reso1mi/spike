package top.imlgw.spike.result;


/**
 * @author imlgw.top
 * @date 2019/5/11 18:21
 *
 * 通用结果返回集
 */
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    private Result(T data) {
        this.code=0;
        this.msg="success";
        this.data=data;
    }

    private Result(CodeMsg codeMsg){
        if(codeMsg==null)return;
        this.code=codeMsg.getCode();
        this.msg=codeMsg.getMsg();
    }

    public static <T> Result<T> success(CodeMsg codeMsg){
        return  new Result<T>(codeMsg);
    }

    public static <T> Result<T> success(T data){
        return  new Result<T>(data);
    }


    public static <T> Result<T> error(CodeMsg codeMsg){
        return  new Result<T>(codeMsg);
    }


    public int getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }

}
