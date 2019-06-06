package top.imlgw.spike.mapper;

import java.util.Scanner;
import java.util.ArrayList;

public class Main3 {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        //矩形大小
        int k=sc.nextInt();
        byte []nums=new byte[k*k];
        int allStore=k*k;
        //素数赋值
        for (int i=0;i<nums.length;i++) {
            if(isPrime(i)){
                nums[i]=1;
                System.out.println("1");
            }
        }
        //进程数
        int N=sc.nextInt();
        Process []ps=new Process[N];

        for (int i = 0; i < N; i++) {
            ps[i]=new Process("p"+(i+1),sc.nextInt());
        }

        for(int i=0;i<N;i++) {
            add(ps[i],nums);
        }

        //want to free process nums
        int n=sc.nextInt();
        int []freeProcessIdxs=new int[n];
        for (int s=0;s<n;s++) {
            //process name
            String  freeProcess=sc.next();
            //切割获取要释放的进程的下标
            freeProcessIdxs[s]=Integer.valueOf(freeProcess.substring(1))-1;
        }

        //print result 处理空格
        for (int i = 0; i < ps.length; i++) {
            if(ps[i].last!=-1) {
                if(i==N-1) {
                    System.out.println(ps[i].last);
                }else {
                    System.out.print(ps[i].last+" ");
                }
            }else {
                if(i==N-1) {
                    System.out.println("false");
                }else {
                    System.out.print("false ");
                }
            }
        }

        //print free process
        for (int i=0;i<freeProcessIdxs.length;i++) {
            if(freeProcessIdxs[i]>=N || ps[freeProcessIdxs[i]].last==-1){
                System.out.println("false");
            }else{
                ArrayList storelist=ps[freeProcessIdxs[i]].indexList;
                for (int j=0;j<storelist.size();j++) {
                    System.out.print(storelist.get(j)+" ");
                }
                System.out.println();
            }
        }
    }

    public static void add(Process p,byte nums[]) {
        if(free(nums)<p.storage) {
            p.last=-1;
            return;
        }
        int last=0;
        int count=0;
        for(int i=0;i<nums.length;i++) {
            if(nums[i]==1) {
                continue;
            }else {
                nums[i]=1;
                //add to list
                p.indexList.add(i);

                last=i;
                count++;
                if(count==p.storage) {
                    p.last=last;
                    return;
                }
            }
        }
        //p.last=-1;
    }

    public static int free(byte nums[]) {
        int count=0;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i]==1) {
                count++;
            }
        }
        return nums.length-count;
    }

    public static boolean isPrime(int val){
        boolean flag = true;
        if((val & 1) == 0){
            flag = false;
            return flag;
        }
        for(int i=3; i<= Math.sqrt(val); i+=2){
            if(val%i == 0){
                flag = false;
                break;
            }
        }
        return flag;
    }
}

class Process{
    String name;
    int storage;
    int last;
    ArrayList<Integer> indexList=new ArrayList<>();
    public Process(String name,int storage){
        this.storage=storage;
        this.name=name;
    }
}