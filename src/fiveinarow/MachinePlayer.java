package fiveinarow;

public class MachinePlayer implements AI {
    private FiveModel game = new FiveModel(9, 9);
    int turn = 0;

    public MachinePlayer(int turn) {
        this.turn = turn;
    }

    @Override
    public int heuristic(int[][] board) {
        int result = 0;

        // check horizontals
        for(int i=0; i<9; i++) {
            for(int j=0; j<=9-5; j++) {
                if((board[i][j] != 2) && (board[i][j + 1] != 2) && (board[i][j + 2] != 2) && (board[i][j + 3] != 2) && (board[i][j + 4] != 2)) {
                    result++;
                }
                if((board[i][j] != 1) && (board[i][j + 1] != 1) && (board[i][j + 2] != 1) && (board[i][j + 3] != 1) && (board[i][j + 4] != 1)) {
                    result--;
                }
            }
        }

        //check verticals
        for(int i=0; i<=9-5; i++) {
            for (int j = 0; j < 9; j++) {
                if ((board[i][j] != 2) && (board[i + 1][j] != 2) && (board[i + 2][j] != 2) && (board[i + 3][j] != 2) && (board[i + 4][j] != 2)) {
                    result++;
                }
                if ((board[i][j] != 1) && (board[i + 1][j] != 1) && (board[i + 2][j] != 1) && (board[i + 3][j] != 1) && (board[i + 4][j] != 1)) {
                    result--;
                }
            }
        }

        //check diagonal
        for(int i=0; i<=9-5; i++) {
            for (int j = 0; j < 9-5; j++) {
                if ((board[i][j] != 2) && (board[i + 1][j + 1] != 2) && (board[i + 2][j + 2] != 2) && (board[i + 3][j + 3] != 2) && (board[i + 4][j + 4] != 2)) {
                    result++;
                }
                if ((board[i][j] != 1) && (board[i - 1][j - 1] != 1) && (board[i - 2][j - 2] != 1) && (board[i - 3][j - 3] != 1) && (board[i - 4][j - 4] != 1)) {
                    result--;
                }
            }
        }

        return result;
    }

    @Override
    public int miniMax(int[][] board, int depth, int turn) {
        int col, best;
        int n;
        int player = 0;
        int e = game.getGameStatus();
        if (e != 0) {
            if (e == -1)
                return 0;
            if (e == turn)
                return 10000;
            else
                return -10000;
        }


        if(depth==0)
            return ((turn==1) ? this.heuristic(board) : -this.heuristic(board));


        best = -10000;

        for(col=0; col < 9; col++)     //check every move
            if(board[9-1][col]==0) {   //make sure column isn't empty
                game.move(col, turn);
                n = miniMax(board, depth-1, 3-turn);
                if(turn==1) {
                    if ( -n > best ) best = -n;
                } else { //turn==2
                    if ( -n > best ) best = -n;
                }
                game.undo();
            }

        return best;
    }

    @Override
    public int ai(int[][] board, int depth) {
        int col, move = 0;
        int n, val = -10000-1;

        for(col=0; col < 9; col++)
            if(board[9-1][col]==0) {
                game.move(col, 2);
                n = miniMax(board, depth, 1);
                if ( -n > val ) {
                    val = -n;
                    move = col;
                }
                game.undo();
            }

        return move;
    }

    @Override
    public int possible(int[][] board, int column) {
        int i;
        int occupied = 0;

        if(column > board[0].length || column < 0 ) {
            return 0;
        }

        for(i = 0; i < board.length; i++) {
            if(board[i][column] != 0)
                occupied++;
        }

        if(occupied == board.length) {
            return 0;
        }

        else
            return 1;
    }
}
