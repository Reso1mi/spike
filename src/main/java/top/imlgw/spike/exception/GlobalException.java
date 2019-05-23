package top.imlgw.spike.exception;

import top.imlgw.spike.result.CodeMsg;

/**
 * 全局通用异常
 * @author imlgw.top
 * @date 2019/5/14 20:51
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
