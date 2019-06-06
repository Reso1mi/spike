package top.imlgw.spike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author imlgw.top
 * @date 2019/5/11 14:15
 */
@SpringBootApplication
public class SpikeApplication /*extends SpringBootServletInitializer*/ {
    public static void main(String[] args) {
        SpringApplication.run(SpikeApplication.class, args);
    }

    /**
     * @param builder
     * @return 打 war包
     */
    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpikeApplication.class);
    }*/
}
