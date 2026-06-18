package com.dsaa.neuway2.DTOs;

public class DijkstraResult {
    private String path;
    private int totalDistance;
    private int operations;

    public DijkstraResult(String path, int totalDistance, int operations) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.operations = operations;
    }

    public String getPath() { return path; }
    public int getTotalDistance() { return totalDistance; }
    public int getOperations() { return operations; }
}
