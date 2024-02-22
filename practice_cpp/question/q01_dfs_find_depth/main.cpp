
#include <iostream>

using namespace std;

const int maxn = 1e6+1;
int n;

int depth = 0;

struct node{
    int left;
    int right;
}a[maxn];

void dfs( int root, int dep){
    if ( root == 0 ) return;
    depth = max(depth,dep);
    dfs(a[root].left,dep+1);
    dfs(a[root].right,dep+1);
}

int main(){
    
    cin >> n;
    for(int i=1; i<=n; i++){
        cin >> a[i].left >> a[i].right;
    }

    dfs(1,1);

    cout << depth  << endl;
    return 0;
}

/**
 
 7
 2 7
 3 6
 4 5
 0 0
 0 0
 0 0
 0 0
 
 */

