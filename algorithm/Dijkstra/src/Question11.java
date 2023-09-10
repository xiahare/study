public class Question11 {
    public static void main(String[] args){

        for(int i=0;i<64;i++){
            // 2^i
            long a = (long) Math.pow(2,i);
            //long b = (long) Math.sqrt(a+7) ;
            long b = 500000000L;
            for(int j=0;j<=b;j++){
                if(a+7==j*j){
                    System.out.println("n:"+i+" , x:"+j);
                }
            }
        }
    }
}
