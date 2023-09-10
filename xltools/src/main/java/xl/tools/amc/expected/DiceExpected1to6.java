package xl.tools.amc.expected;

import java.util.HashSet;
import java.util.Random;

/**
 * Division B Individual Round
 * 2023 Cowconuts Labor Day Math Competition
 * Cowconuts Math Club
 * September 3, 2023
 *
 * 20. A standard 6-sided die is repeatedly rolled until every number from 1 to 6 inclusive appears on
 * the top face at least once. Let m
 * n be the number of rolls, on average, that are necessary for this
 * to occur. What is m + n?
 */
public class DiceExpected1to6 {
    public static void main(String[] args) {
        double avg = 0;
        int testTimes = 100000;
        for(int i=1; i<=testTimes; i++){
            int times = test();
            avg=(1.0*avg*(i-1)+times)/i;
            System.out.println(String.format("Current Average [%5.2f]: [%d]",avg ,i));
        }
    }
    private static int test(){
        HashSet<Integer> occurDice = new HashSet<>(6);
        int totalTimes = 0;

        while(true){
            if(occurDice.size()==6){
                break;
            }
            int currentDice = getRandomDice();
            occurDice.add(currentDice);
            totalTimes++;
            System.out.println(String.format("Count [%3d]: [%d]",totalTimes ,currentDice));
        }

        System.out.println(String.format("Count [%3d] ============ END ===============",totalTimes ));

        return totalTimes;
    }
    private static Random random = new Random();
    private static int getRandomDice(){

        return random.nextInt(6);
    }
}
