package com.s00flea.aiproject1;

import java.util.ArrayList;
import java.util.List;

public class State {
    private int x;
    private int y;
    private int g;
    private List<State> path;

    public State(int x, int y) {
        this.x = x;
        this.y = y;
        this.g = 0;
        this.path = new ArrayList<>();
        this.path.add(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public List<State> getPath() {
        return path;
    }

    public void setPath(List<State> path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof State)) {
            return false;
        }
        State other = (State) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }
}
