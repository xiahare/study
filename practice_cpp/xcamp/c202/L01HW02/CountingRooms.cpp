#include <iostream>
#include <vector>
#include <climits>

using namespace std;


bool possible(int x,int y, vector<vector<char>> &maze, vector<vector<bool>> &visited, int row, int col){
    if(x>=0&&y>=0&&x<row&&y<col&&!visited[x][y]&&maze[x][y]=='.'){
        return true;
    }
    return false;
}

/**
  * &visited : will be changed in the function
  * other parameters are not
  */
void bloodfill(int x,int y, vector<vector<char>> &maze, vector<vector<bool>> &visited, int row, int col){
    if( !possible(x,y,maze,visited,row,col) ){
        return ;
    }

    visited[x][y]=true;
    
    bloodfill(x+1,y,maze,visited,row,col);
    bloodfill(x-1,y,maze,visited,row,col);
    bloodfill(x,y+1,maze,visited,row,col);
    bloodfill(x,y-1,maze,visited,row,col);

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
    int rooms=0;
    
    for(int i=0;i<row;i++){
        for(int j=0;j<col;j++){
            if( maze[i][j]=='.' && !visited[i][j] ){
                rooms++;
                bloodfill(i,j,maze,visited,row,col);
            }
        }
    }
    
    cout << rooms << endl;
    
    return 0;
}


/**
5 8
########
#..#...#
####.#.#
#..#...#
########

answer:
3
 
 */
