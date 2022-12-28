package com.xl.study.sameples.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetectCycle {
    // Function to detect cycle in a directed graph.
    public boolean isCyclic(int V, ArrayList<ArrayList<Integer>> adj) {
        // code here
        Set<Integer> visitedSet = new HashSet<Integer>();
        Set<Integer> noCycleSet = new HashSet<Integer>();

        for (int i=0; i<V; i++) {
            if(noCycleSet.contains(i)){
                continue;
            }
            if( checkDuplicated(visitedSet,noCycleSet,i,adj) ){
                return true;
            }
        }

        return false;
    }
    private boolean checkDuplicated(Set<Integer> visitedSet, Set<Integer> noCycleSet, Integer visitingNode, ArrayList<ArrayList<Integer>> adj){
        if(noCycleSet.contains(visitingNode)){
            return false;
        }
        if(visitedSet.contains(visitingNode)){
            return true;
        } else {
            visitedSet.add(visitingNode);
            for (Integer nextNode:adj.get(visitingNode)) {
                if(noCycleSet.contains(nextNode)){
                    continue;
                }
                // recurse
                if( checkDuplicated(visitedSet,noCycleSet,nextNode,adj) ){
                    return true;
                }
            }
            visitedSet.remove(visitingNode);
            noCycleSet.add(visitingNode);
            return false;
        }
    }
}
