package com.s00flea.aiproject1;

import java.util.*;

public class AStarMazeSolver {
    private int[][] maze;
    private PriorityQueue<State> queue;
    private Queue<State> visited;
    private int maxStates;

    public State finalState;

    public AStarMazeSolver(int[][] maze, int maxStates, State goal) {
        this.maze = maze;
        this.queue = new PriorityQueue<>(maxStates, new StateComparator(goal));
        this.visited = new LinkedList<>();
        this.maxStates = maxStates;
    }

    public int[][] solve() {
        // Create a new 2D array to keep track of the explored path
        int[][] explored = new int[maze.length][maze[0].length];

        // Find the starting point
        State start = findStart();
        // Add the starting point to the queue
        queue.offer(start);
        // While the queue is not empty
        while (!queue.isEmpty()) {
            // Get the state with the highest priority
            State current = queue.poll();
            // Mark the current state as explored
            int x = current.getX();
            int y = current.getY();
            explored[x][y] = 4;
            // If the current state is the goal
            if (isGoal(current)) {
                // Mark the path in the maze
                markPath(current);
                return maze;
            }
            // If the visited queue is full
            if (visited.size() == maxStates) {
                // Remove the oldest state from the visited queue
                visited.poll();
            }
            // Add the current state to the visited queue
            visited.offer(current);
            // Generate the next states
            List<State> nextStates = generateNextStates(current);
            // For each next state
            for (State nextState : nextStates) {
                // If the next state is already in the visited queue
                if (visited.contains(nextState)) {
                    // Skip this state
                    continue;
                }
                // If the queue is full
                if (queue.size() == maxStates) {
                    // Remove the state with the lowest priority
                    queue.poll();
                }
                // Add the next state to the queue
                queue.offer(nextState);
                finalState = nextState;
            }
        }
        // If the algorithm reaches this point, it means that no solution was found
        return explored;
    }

    public int[][] getMaze() {
        return maze;
    }

    /*public void markPath(){
        State current = finalState;
        while (current != null) {
            int x = current.getX();
            int y = current.getY();
            maze[x][y] = 3;
            current = current.getParent();
        }
    }*/

    public void markPath(State finalState) {
        //List<State> path = current.getPath();
        for (State state : finalState.getPath()) {
            int x = state.getX();
            int y = state.getY();
            maze[x][y] = 3;
        }
    }

    private State findStart() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == -1) {
                    return new State(i, j);
                }
            }
        }
        throw new IllegalStateException("Start point not found in the maze");
    }

    private boolean isGoal(State state) {
        int x = state.getX();
        int y = state.getY();
        return maze[x][y] == -2;
    }

    private List<State> generateNextStates(State current) {
        List<State> nextStates = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();
        // Check if the cell above is free
        if (x > 0 && maze[x - 1][y] == 0) {
            nextStates.add(new State(x - 1, y));
        }
        // Check if the cell to the right is free
        if (y < maze[0].length - 1 && maze[x][y + 1] == 0) {
            nextStates.add(new State(x, y + 1));
        }
        // Check if the cell below is free
        if (x < maze.length - 1 && maze[x + 1][y] == 0) {
            nextStates.add(new State(x + 1, y));
        }
        // Check if the cell to the left is free
        if (y > 0 && maze[x][y - 1] == 0) {
            nextStates.add(new State(x, y - 1));
        }
        return nextStates;
    }

    private class StateComparator implements Comparator<State> {
        private State goal;

        public StateComparator(State goal) {
            this.goal = goal;
        }

        @Override
        public int compare(State s1, State s2) {
            int f1 = s1.getG() + manhattanDistance(s1, goal);
            int f2 = s2.getG() + manhattanDistance(s2, goal);
            return Integer.compare(f1, f2);
        }

        private int manhattanDistance(State s1, State s2) {
            int x1 = s1.getX();
            int y1 = s1.getY();
            int x2 = s2.getX();
            int y2 = s2.getY();
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
        }
    }
}
