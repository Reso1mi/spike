package top.imlgw.spike.utils;

import top.imlgw.spike.entity.SpikeUser;

/**
 * @author imlgw.top
 * @date 2019/6/8 23:51
 * ThreadLocal User上下文，使得运行在当前线程下的方法都可以方便的获取用户信息
 */
public class UserContext {

    private static  ThreadLocal<SpikeUser> userHolder =new ThreadLocal<>();

    public static void setUser(SpikeUser spikeUser){
        userHolder.set(spikeUser);
    }

    public static SpikeUser getUser(){
        return userHolder.get();
    }

    public static void removeUser(){
        userHolder.remove();
    }
}
