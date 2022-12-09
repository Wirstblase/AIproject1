package com.s00flea.aiproject1;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.*;

public class HelloController {

    public static Maze populateMaze(Maze maze) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Create a random number generator
        Random rand = new Random();

        // Add obstacles to the maze (represented by the value 1)
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (rand.nextDouble() < 0.3) {
                    maze.setCell(i, j, 1);
                }
            }
        }

        // Add a point of start to the maze (represented by the value -1)
        int startX = rand.nextInt(height);
        int startY = rand.nextInt(width);
        maze.setCell(startX, startY, -1);

        try {
            maze.setCell(startX - 1, startY, 0);
            maze.setCell(startX + 1, startY, 0);
            maze.setCell(startX - 1, startY - 1, 0);
            maze.setCell(startX, startY - 1, 0);
            maze.setCell(startX + 1, startY - 1, 0);
            maze.setCell(startX - 1, startY + 1, 0);
            maze.setCell(startX, startY + 1, 0);
            maze.setCell(startX + 1, startY + 1, 0);
        } catch (Exception e) {
        }

        // Add a point of end to the maze (represented by the value -2)
        int endX = rand.nextInt(height);
        int endY = rand.nextInt(width);
        maze.setCell(endX, endY, -2);

        try {
            maze.setCell(endX - 1, endY, 0);
            maze.setCell(endX + 1, endY, 0);
            maze.setCell(endX - 1, endY - 1, 0);
            maze.setCell(endX, endY - 1, 0);
            maze.setCell(endX + 1, endY - 1, 0);
            maze.setCell(endX - 1, endY + 1, 0);
            maze.setCell(endX, endY + 1, 0);
            maze.setCell(endX + 1, endY + 1, 0);
        } catch (Exception e) {
        }

        // Return the populated maze
        return maze;
    }


    // Helper method to calculate the estimated total cost of a node
    private static int getEstimatedTotalCost(int x, int y, Maze maze) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Find the end position in the maze
        int endX = 0;
        int endY = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze.getMaze()[i][j] == -2) {
                    endX = i;
                    endY = j;
                }
            }
        }

        // Calculate the estimated total cost using the Manhattan distance
        return Math.abs(x - endX) + Math.abs(y - endY);
    }

    public static int[][] solveMaze(Maze maze) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Create a 2D array to store the solution
        int[][] solution = new int[height][width];

        // Create a priority queue to store the unexplored nodes
        PriorityQueue<Node> unexplored = new PriorityQueue<>();

        // Find the starting position in the maze
        int startX = 0;
        int startY = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze.getMaze()[i][j] == -1) {
                    startX = i;
                    startY = j;
                }
            }
        }

        // Add the starting node to the unexplored queue
        unexplored.add(new Node(startX, startY, 0, 0, null));

        // Loop until the unexplored queue is empty
        while (!unexplored.isEmpty()) {
            // Get the node with the lowest estimated total cost
            Node current = unexplored.poll();

            // Check if we have reached the end of the maze
            if (maze.getMaze()[current.x][current.y] == -2) {
                // If so, construct the solution path by following the
                // chain of parent nodes back to the starting position
                while (current.parent != null) {
                    solution[current.x][current.y] = 3;
                    current = current.parent;
                }
                break;
            }

            // Otherwise, add the current node's neighbors to the unexplored queue
            addNeighbors(current, maze, unexplored);
        }

        // Return the solution
        return solution;
    }

    /*// Method to add a node's neighbors to the unexplored queue
    public static void addNeighborsToUnexplored(Maze maze, PriorityQueue<Node> unexplored, Node current) {
        // Get the coordinates of the current node's neighbors
        int[] neighborsX = {current.x - 1, current.x + 1, current.x, current.x};
        int[] neighborsY = {current.y, current.y, current.y - 1, current.y + 1};

        // Loop through the neighbors
        for (int i = 0; i < 4; i++) {
            // Get the coordinates of the current neighbor
            int x = neighborsX[i];
            int y = neighborsY[i];

            // Check if the current neighbor is within the maze bounds
            // and not an obstacle
            if (x >= 0 && x < maze.getMaze().length && y >= 0 && y < maze.getMaze()[0].length && maze.getMaze()[x][y] != 1) {
                // If it is, add it to the unexplored queue if it hasn't been explored yet
                if (!unexplored.contains(new Node(x, y))) {
                    unexplored.add(new Node(x, y, current.cost + 1, getEstimatedTotalCost(x, y, maze), current));

                }
            }
        }
    }

    // Solve the maze using the A* algorithm
    public static int[][] solveMaze2(Maze maze) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Create a 2D array to store the solution
        int[][] solution = new int[height][width];

        // Create a priority queue to store the unexplored nodes
        PriorityQueue<Node> unexplored = new PriorityQueue<>();

        // Add the starting point to the unexplored queue
        unexplored.add(new Node(maze.getStartX(), maze.getStartY(), 0, getEstimatedTotalCost(maze.getStartX(), maze.getStartY(), maze), null));

        // Create a Node to store the current position
        Node current = null;

        // Loop until the end point is reached or the queue is empty
        while (!unexplored.isEmpty()) {
            // Get the node with the lowest estimated total cost from the queue
            current = unexplored.poll();

            // Check if the current position is the same as the previous position
        // If it is, this means that the algorithm has reached a dead end
        // and cannot move any further, so break out of the loop
            if (current.previous != null && current.x == current.previous.x && current.y == current.previous.y) {
                break;
            }

        // Add the current position to the solution
            solution[current.x][current.y] = 3;

        // Check if the current position is the end point
            if (current.x == maze.getEndX() && current.y == maze.getEndY()) {
                // If it is, construct the solution path by following the previous
                // nodes back to the starting point
                while (current.previous != null) {
                    current = current.previous;
                    solution[current.x][current.y] = 3;
                }
                break;
            }

        // Add the current node's neighbors to the unexplored queue
            addNeighborsToUnexplored(maze, unexplored, current);
        }
        return solution;
    }*/

    // Helper method to add a node's neighbors to the unexplored queue
    private static void addNeighbors(Node node, Maze maze, PriorityQueue<Node> unexplored) {
        int[][] mazeArray = maze.getMaze();
        int height = mazeArray.length;
        int width = mazeArray[0].length;

        // Check the node's neighbors (up, down, left, right)
        // and add them to the unexplored queue if they are valid
        // and not already in the queue
        if (node.x > 0 && mazeArray[node.x - 1][node.y] != 1 && !unexplored.contains(node.x - 1)) {
            unexplored.add(new Node(node.x - 1, node.y, node.cost + 1, getEstimatedTotalCost(node.x - 1, node.y, maze), node));
        }
        if (node.x < height - 1 && mazeArray[node.x + 1][node.y] != 1 && !unexplored.contains(node.x + 1)) {
            unexplored.add(new Node(node.x + 1, node.y, node.cost + 1, getEstimatedTotalCost(node.x + 1, node.y, maze), node));
        }
        if (node.y > 0 && mazeArray[node.x][node.y - 1] != 1 && !unexplored.contains(node.x)) {
            unexplored.add(new Node(node.x, node.y - 1, node.cost + 1, getEstimatedTotalCost(node.x, node.y - 1, maze), node));
        }
        if (node.y < width - 1 && mazeArray[node.x][node.y + 1] != 1 && !unexplored.contains(node.x)) {
            unexplored.add(new Node(node.x, node.y + 1, node.cost + 1, getEstimatedTotalCost(node.x, node.y + 1, maze), node));
        }
    }

    // Method to print a maze to the console
    public static void printMaze(int[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to print a maze with the solution path on top of it
    public static void printMazeWithSolution(Maze maze, int[][] solution) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Loop through the maze and the solution
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // If the current position is an obstacle, print a "1"
                if (maze.getMaze()[i][j] == 1) {
                    System.out.print("1 ");
                } else if (maze.getMaze()[i][j] == -1) {
                    System.out.print("3 ");
                }
                // If the current position is not an obstacle, print the solution value
                // (either "0" or "3" depending on whether it is part of the solution path)
                else {
                    System.out.print(solution[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    // Method to merge the populated maze and the solution into a single array
    public static int[][] mergeMazeAndSolution(Maze maze, int[][] solution) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Create a new 2D array to store the merged maze and solution
        int[][] mergedMaze = new int[height][width];

        // Loop through the maze and the solution
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // If the current position is an obstacle, set the merged maze value to "1"
                if (maze.getMaze()[i][j] == 1) {
                    mergedMaze[i][j] = 1;
                } else if (maze.getMaze()[i][j] == -1) {
                    mergedMaze[i][j] = 3;
                    //sets the start point to be blue aswell
                }
                // Otherwise, set the merged maze value to the solution value
                else {
                    mergedMaze[i][j] = solution[i][j];
                }
            }
        }

        // Return the merged maze
        return mergedMaze;
    }

    @FXML
    private Canvas canmvas;

    Maze maze;

    int[][] solution;
    int[][] solvedMaze;

    // Method to show a maze in a JavaFX Canvas object
    public static void showMaze(Canvas canvas, int[][] maze) {
        // Get the dimensions of the maze
        int height = maze.length;
        int width = maze[0].length;

        // Calculate the cell size based on the canvas dimensions
        int cellSize = (int) Math.min(canvas.getWidth() / width, canvas.getHeight() / height);

        // Create a GraphicsContext to draw on the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Loop through the maze and draw each cell
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // If the current cell is an obstacle, draw a black rectangle
                if (maze[i][j] == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
                // If the current cell is part of the solution path, draw a blue rectangle
                else if (maze[i][j] == 3) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                } else if (maze[i][j] == -1) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                } else if (maze[i][j] == -2) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
                // Otherwise, draw a white rectangle
                else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoadButtonClick() {
        welcomeText.setText("maze generated");
        maze = new Maze(new int[20][20]);
        maze = populateMaze(maze);

        showMaze(canmvas, maze.getMaze());

    }

    @FXML
    protected void onHelloButtonClick() {
        solution = solveMaze(maze);

        welcomeText.setText("maze solved");
        solvedMaze = mergeMazeAndSolution(maze, solution);

        showMaze(canmvas, solvedMaze);
    }


}