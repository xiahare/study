public class Question6L2 {
    public static void main(String[] args){
        int a = 49;
        int b2 = 400*6;
        int exp = 505;
        int mod = 101;
        int iloop = (exp-1)/2;
        int sumWithMod = 0;
        // sum: combination(505,2*i)  * pow(400*6,i) * pow(49,505-2*i)
        for( int i=0; i<=iloop; i++){
            int tmp = combinationWithMod(exp,i*2, mod) * powWithMod(b2, i, mod) * powWithMod(a, exp-i*2, mod);
            //int tmp = powWithMod(b2, i, mod) * powWithMod(a, exp-i*2, mod);
            tmp = tmp % mod;
            System.out.println(String.format("combinationWithMod(exp,i*2, mod):%d, powWithMod(b2, i, mod):%d, powWithMod(a, exp-i*2, mod):%d, tmp:%d", combinationWithMod(exp,i*2, mod),powWithMod(b2, i, mod),powWithMod(a, exp-i*2, mod), tmp));
            sumWithMod = (sumWithMod + tmp)%mod;
        }
        System.out.println(String.format("sumWithMod:%d", sumWithMod));


    }
    private static int powWithMod(int num, int exp, int mod){
        int ans = 1;

        while(exp>0){
            if((exp&1)>0){
                ans = ans * num % mod ;
            }
            num = num * num % mod;
            exp >>= 1;
        }
        return ans;
    }
/*    private static int powWithMod(int num, int exp, int mod){
        int res = 1;

        for(int i=1; i<=exp ; i++){
            res = modCeiling(res*num,mod);
        }
        return res;
    }*/


    // a^(euler(n)-1) is inverse of a
    private static int euler(int n){
        int res = n;
        for ( int i=2; i*i<=n; i++){
            if ( n%i == 0){
                res = res / i * (i-1);
                while ( n%i == 0 ){
                    n /= i;
                }
            }
        }
        if(n != 1){
            res = res / n * ( n-1 );
        }
        return res;
    }

    private static int niyuan(int num, int mod){

        // return powWithMod(num, mod-2, mod);
        return powWithMod(num, euler(mod)-1, mod);
    }

    private static int combinationWithMod(int num, int sub, int mod){

        return modCeiling(factorialWithMod(num,mod)*modCeiling(niyuan(factorialWithMod(sub,mod),mod),mod)*modCeiling(niyuan(factorialWithMod(num-sub,mod),mod),mod),mod);
    }

    private static int factorialWithMod(int num, int mod){
        int res = 1;

        for(int i=1; i<=num ; i++){
           res = modCeiling(res*i,mod);
        }

        return res;
    }

    private static int modCeiling(int num , int mod){
        int res = num%mod;
        if (res==0){
            res = mod;
        }
        return res;
    }
}
