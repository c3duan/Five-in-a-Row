package fiveinarow;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Five extends JApplet {
    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final Color[] PLAYER_COLOR = {null, Color.BLACK, Color.WHITE};
    private static final String[] PLAYER_NAME = {null, "BLACK", "WHITE"};

    private GameBoard  boardDisplay;
    private JTextField statusField = new JTextField();
    private FiveModel gameLogic = new FiveModel(ROWS, COLS);

    public static void main(String[] args) {
        JFrame window = new JFrame("Five in a Row");
        window.setContentPane(new Five());
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    public Five() {
        JButton newGameButton = new JButton("New Game");
        JButton undoButton = new JButton("Undo");

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(newGameButton);
        controlPanel.add(undoButton);

        boardDisplay = new GameBoard();

        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(boardDisplay, BorderLayout.CENTER);
        this.add(statusField, BorderLayout.SOUTH);

        newGameButton.addActionListener(new NewGameAction());
        undoButton.addActionListener(new UndoAction());
    }

    private class NewGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gameLogic.reset();
            boardDisplay.repaint();
        }
    }

    private class UndoAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gameLogic.undo();
            boardDisplay.repaint();
        }
    }

    class GameBoard extends JComponent implements MouseListener {
        private static final int CELL_SIZE = 30;
        private static final int WIDTH = COLS * CELL_SIZE;
        private static final int HEIGHT = ROWS * CELL_SIZE;

        public GameBoard() {
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            this.addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, WIDTH, HEIGHT);
            g2.setColor(Color.BLACK);

            for (int r = 1; r < ROWS; r++) {
                g2.drawLine(0, r * CELL_SIZE, WIDTH, r * CELL_SIZE);
            }

            for (int c = 1; c < COLS; c++) {
                g2.drawLine(c * CELL_SIZE, 0, c * CELL_SIZE, HEIGHT);
            }

            for (int r = 0; r < ROWS; r++) {
                for (int c  = 0; c < COLS; c++) {
                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;
                    int who = gameLogic.getPlayerAt(r, c);
                    if (who != gameLogic.EMPTY) {
                        g2.setColor(PLAYER_COLOR[who]);
                        g2.fillOval(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                    }
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            int col, row;
            if (e.getX() < 30) {
                col = 0;
            }
            else if (e.getX() % 30 == 0) {
                col = e.getX() / 30 - 1;
            }
            else {
                col = e.getX() / 30;
            }

            if (e.getY() < 30) {
                row = 0;
            }
            else if (e.getY() % 30 == 0) {
                row = e.getY() / 30 - 1;
            }
            else {
                row = e.getY() / 30;
            }

            boolean gameOver = gameLogic.getGameStatus() != 0;
            int currentOccupant = gameLogic.getPlayerAt(row, col);

            if (!gameOver && currentOccupant == gameLogic.EMPTY) {
                gameLogic.move(row, col);
                switch (gameLogic.getGameStatus()) {
                    case 1:
                        statusField.setText("BLACK WINS");
                        break;

                    case 2:
                        statusField.setText("WHITE WINS");
                        break;

                    case FiveModel.TIE:
                        statusField.setText("TIE GAME");
                        break;

                    default:
                        statusField.setText(PLAYER_NAME[gameLogic.getNextPlayer()] + " to play");
                }
            }

            else {
                Toolkit.getDefaultToolkit().beep();
            }

            this.repaint();
        }

        public void mouseClicked(MouseEvent e){}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}
