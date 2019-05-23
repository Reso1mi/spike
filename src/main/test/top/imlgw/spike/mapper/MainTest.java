package top.imlgw.spike.mapper;

import java.util.Scanner;

public class MainTest{

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int chip=sc.nextInt();
        int N=sc.nextInt();
        Process [] pros=new Process[N];
        for (int i=0;i<N;i++) {
            pros[i]=new Process(sc.next(),sc.nextInt());
        }
        int times=sc.nextInt();
        int count=0;
        for (int i=0;i<N;i++) {
            if(pros[i].requireTime<=0){
                continue;
            }
            for (int j=0;j<chip&&pros[j].requireTime>0;j++) {
                pros[i].requireTime--;
            }
            count++;
            if(count==times){
                System.out.println(pros[i].name);
            }
        }
    }

    static class Process{
        String name;
        int requireTime;
        public Process(String name,int requireTime){
            this.name=name;
            this.requireTime=requireTime;
        }
    }

}