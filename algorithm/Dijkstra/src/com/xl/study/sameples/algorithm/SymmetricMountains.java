package com.xl.study.sameples.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymmetricMountains {
    public static void main(String[] args){
        // test 1: [3 1 4 1 5 9 2]
        System.out.println(calculateSymmetricMountains(new Integer[]{3,1,4,1,5,9,2}));
        // result 1: [0, 2, 0, 5, 2, 10, 10]

        // test 2: [1 3 5 6]
        System.out.println(calculateSymmetricMountains(new Integer[]{1,3,5,6}));
        // result 2: [0, 1, 3, 7]
    }

    public static List<Integer> calculateSymmetricMountains(Integer[] all){
        List<Integer> result = new ArrayList<>();
        int N = all.length;
        for ( int step = 0; step < N; step++ ){
            // calculate i-th min Asymmetric Height
            Integer minAsymmetricHeight = Integer.MAX_VALUE;
            for (int j = 0; j < N-step; j++) {
                //loop len=(step+1) consecutive numbers
                int len = step+1;
                int from = j;
                int to = j+len;
                int asymmetricHeight = calculateAsymmetricHeight(Arrays.copyOfRange(all, from, to));
                minAsymmetricHeight = Math.min(minAsymmetricHeight,asymmetricHeight);

            }
            result.add(minAsymmetricHeight);
        }
        return result;
    }

    private static int calculateAsymmetricHeight(Integer[] sub){
        // Calculate sub list Asymmetric Height
        int subLen = sub.length;
        int maxIndex = (int) Math.ceil(subLen / 2.0);
        int sum = 0;
        for (int i = 0; i < maxIndex; i++) {
            int left = sub[i];
            int right = sub[(subLen-1)-i];
            sum = sum + Math.abs(left-right);
        }
        return sum;
    }
}
