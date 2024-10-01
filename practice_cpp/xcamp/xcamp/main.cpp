#include <iostream>
using namespace std;
long factorial(long n)
{
  long val = 1;
  for (long i = 1; i <=n; i++)
    {
      val *= i;
    }
  return val;
}

long nCm(long n, long m)
{
  long val = factorial(m)/(factorial(n)*factorial(m-n));
  return val;
}

int main()
{
    long a, b;
    cin >> a >> b;
    cout << nCm(min(a, b), a+b);
}
