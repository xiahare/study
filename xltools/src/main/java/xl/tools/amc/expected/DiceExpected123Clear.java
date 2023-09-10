package xl.tools.amc.expected;

import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

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
