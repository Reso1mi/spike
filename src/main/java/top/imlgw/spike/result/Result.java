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


    /**
     * @param codeMsg
     * @param <T>
     * @return 定义好的CodeMsg类型
     */
    public static <T> Result<T> success(CodeMsg codeMsg){
        return  new Result<>(codeMsg);
    }

    /**
     * @param data 返回给前端的数据
     * @param <T>  msg==success
     * @return     code=0
     */
    public static <T> Result<T> success(T data){
        return  new Result<>(data);
    }

    /**
     * @param codeMsg
     * @param <T>
     * @return 定义好的error CodeMsg类型
     */
    public static <T> Result<T> error(CodeMsg codeMsg){
        return  new Result<>(codeMsg);
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
