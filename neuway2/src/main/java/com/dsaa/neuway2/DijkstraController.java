package com.dsaa.neuway2;

import org.springframework.web.bind.annotation.*;

import com.dsaa.neuway2.DTOs.DijkstraRequest;
import com.dsaa.neuway2.DTOs.DijkstraResult;

import java.util.*;

@RestController
@RequestMapping("/dijkstra")
public class DijkstraController {

    //mapping (neu map)
    private Map<String, List<MainDijkstrasAlgo.Edge>> neuMap = new HashMap<>();

    public DijkstraController() {
        MainDijkstrasAlgo.addEdge(neuMap, "GATE", "MAIN BLG", 260);
        MainDijkstrasAlgo.addEdge(neuMap, "GATE", "IS BLG", 350);
        MainDijkstrasAlgo.addEdge(neuMap, "GATE", "LIBRARY", 250);
        MainDijkstrasAlgo.addEdge(neuMap, "GATE", "SOM", 500);
        MainDijkstrasAlgo.addEdge(neuMap, "MAIN BLG", "LIBRARY", 190);
        MainDijkstrasAlgo.addEdge(neuMap, "MAIN BLG", "SECRET GARDEN", 100);
        MainDijkstrasAlgo.addEdge(neuMap, "MAIN BLG", "IS BLG", 180);
        MainDijkstrasAlgo.addEdge(neuMap, "LIBRARY", "SECRET GARDEN", 80);
        MainDijkstrasAlgo.addEdge(neuMap, "IS BLG", "SOM", 200);
        MainDijkstrasAlgo.addEdge(neuMap, "IS BLG", "PSB", 350);
    }

    @GetMapping
    public DijkstraResult computePathGet(@RequestParam String start, @RequestParam String destination) {
        MainDijkstrasAlgo.Result result = MainDijkstrasAlgo.computeDijkstra(neuMap, start.toUpperCase(), destination.toUpperCase());

        return new DijkstraResult(result.path, result.totalDistance, result.operations);
    }

    @PostMapping
    public DijkstraResult computePathPost(@RequestBody DijkstraRequest request) {
        MainDijkstrasAlgo.Result result = MainDijkstrasAlgo.computeDijkstra(neuMap, request.getStart().toUpperCase(), request.getDestination().toUpperCase());
        return new DijkstraResult(result.path, result.totalDistance, result.operations);
    }

    @GetMapping("/all-paths")
    public List<AllPathsDFS.PathResult> allPathsGet(
            @RequestParam String start,
            @RequestParam String destination) {

        String s = start.toUpperCase().trim();
        String d = destination.toUpperCase().trim();

        return AllPathsDFS.getAllPaths(neuMap, s, d);
    }

    @PostMapping("/all-paths")
    public List<AllPathsDFS.PathResult> allPaths(
            @RequestBody DijkstraRequest request
    ) {
        return AllPathsDFS.getAllPaths(
                neuMap,
                request.getStart().toUpperCase(),
                request.getDestination().toUpperCase()
        );
    }

    public List<String> options = Arrays.asList("GATE", "IS BLG", "LIBRARY", "MAIN BLG", "PSB", "SECRET GARDEN","SOM");

    @PostMapping("/search")
    public List<String> searchPost(@RequestBody Map<String, String> body) {
    String prefix = body.getOrDefault("prefix", "");
    if (prefix.isEmpty()) return Collections.emptyList();
    Collections.sort(options);
    return BinarySearchLocation.binarySearch(options, prefix);
    }
}

