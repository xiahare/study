//
//  main.cpp
//  q03_common_tree
//
//  Created by Lei Xia on 2/21/24.
//

#include <stdlib.h>

#include <iostream>

using namespace std;

struct node {
    char data;
    struct node *left;
    struct node *right;
};

// New node creation
struct node *newNode(char data) {
    struct node *node = (struct node *)malloc(sizeof(struct node));
    
    node->data = data;
    
    node->left = NULL;
    node->right = NULL;
    return (node);
}

// Traverse Preorder
void traversePreOrder(struct node *temp) {
    if (temp != NULL) {
        cout << temp->data;
        traversePreOrder(temp->left);
        traversePreOrder(temp->right);
    }
}

// Traverse Inorder
void traverseInOrder(struct node *temp) {
    if (temp != NULL) {
        traverseInOrder(temp->left);
        cout << temp->data;
        traverseInOrder(temp->right);
    }
}

// Traverse Postorder
void traversePostOrder(struct node *temp) {
    if (temp != NULL) {
        traversePostOrder(temp->left);
        traversePostOrder(temp->right);
        cout << temp->data;
    }
}

int main() {
    struct node *root = newNode('A');
    root->left = newNode('B');
    root->right = newNode('C');
    root->left->left = newNode('D');
    root->left->right = newNode('E');
    root->right->left = newNode('F');
    
    cout << "preorder traversal: ";
    traversePreOrder(root);
    cout << "\nInorder traversal: ";
    traverseInOrder(root);
    cout << "\nPostorder traversal: ";
    traversePostOrder(root);
    
    cout << endl;
}
