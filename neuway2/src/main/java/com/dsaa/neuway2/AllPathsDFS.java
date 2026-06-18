package com.dsaa.neuway2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllPathsDFS {
    //Map graph -- neuMap (adjacency List)
    public static class PathResult {
        public String path;
        public int distance;

        public PathResult(String path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    //CONTROLLER-FRIENDLY METHOD
    public static List<PathResult> getAllPaths(
            Map<String, List<MainDijkstrasAlgo.Edge>> graph,
            String start,
            String destination
    ) {
        List<PathResult> results = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();

        dfsAllPaths(graph, start, destination, visited, path, 0, results);
        insertionSortByDistance(results);
        return results;
    }

    static void dfsAllPaths(Map<String, List<MainDijkstrasAlgo.Edge>> graph,
                        String current, //node the DFS is currently visiting 
                        String destination, //target node we want to reach
                        Set<String> visited, //keeps track of nodes already in current path
                        List<String> path, //sequence of nordes from start to target destination
                        //List<List<String>> allPaths, //stores copies of every complete path
                        //Map<List<String>, Integer> allDist, //stores (allPaths, totaldistance)
                        int curDist,
                        List<PathResult> results) {

    visited.add(current); //removes redundancy o visited nodes
    path.add(current); //append to ongoiung path sequence

    // If destination reached, store copy of the path
    if (current.equals(destination)) {
        results.add(new PathResult(String.join(" -> ", path), curDist));
    } else {
        // Explore neighbors
        for (MainDijkstrasAlgo.Edge e : graph.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(e.target)) {
                dfsAllPaths(graph, e.target, destination, visited, path, curDist + e.travelDistance, results);
            }
        }
    }

    // BACKTRACK 
    visited.remove(current);
    path.remove(path.size() - 1);
    }

    //Insertion Sort (distance)
    static void insertionSortByDistance(List<PathResult> list) {
        for (int i = 1; i < list.size(); i++) {
            PathResult key = list.get(i);
            int j = i - 1;

            // Move elements greater than key.distance one step ahead
            while (j >= 0 && list.get(j).distance > key.distance) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }  
    }
}