package com.s00flea.aiproject1;

public class Maze {
    // The maze as a 2D array of integers
    private int[][] maze;

    // Constructor that initializes the maze
    public Maze(int[][] maze) {
        this.maze = maze;
    }

    // Getter method that returns the maze
    public int[][] getMaze() {
        return maze;
    }

    // Setter method that sets the value of a cell in the maze
    public void setCell(int x, int y, int value) {
        maze[x][y] = value;
    }

    public State findEnd() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == -2) {
                    return new State(i, j);
                }
            }
        }
        return null;
    }

    // Method that prints the maze to the console
    public void printMaze() {
        for (int[] row : maze) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}