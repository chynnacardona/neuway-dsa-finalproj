package com.dsaa.neuway2;

import java.util.*;

public class MainDijkstrasAlgo {
static class Edge { 
    //Helper class for edges 
    //Edges -- the connections between nodes (or vertices) in a graph, 
    // and they represent the cost or weight of traveling between them

        String target; //the edge that leads to this location (curNode points to target)
        int travelDistance; //the weight (cost) of travelling through this edge 

        Edge(String target, int travelDistance) {
            this.target = target;
            this.travelDistance = travelDistance;
        }
    }

    public static class Result { //holds the final output
        String path; 
        public int totalDistance;
        int operations; 
        // this is to count no. of to determine how many steps algo is executed
        //for analysis rather than solving a problem or something in the program

        Result (String path, int totalDistance, int operations) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.operations = operations;
        }
    }  

    //dijkstra method
    static Result computeDijkstra(Map<String, List<Edge>> neuMap, String start, String destination) {
        Map<String, Integer> distances = new HashMap<>();
        //number of operations performed
        Map<String, String> previous = new HashMap<>();
        //keeps predecessor to reconstruct the path later

        PriorityQueue<Edge> distNode = new PriorityQueue<>(Comparator.comparingInt(e -> e.travelDistance));
        //returns the node w smallest distance
        //Sots based on travelDistance (from class Edge)
        int operations = 0;

        Set<String> allNodes = new HashSet<>();
        //map.Entry gets both Key and Value 
        for (Map.Entry<String, List<Edge>> entry : neuMap.entrySet()) { //neuMap contains adjaceny set
            allNodes.add(entry.getKey());
            for (Edge e : entry.getValue()) {
                allNodes.add(e.target);
            }
        }

        // Initialize distances to infinity
        for (String node : allNodes) {
            distances.put(node, Integer.MAX_VALUE);
        }

        distances.put(start, 0); //node initialization start = 0
        distNode.add(new Edge(start, 0)); //add start to pq 

        if (start.equals(destination)) { //Check if (start to destination) it loops to itself
            List<Edge> selfLoop = neuMap.getOrDefault(start, new LinkedList<>());
            for (Edge e : selfLoop) { 
                if (e.target.equals((start))) {
                    return new Result (start + "->" + destination, e.travelDistance, operations + 1);
                } 
            } 

            return new Result (start, 0, operations+1);
        }

        //While pq is not empty, get edge with smallest distance
        while (!distNode.isEmpty()) { 
            Edge current = distNode.poll();
            operations++; 

            //skip outaded entires
            if (current.travelDistance > distances.get(current.target)) continue;

            //get neighbors of current nodes
            List<Edge> neighbors = neuMap.getOrDefault(current.target, new LinkedList<>());

            //Relaxation d[v] = d[u] + c(u,v)
            /*
            If (d[u] + c(u,v) < d[v]) then update d[v]
            u -> the current node we are processing
            v -> one of its neighbors
            d[u] -> the current shortest distance from the start to u
            c(u, v) -> the cost of edge from u (weight) to v (distance)
            d[v] -> the current shortest known distnce from the start to v
            */
            for (Edge neighbor : neighbors) {
                int newDist = distances.get(current.target) + neighbor.travelDistance;

                if (newDist < distances.get(neighbor.target)) { 
                    distances.put(neighbor.target, newDist);
                    previous.put(neighbor.target, current.target);
                    distNode.add(new Edge(neighbor.target, newDist));
                }
            }
        }
        // After Dijkstra finishes: Check if destination is unreachable
        if (!distances.containsKey(destination) || distances.get(destination) == Integer.MAX_VALUE) {
            return new Result("No path", Integer.MAX_VALUE, operations);
        }

        // Build path: Reconstruct path by going backwards using the previous map.
        // since we are using linked list (reverse)
        LinkedList<String> pathNodes = new LinkedList<>();
        String currentNode = destination;

        while (currentNode != null && previous.containsKey(currentNode)) {
            pathNodes.addFirst(currentNode);
            currentNode = previous.get(currentNode);

        }
        // Add start node
        pathNodes.addFirst(start);

        // Convert to string
        // Convert path list to string & return result 
        String pathString = String.join(" -> ", pathNodes);

        return new Result(pathString, distances.get(destination), operations);
    }
    
    static void addEdge(Map<String, List<Edge>> map, String from, String to, int dist) {
        map.putIfAbsent(from, new LinkedList<>());
        map.get(from).add(new Edge(to, dist));
        
        //Reversed paths
        map.putIfAbsent(to, new LinkedList<>());
        map.get(to).add(new Edge(from, dist));
    }

    
}
