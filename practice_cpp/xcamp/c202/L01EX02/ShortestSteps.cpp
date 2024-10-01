#include <iostream>
#include <vector>
#include <climits>

using namespace std;


bool possible(int x,int y,int endx,int endy, vector<vector<char>> &maze, vector<vector<bool>> &visited, int row, int col){
    if(x>=0&&y>=0&&x<row&&y<col&&!visited[x][y]&&maze[x][y]=='.'){
        return true;
    }
    return false;
}

/**
  * &visited, &best_steps : will be changed in the function
  * other parameters are not
  */
void bloodfill(int x,int y,int endx,int endy, vector<vector<char>> &maze, vector<vector<bool>> &visited, int row, int col, int& best_steps, int current_steps){
    if( !possible(x,y,endx,endy,maze,visited,row,col) ){
        return ;
    }
    
    if( x==endx && y==endy ){
        best_steps = min(best_steps,current_steps);
        return;
    }
    
    int next_steps = current_steps + 1;
    visited[x][y]=true;
    
    bloodfill(x+1,y,endx,endy,maze,visited,row,col,best_steps,next_steps);
    bloodfill(x-1,y,endx,endy,maze,visited,row,col,best_steps,next_steps);
    bloodfill(x,y+1,endx,endy,maze,visited,row,col,best_steps,next_steps);
    bloodfill(x,y-1,endx,endy,maze,visited,row,col,best_steps,next_steps);
    
    // backtracking
    visited[x][y]=false;

}
int main(){
    int row, col;
    cin>>row>>col;
    vector<vector<char>> maze(row, vector<char>(col));
    for(int i=0;i<row;i++){
        for(int j=0;j<col;j++){
            cin>>maze[i][j];
        }
    }
    vector<vector<bool>> visited(row, vector<bool>(col, false));
    int sx=0,sy=0,ex=row-1,ey=col-1;
    int best_steps=INT_MAX;
    int current_steps=1;
    bloodfill(sx,sy,ex,ey,maze,visited,row,col,best_steps,current_steps);
    
    cout << best_steps << endl;
    
    return 0;
}


/**
 5 5
 .....
 .###.
 .#...
 .#.#.
 ...#.
 
 answer: 9
 
 5 5
 .....
 .###.
 .###.
 .#...
 ...#.
 
 answer: 9
 
 */
