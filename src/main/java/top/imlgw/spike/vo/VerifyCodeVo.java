package top.imlgw.spike.vo;

import java.awt.image.BufferedImage;

public class VerifyCodeVo {
    private Integer result;
    private BufferedImage bufferedImage;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public VerifyCodeVo() {

    }

    public VerifyCodeVo(Integer result, BufferedImage bufferedImage) {
        this.result = result;
        this.bufferedImage = bufferedImage;
    }
}