package xl.tools.amc;

import java.util.HashSet;

public class MappingMod0 {
    public static void main(String[] args) {
        int num = 26;
        int start = 2023;
        HashSet<Integer> used = new HashSet<>(num);
        Integer[] result = new Integer[num];
        for (int i = 26; i > 0; i--) {

            for (int j = start+num; j >= start; j--) {
                if (j % i == 0) {
                    System.out.println(String.format("Number [%d] -> [%d]", i, j));
                    if (!used.contains(j)) {
                        result[i - 1] = j;
                        used.add(j);
                        System.out.println(String.format("===== Save result [%d] - [%d]", i, j));
                    }
                }

            }
        }
        System.out.println(String.format("==================================="));
        for (int i = 26; i > 0; i--) {

            System.out.println(String.format("Number [%d] -> [%d]", i, result[i - 1]));
        }
    }
}
