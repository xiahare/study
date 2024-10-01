
#include <iostream>
using namespace std;
const int mx_n=100;
int r, c;
int sx,sy;
char maze[mx_n][mx_n];
bool visited[mx_n][mx_n];
int steps;

bool possible(int x, int y){
    if( x>=0 && y>=0 && x<r && y<c && !visited[x][y] && maze[x][y]=='.'){
        return true;
    }
    return false;
}
void bloodfill(int x, int y){
    if(!possible(x,y)){
        return;
    }
    
    visited[x][y]=true;
    steps++;
    
    bloodfill(x+1, y);
    bloodfill(x-1, y);
    bloodfill(x, y+1);
    bloodfill(x, y-1);
}

int main(){

    cin>>r>>c;
    cin>>sx>>sy;
    for(int i=0;i<r;i++){
        for(int j=0;j<c;j++){
            cin>>maze[i][j];
        }
    }
    
    bloodfill(sx,sy);
    cout<<steps<<endl;
    return 0;
}


/**
 4 4
 0 2
 ...#
 ##..
 .#.#
 ..#.
 
 Answer: 6
 
 */
