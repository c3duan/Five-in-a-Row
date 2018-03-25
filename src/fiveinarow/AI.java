package fiveinarow;

public interface AI {
    public int heuristic(int board[][]);
    public int miniMax(int board[][], int depth, int turn);
    public int ai(int board[][], int depth);
    public int possible(int board[][], int column);
}
