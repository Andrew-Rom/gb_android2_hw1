package lec1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    private static final int DOT_PADDING = 10;
    private int panelWidth;
    private int panelHeight;
    private int cellHeight;
    private int cellWidth;
    private static final Random RANDOM = new Random();
    //    private final int HUMAN_DOT = 1;
//    private final int AI_DOT = -1;
//    private final int EMPTY_DOT = 0;
//    private int[][] field;
    private final char HUMAN_DOT = 1;
    private final char AI_DOT = 2;
    private final char EMPTY_DOT = 0;
    private char[][] field;

    private int fieldSizeY;
    private int fieldSizeX;
    private int winLen;
    private boolean gameAi;


    private int gameOverType;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

//    private static final int STATE_WIN_AI = -1;

    private static final String MSG_WIN_HUMAN = "Human won!";
    private static final String MSG_WIN_AI = "Bot won!";
    private static final String MSG_DRAW = "Nobody won!";

    private boolean isGameOver;
    private boolean isInitialized;


    Map() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
        isInitialized = false;
    }

    void startNewGame(boolean mode, int fSzX, int fSzY, int wLen) {
        fieldSizeX = fSzX;
        fieldSizeY = fSzY;
        winLen = Math.min(wLen, fSzX);
        gameAi = mode;
        System.out.printf("Game vs AI: %b;\nSize: x=%d, y=%d;\nWin Length: %d\n", mode, fSzX, fSzY, wLen);
        isGameOver = false;
        isInitialized = true;
        initMap();
        repaint();
    }

    private void initMap() {
//        field = new int[fieldSizeY][fieldSizeX];
        field = new char[fieldSizeY][fieldSizeX];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = EMPTY_DOT;
            }
        }
    }

    private void update(MouseEvent e) {
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        System.out.printf("x=%d, y=%d\n", cellX, cellY);
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        field[cellY][cellX] = HUMAN_DOT;
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) return;
        repaint();
        if (isGameOver || isInitialized) return;
    }


    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }


    private void aiTurn() {
        if (turnAiWinCell()) return;
        if (turnHumanWinCell()) return;
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean turnAiWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = AI_DOT;
                    if (checkWin(AI_DOT)) return true;
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean turnHumanWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = HUMAN_DOT;
                    if (checkWin(HUMAN_DOT)) {
                        field[i][j] = AI_DOT;
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean checkWin(char c) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLen, c)) return true;
                if (checkLine(i, j, 1, 1, winLen, c)) return true;
                if (checkLine(i, j, 0, 1, winLen, c)) return true;
                if (checkLine(i, j, 1, -1, winLen, c)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(int x, int y, int vx, int vy, int len, int c) {
        final int far_x = x + (len - 1) * vx;
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;
        for (int i = 0; i < len; i++) {
            if (field[y + i * vy][x + i * vx] != c) return false;
        }
        return true;
    }

//    private boolean checkWin(int c) {
//        int diagonalDown = 0;
//        int diagonalUp = 0;
//        for (int i = 0; i < fieldSizeX; i++) {
//            diagonalDown += field[i][i];
//            diagonalUp += field[i][fieldSizeX - 1 - i];
//        }
//        if (diagonalDown == c * fieldSizeX || diagonalUp == c * fieldSizeX) {
//            return true;
//        }
//
//        for (int j = 0; j < fieldSizeY; j++) {
//            int lineX = 0;
//            int lineY = 0;
//            for (int k = 0; k < fieldSizeX; k++) {
//                lineX += field[j][k];
//                lineY += field[k][j];
//            }
//            if (lineX == c * fieldSizeX || lineY == c * fieldSizeX) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//        if (field[0][0] == c && field[0][1] == c && field[0][2] == c) return true;
//        if (field[1][0] == c && field[1][1] == c && field[1][2] == c) return true;
//        if (field[2][0] == c && field[2][1] == c && field[2][2] == c) return true;
//
//        if (field[0][0] == c && field[1][1] == c && field[2][0] == c) return true;
//        if (field[0][1] == c && field[1][1] == c && field[2][1] == c) return true;
//        if (field[0][2] == c && field[1][2] == c && field[2][2] == c) return true;
//
//        if (field[0][0] == c && field[1][1] == c && field[2][2] == c) return true;
//        if (field[0][2] == c && field[1][1] == c && field[2][0] == c) return true;
//        return false;
//    }

    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    private boolean checkEndGame(char dot, int gameOverType) {
        if (checkWin(dot)) {
            isGameOver = true;
            this.gameOverType = gameOverType;
            repaint();
            return true;
        }
        if (isMapFull()) {
            isGameOver = true;
            this.gameOverType = STATE_DRAW;
            repaint();
            return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!isInitialized) return;
        panelWidth = getWidth();
        panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;

        g.setColor(Color.BLACK);
        for (int h = 0; h < fieldSizeY; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }
        for (int w = 0; w < fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) continue;

                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == AI_DOT) {
                    g.setColor(new Color(0xff0000));
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException("Unexpected value " + field[y][x] + " in cell: x=" + x + " y=" + y);
                }
            }
        }
        if (isGameOver) showMessageGameOver(g);
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.yellow);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW -> g.drawString(MSG_DRAW, 110, getHeight() / 2);
            case STATE_WIN_AI -> g.drawString(MSG_WIN_AI, 160, getHeight() / 2);
            case STATE_WIN_HUMAN -> g.drawString(MSG_WIN_HUMAN, 120, getHeight() / 2);
            default -> throw new RuntimeException("Unexpected gameOver state " + gameOverType);
        }
    }
}
