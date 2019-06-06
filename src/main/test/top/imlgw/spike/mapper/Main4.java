package top.imlgw.spike.mapper;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Main4 {

    static int allStore=0;

    static List<Integer> storedList=new ArrayList<>();

    static int primeStore;

    static  int sotreLast=-1;

    public static void main(String[] args) {
        //System.out.println(isPrime(4));
        Scanner sc = new Scanner(System.in);
        //矩形大小
        int k = sc.nextInt();
        allStore = k * k;
        primeStore=0;
        countPrime(allStore);
        //进程数
        int N = sc.nextInt();
        Process[] ps = new Process[N];

        for (int i = 0; i < N; i++) {
            ps[i] = new Process("p" + (i + 1), sc.nextInt());
        }

        for (int i = 0; i < N; i++) {
            add(ps[i]);
            if(ps[i].last!=-1){
                sotreLast=ps[i].last;
            }else {
                sotreLast=sotreLast;
            }
        }

        //want to free process nums
        int n = sc.nextInt();
        int[] freeProcessIdxs = new int[n];
        int s = 0;
        for (s = 0; s < n; s++) {
            //process name
            String freeProcess = sc.next();
            freeProcessIdxs[s] = Integer.valueOf(freeProcess.substring(1)) - 1;
        }
        //print result
        for (int i = 0; i < ps.length; i++) {
            if (ps[i].last != -1) {
                if (i == N - 1) {
                    System.out.println(ps[i].last);
                } else {
                    System.out.print(ps[i].last + " ");
                }
            } else {
                if (i == N - 1) {
                    System.out.println("false");
                } else {
                    System.out.print("false ");
                }
            }
        }

        //print free process
        for (int i = 0; i < freeProcessIdxs.length; i++) {
            if (freeProcessIdxs[i] >= N || ps[freeProcessIdxs[i]].last == -1) {
                System.out.println("false");
            } else {
                ArrayList storelist = ps[freeProcessIdxs[i]].indexList;
                for (int j = 0; j < storelist.size(); j++) {
                    System.out.print(storelist.get(j) + " ");
                }
                System.out.println();
            }
        }
    }

    public static void add(Process p) {
        int storage = p.storage;
        int count=0;
        if(isfree(p)){
            for (int i=sotreLast+1;i<allStore;i++){
                if(!isPrime(i)){
                    p.indexList.add(i);
                    storedList.add(i);
                    count++;
                    if(count==storage){
                        p.last=i;
                        return;
                    }
                }
            }
        } else{
            p.last=-1;
        }
    }

    public static boolean isfree(Process p) {
        return p.storage+primeStore+storedList.size()<allStore;
    }

    public static boolean isPrime(int val) {
        if(val==1){
            return false;
        }if (val==2){
            return true;
        }
        boolean flag = true;
        if ((val & 1) == 0) {
            flag = false;
            return flag;
        }
        for (int i = 3; i <= Math.sqrt(val); i += 2) {
            if (val % i == 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    //count prime
    public static int countPrime(int n){
        int res=0;
        //申请一个足够空间的数组，进行标记
        boolean[] nums = new boolean[n];
        for (int i = 2; i < nums.length; i++) {  //首先标记全部标记为true
            nums[i] = true;
        }

        //遍历数组，采用上述算法，标记是倍数的为false
        for(int i = 2; i*i < nums.length; i++) {
            if (nums[i]) {
                for(int j = i*i; j < nums.length; j+=i) {
                    nums[j] = false;
                }
            }
        }

        //统计出为true的，就是质数的总数
        for(boolean bool : nums) {
            res += bool ? 1 : 0;
        }

        return res;
    }

    static class Process {
        String name;
        int storage;
        int last;
        ArrayList<Integer> indexList = new ArrayList<>();

        public Process(String name, int storage) {
            this.storage = storage;
            this.name = name;
        }
    }
}

