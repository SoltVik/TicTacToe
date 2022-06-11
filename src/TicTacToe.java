import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class TicTacToe extends JPanel {
    private final int size = 3;
    private final int[] tiles = new int[9];
    // Size of tile on UI
    private final int tileSize;

    // Margin for the grid on the frame
    private final int margin;
    // Grid UI Size
    private final int gridSize;
    private int gameOver; // state of Game

    private boolean isXTurn; // is X turn?
    private final int xValue = 10;
    private final int oValue = 100;

    public TicTacToe() {
        // Grid UI Dimension
        int dimension = 550;
        margin = 30;

        isXTurn = true;

        // calculate grid size and tile size
        gridSize = (dimension - 2 * margin);
        tileSize = gridSize / size;

        setPreferredSize(new Dimension(dimension, dimension + margin));
        setFont(new Font("SansSerif", Font.BOLD, 60));

        gameOver = 0;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // used to let users to interact on the grid by clicking
                if (gameOver > 0) {
                    newGame();
                } else {
                    // get position of the click
                    int ex = e.getX() - margin;
                    int ey = e.getY() - margin;

                    // click in the grid ?
                    if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize)
                        return;

                    // get position in the grid
                    int c1 = ex / tileSize;
                    int r1 = ey / tileSize;

                    int clickPos = r1 * size + c1;

                    if (tiles[clickPos] == 0) {
                        if (isXTurn) {
                            tiles[clickPos] = xValue;
                            isXTurn = false;
                        } else {
                            tiles[clickPos] = oValue;
                            isXTurn = true;
                        }
                    }
                    gameOver = isGameOver();
                }

                repaint();
            }
        });

        newGame();
    }

    private void newGame() {
        reset();
        gameOver = 0;
        isXTurn = true;
    }

    private void reset() {
        Arrays.fill(tiles, 0);
    }

    private int isGameOver() {

        for (int a = 0; a < 8; a++) {
            int line = 0;

            switch (a) {
                case 0:
                    line = tiles[0] + tiles[1] + tiles[2];
                    break;
                case 1:
                    line = tiles[3] + tiles[4] + tiles[5];
                    break;
                case 2:
                    line = tiles[6] + tiles[7] + tiles[8];
                    break;
                case 3:
                    line = tiles[0] + tiles[3] + tiles[6];
                    break;
                case 4:
                    line = tiles[1] + tiles[4] + tiles[7];
                    break;
                case 5:
                    line = tiles[2] + tiles[5] + tiles[8];
                    break;
                case 6:
                    line = tiles[0] + tiles[4] + tiles[8];
                    break;
                case 7:
                    line = tiles[2] + tiles[4] + tiles[6];
                    break;
            }

            if (line == xValue * 3) { //For X winner
                return xValue;
            } else if (line == oValue * 3) { // For O winner
                return oValue;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (tiles[i] == 0) {
                break;
            } else if (i == 8) {
                return 1;
            }
        }

        return 0;
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            // we convert 1D coords to 2D coords given the size of the 2D Array
            int r = i / size;
            int c = i % size;
            // we convert in coords on the UI
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;

            g.setColor(tiles[i] == oValue ? Color.BLUE : Color.RED);
            drawCenteredString(g, (tiles[i] == xValue ? "X" : (tiles[i] == oValue ? "0" : "")), x, y);

        }
        g.setColor(Color.BLACK);
        g.drawLine(margin, margin + tileSize, gridSize, margin + tileSize);
        g.drawLine(margin, margin + tileSize * 2, gridSize, margin + tileSize * 2);
        g.drawLine(margin + tileSize, margin, margin + tileSize, gridSize);
        g.drawLine(margin + tileSize * 2, margin, margin + tileSize * 2, gridSize);

    }


    private void drawMessage(Graphics2D g) {
        if (gameOver > 0) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(Color.BLACK);
            String s = "Click to start new game";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2, getHeight() - margin);

            g.setColor(gameOver == xValue ? Color.RED : (gameOver == oValue ? Color.BLUE : Color.ORANGE));
            String winner = (gameOver == xValue ? "Congratulations X won" : (gameOver == oValue ? "Congratulations 0 won" : "This time is draw"));
            g.drawString(winner, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2, getHeight() - margin * 2);
        } else {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(isXTurn ? Color.RED : Color.BLUE);
            String s = (isXTurn ? "X make your turn" : "0 make your turn");
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2, getHeight() - margin);
        }
    }


    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        // center string s for the given tile (x,y)
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2, y + (asc + (tileSize - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2D);
        drawMessage(g2D);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Tic Tac Toe");
            frame.setResizable(false);
            frame.add(new TicTacToe(), BorderLayout.CENTER);
            frame.pack();
            // center on the screen
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}