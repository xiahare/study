//
//  main.cpp
//  q02_find_suffix
//
//  Created by Lei Xia on 2/20/24.
//

#include <iostream>
using namespace std;
const int maxn = 10;

struct node {
    char data;
    struct node *left;
    struct node *right;
};

struct node *newNode() {
    char data = '0';
    struct node *node = (struct node *)malloc(sizeof(struct node));
    node->data = data;
    node->left = NULL;
    node->right = NULL;
    return (node);
}

string sp;
string sm;
string ss;

node *root = newNode();

void dfs( string a, string b, node *parent){
    if(a.empty()) return;
    
    char data = a.at(0);
    
    parent->data = data;
    
    long mid = b.find(data);
    
    if( mid == 0 ){
        // no left
    } else {
        string bleft = b.substr(0,mid);
        string aleft = a.substr(1,mid);
        
        parent->left = newNode();
        dfs(aleft,bleft,parent->left);
    }
    
    if( mid+1 == b.size() ){
        // no right
    } else {
        string bright = b.substr(mid+1);
        string aright = a.substr(mid+1);
        
        parent->right = newNode();
        dfs(aright,bright,parent->right);
    }
    
}

void output_suffix(node *nod){
    if(nod!=NULL){
        output_suffix(nod->left);
        output_suffix(nod->right);
        cout << nod->data;
    }
    
}
int main(int argc, const char * argv[]) {
    
    cin >> sp;
    cin >> sm;
    
    
    dfs(sp,sm,root);
    
    // suffix output
    output_suffix(root);
    cout << endl;
    
    return 0;
}

/**
 Test 1:
 -- input --
 ABDEC
 DBEAC
 
 -- output --
 DEBCA
 Test 2:
 
 -- input --
 ABDECF
 DBEAFC
 
 -- output --
 DEBFCA
 */

