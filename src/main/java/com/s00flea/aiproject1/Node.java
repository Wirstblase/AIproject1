package com.s00flea.aiproject1;

// Class to represent a position in the maze
class Node implements Comparable<Node> {
    int x;
    int y;
    int cost;
    int estimatedTotalCost;
    Node parent;

    // Constructor
    public Node(int x, int y, int cost, int estimatedTotalCost, Node parent) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.estimatedTotalCost = estimatedTotalCost;
        this.parent = parent;
    }

    // Method to compare nodes based on their estimated total cost
    public int compareTo(Node other) {
        return this.estimatedTotalCost - other.estimatedTotalCost;
    }

}