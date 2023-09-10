public class Question10 {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(java.lang.Object[].class);
        System.out.println(byte[].class);
        Class clazz = Class.forName("[Ljava.lang.Object;");

        boolean b = java.lang.Object[].class == clazz;
        System.out.println("java.lang.Object[].class == clazz  ==>" + b);
        int curNum = 100;
        int lastNum = 100;
        int longest = 0;
        int start = 100;
        int end = 100;
        for(int i=100;i<1000;i++){
            //
            int sumI = sum(i);
            if (i % sumI==0){


                lastNum = curNum;
                curNum = i;
                if(curNum-lastNum-1>longest){
                    longest = curNum-lastNum-1;
                    start = lastNum + 1;
                    end = curNum - 1;
                }
                System.out.println("n:"+i + " d="+sumI + " length=" + (curNum-lastNum-1));
            }
        }
        System.out.println("============= Result longest:"+longest + "  Start: " + start + "  End:" + end );

    }

    private static int sum(int num){
        int sum = 0;
        String strI = String.valueOf(num);
        for(int m=0; m<3 ; m++){
           sum += Integer.parseInt(String.valueOf(strI.charAt(m))) ;
        }
        return sum;
    }
}
