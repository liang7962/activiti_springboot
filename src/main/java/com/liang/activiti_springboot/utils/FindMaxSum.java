package com.liang.activiti_springboot.utils;

/*
*       连续子数组的和
* */
public class FindMaxSum {

    public static void main(String[] args) {
        int[] arrays={1 , -2 , 3 , 10 , -4 , 7 , 2 , -5 };
        System.out.println(FindMaxSum.FindGreatestSumOfSubArray(arrays));
    }

    public static int FindGreatestSumOfSubArray(int[] array) {

        if(array == null || (array.length == 1 && array[0] <= 0))
            return 0;

        int cur = array[0];
        int sum = array[0];
        for(int i = 1;i < array.length;i++){
            if(cur < 0)
                cur = 0;
            cur = cur + array[i];
            if(sum <= cur){
//                System.out.println("i>>>>"+i);
                sum = cur;
            }

        }
        return sum;
    }

}
