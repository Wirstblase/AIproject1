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

        //added a steps limit so that it doesn't get stuck
        int stepsLimit = 10000;
        int steps = 0;
        // Loop until the unexplored queue is empty
        while (!unexplored.isEmpty() && steps < stepsLimit) {
            // Get the node with the lowest estimated total cost
            Node current = unexplored.poll();
            steps++;

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
    public static int[][] mergeMazeAndSolution(Maze maze, int[][] solution, int[][] explored) {
        // Get the dimensions of the maze
        int height = maze.getMaze().length;
        int width = maze.getMaze()[0].length;

        // Create a new 2D array to store the merged maze and solution
        int[][] mergedMaze = new int[height][width];

        // Loop through the maze and the solution
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                /*// If the current position is an obstacle, set the merged maze value to "1"
                if (maze.getMaze()[i][j] == 1) {
                    mergedMaze[i][j] = 1;
                } else if (maze.getMaze()[i][j] == -1) {
                    mergedMaze[i][j] = 3;
                    //sets the start point to be blue aswell
                } else if(explored[i][j] == 3){
                    mergedMaze[i][j] = 3;
                } else if(solution[i][j] == 4){
                    mergedMaze[i][j] = 3;
                }
                // Otherwise, set the merged maze value to the solution value
                else {
                    mergedMaze[i][j] = solution[i][j];
                }*/

                if(maze.getMaze()[i][j] == 1){
                    mergedMaze[i][j] = 1;
                } else {
                    mergedMaze[i][j] = 0;
                }

                if(explored[i][j] == 4){
                    mergedMaze[i][j] = 4;
                }

                if(maze.getMaze()[i][j] == -1){
                    mergedMaze[i][j] = 3;
                }

                 if(solution[i][j] == 3){
                    mergedMaze[i][j] = 3;
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

                else if(maze[i][j] == 3){
                    gc.setFill(Color.BLUE);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
                // If the current cell is part of the solution path, draw a blue rectangle
                else if (maze[i][j] == 4) {
                    gc.setFill(Color.GRAY);
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

    //method that takes two int[][] and counts how many times the value 3 overlaps between them
    public static int countOverlap(int[][] explored, int[][] solution){
        int count = 0;
        for(int i = 0; i < explored.length; i++){
            for(int j = 0; j < explored[0].length; j++){
                if(explored[i][j] == 4 && solution[i][j] == 3){
                    count++;
                }
            }
        }
        return count;
    }

    int countSolutionLength(int[][] solution){
        int count = 0;
        for(int i = 0; i < solution.length; i++){
            for(int j = 0; j < solution[0].length; j++){
                if(solution[i][j] == 3){
                    count++;
                }
            }
        }
        return count;
    }

    //method to set all values of int[][] to 0
    public static void resetArray(int[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                array[i][j] = 0;
            }
        }
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoadButtonClick() {
        welcomeText.setText("maze generated");
        maze = new Maze(new int[30][30]);

        int overlap = 0;
        int count = 0;

        while(count < 2) {
        while(overlap+1 != count){

            maze.clearMaze();

            maze = populateMaze(maze);

            int [][] sol = solveMaze(maze);

            AStarMazeSolver solver = new AStarMazeSolver(maze.getMaze(), 20, maze.findEnd());
            int [][] expl = solver.solve();

            count = countSolutionLength(sol);
            overlap = countOverlap(expl, sol);

            //System.out.println(overlap+1);
            //System.out.println(count);

        }
        }

        showMaze(canmvas, maze.getMaze());

    }

    @FXML
    protected void onHelloButtonClick() {
        solution = solveMaze(maze);
        AStarMazeSolver solver = new AStarMazeSolver(maze.getMaze(), 20, maze.findEnd());

        int [][] explored = solver.solve();

        //solver.markPath(solver.finalState);
        //solution = solver.getMaze();
        //showMaze(canmvas, explored);
        //printMaze(solution);

        welcomeText.setText("maze solved");
        //printMaze(solution);

        solvedMaze = mergeMazeAndSolution(maze, solution, explored);

        showMaze(canmvas, solvedMaze);
    }


}