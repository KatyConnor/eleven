package com.hx.thread.pool.executor.pool;

/**
 * 并行递归
 * @Author mingliang
 * @Date 2018-07-18 10:22
 */
public class CombTest {

    int m=0,n=0,total=0;
    CombTest(int n, int m){
        this.m=m;
        this.n=n;
    }
    public void comb(String str)
    {
        for(int i=1;i<n+1;i++){
            if(str.length()==m-1){
                System.out.println(str+i);//打印出组合序列
                total++;
            }
            else
                comb(str+i);
        }
    }

    public static void main(String[] args)
    {
        CombTest ct = new CombTest(4, 10);
        long begin = System.currentTimeMillis();
        ct.comb("");
        System.out.println("total:"+ct.total);
        long end = System.currentTimeMillis();
        System.out.println("time:"+(end-begin)/1000+"s");
    }
}
