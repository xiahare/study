package com.xl.study.sameples.algorithm;

import java.util.Stack;

public class Dijkstra {
    public static void main(String[] args) {
        int graph[][] = new int[][] { { 0, 2, 1, 0, 0, 0},
                                      { 2, 0, 7, 0, 8, 4}, 
                                      { 1, 7, 0, 7, 0, 3}, 
                                      { 0, 0, 7, 0, 8, 4}, 
                                      { 0, 8, 0, 8, 0, 5}, 
                                      { 0, 4, 3, 4, 5, 0} };
        Result result = findShortestDistance(graph,0) ;
        result.print();
    }

    private static Result findShortestDistance(int[][] graph, int source){
        int num = graph.length;
        int[] dist = new int[num];
        int[] visited = new int[num];
        int[] preNode = new int[num];

        // init
        for (int i = 0; i < num; i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = 0;
            preNode[i] = -1;
        }
        dist[0] = 0;

        for (int i = 0; i < num; i++) {

            int curMinNode = findMinNode(dist, visited);
            visited[curMinNode] = 1;
            for (int j = 0; j < num; j++) {
                if( visited[j]==0 && graph[curMinNode][j]>0 ){
                    int newDist = dist[curMinNode] + graph[curMinNode][j];
                    if( newDist<dist[j] ){
                        dist[j]=newDist;
                        preNode[j]=curMinNode;
                    }
                }
            }
        }

        Result result = new Result();
        result.dist=dist;
        result.preNode=preNode;

        return result;
    }

    private static int findMinNode(int[] dist, int[] visited){
        int minIndex = -1;
        for (int i = 0; i < dist.length; i++) {
            if( visited[i]==0 ){
                if( minIndex==-1 ){
                    minIndex=i;
                }
                if ( dist[minIndex]>dist[i] ) {
                    minIndex = i;
                }
            }

        }
        return minIndex;
    }



}

class Result {
    static public int[] dist;
    static public int[] preNode;

    public void print(){
        for (int i = 0; i < dist.length; i++) {
            System.out.print(String.format("NODE[%d] - Dist : {%2d } ",i, dist[i]));
            System.out.print(" | Path : ");
            printPath(preNode,i);
            System.out.println();
        }
    }

    private static void printArray(int[] dist){
        for (int i = 0; i < dist.length; i++) {
            System.out.print("| "+dist[i]);
        }
        System.out.println();
    }

    private static void printPath(int[] preNode, int node){

        Stack<Integer> path = new Stack<Integer>();
        int curNode = node;
        while(preNode[curNode]!=-1){
            path.push(preNode[curNode]);
            curNode = preNode[curNode];
        }

        while (!path.isEmpty()){
            System.out.print(String.format("[%d]->",path.pop()));
        }

        System.out.print(String.format("[%d]", node));

    }
}