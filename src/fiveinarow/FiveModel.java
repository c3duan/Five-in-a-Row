package fiveinarow;

class FiveModel {
    public static final int EMPTY = 0;
    private static final int PLAYER1 = 1;
    public static final int TIE = -1;

    private int maxRows;
    private int maxCols;

    private int[][] board;
    private int lastR;
    private int lastC;
    private int nextPlayer;
    private int moves = 0;

    public FiveModel(int rows, int cols) {
        maxRows = rows;
        maxCols = cols;
        board = new int[maxRows][maxCols];
        reset();
    }

    public int getNextPlayer() {
        return this.nextPlayer;
    }

    public int getPlayerAt(int r, int c) {
        return board[r][c];
    }

    public void reset() {
        for (int r = 0; r < maxRows; r++) {
            for (int c = 0; c < maxCols; c++) {
                board[r][c] = EMPTY;
            }
        }
        moves = 0;
        nextPlayer = PLAYER1;
        move(maxCols / 2, maxRows / 2);
    }

    public void undo() {
        board[lastR][lastC] = EMPTY;
        nextPlayer =  3 - nextPlayer;
        moves =- 1;
    }

    public void move(int r, int c) {
        assert board[r][c] == EMPTY;
        board[r][c] = nextPlayer;
        nextPlayer = 3 - nextPlayer;
        moves++;
        lastR = r;
        lastC = c;
    }

    private boolean count5(int r, int dr, int c , int dc) {
        int player = board[r][c];
        for (int i = 0; i < 5; i++) {
            if (r + dr * i >= maxRows || r + dr * i < 0 || c + dc * i >= maxCols || c + dc * i < 0) {
                return false;
            }
            if (board[r + dr * i][c + dc * i] != player) {
                return false;
            }
        }
        return true;
    }

    /*public int computerTurn(int depth) {
        return ai(this.board, depth);
    }*/

    public int[][] getCurrentCopyBoard() {
        int[][]copyBoard = new int[this.board.length][this.board[0].length];
        for(int i = 0; i < this.board.length;  i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                copyBoard[i][j] = this.board[i][j];
            }
        }
        return copyBoard;
    }


    public int getGameStatus() {
        int row, col;

        for (row = 0; row < maxRows; row++) {
            for (col = 0; col < maxCols; col++) {
                int p = board[row][col];
                if (p != EMPTY) {
                    if (row < maxRows - 4) {
                        if (count5(row, 1, col, 0)) {
                            return p;
                        }
                    }

                    if (col < maxCols - 4) {
                        if (count5(row, 0, col, 1)) {
                            return p;
                        }
                        if (row < maxRows - 4) {
                            if (count5(row, 1, col, 1)) {
                                return p;
                            }
                        }
                    }

                    if (col > 3 && row < maxRows - 4) {
                        if (count5(row, 1, col, -1)) {
                            return p;
                        }
                    }
                } // end of wasn't empty
            } // end for Row
        } // end for col

        if (moves == maxRows * maxCols) {
            return TIE;
        }
        else {
            return 0;
        }
    }
}

