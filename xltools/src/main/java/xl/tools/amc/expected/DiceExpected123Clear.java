package xl.tools.amc.expected;

import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

/**
 * Division B Individual Round
 * 2023 Cowconuts Labor Day Math Competition
 * Cowconuts Math Club
 * September 3, 2023
 *
 * 16. Lawrence’s calculator has only 4 buttons: the numbers 1, 2, and 3 as well as a “clear” button
 * which clears the display. Lawrence starts with an empty display and presses buttons randomly
 * until his display contains only the three digits “123”, in that order. What is the expected
 * number of buttons he must press until this happens?
 */
public class DiceExpected123Clear {
    public static void main(String[] args) {
        double avg = 0;
        int testTimes = 100000;
        for(int i=1; i<=testTimes; i++){
            int times = test();
            avg=(1.0*avg*(i-1)+times)/i;
            System.out.println(String.format("#%5d Average [%5.2f]",i,avg));
        }
    }
    private static int test(){
        String display = "";
        int totalTimes = 0;

        while(true){

            if(display.endsWith("123")){
                break;
            }
            int currentDice = getRandomDice();
            if(currentDice==0){
                display = "";
            } else {
                display = display + currentDice;
            }

            totalTimes++;
            System.out.println(String.format("Count [%3d], Dice: [%d],display: [%s]",totalTimes, currentDice ,display));
        }

        System.out.println(String.format("Count [%3d] ============ END ===============",totalTimes ));

        return totalTimes;
    }
    private static Random random = new Random();
    private static int getRandomDice(){

        return random.nextInt(4);
    }
}
