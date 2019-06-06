package top.imlgw.spike.mapper;

import java.util.Scanner;

/**
 * @author imlgw.top
 * @date 2019/6/4 16:24
 */
public class Mian11 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String []str = new String[3];
        int []base=new int[64];
        // 1 7 13 23 47
        base[1]=1;
        base[7]=1;
        base[13]=1;
        base[23]=1;
        base[47]=1;
        int count=0;
        int n=sc.nextInt();
        if(n>=58){
            System.out.println("false");
            return;
        }
        for (int i=0;i<64;i++){
            if(base[i]==1){
                continue;
            }else {
                count++;
                if(count==n){
                    System.out.println(count);
                    return;
                }
            }
        }
    }
}
