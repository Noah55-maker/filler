package Filler;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

public class TwoColor implements ActionListener, KeyListener{

    private final int LENGTH = 15, HEIGHT = 12;

    private JFrame frame = new JFrame("Filler");
    private JLabel scoreboard1 = new JLabel("1");
    private JLabel scoreboard2 = new JLabel("1");
    private JLabel displayText = new JLabel();
    private int player1Score = 1, player2Score = 1;
    private Color[] colors = {new Color(0xFFFFFF), new Color(0x000000)};
    private JPanel grid = new JPanel();
    private JPanel[][] panels = new JPanel[HEIGHT][LENGTH];
    private int[][] spots = new int[HEIGHT][LENGTH];
    /*
    -1: player 1
    -2: player 2
    0-1: colors
    */
    private boolean player1Turn = true;
    private JButton[] buttons = new JButton[colors.length];
    private Random rand = new Random();

    public static void main(String[] args) {
        new TwoColor();
    }

    TwoColor() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(800, 800);
        frame.addKeyListener(this);

        scoreboard1.setBackground(new Color(0x32A2ED));
        scoreboard1.setOpaque(true);
        scoreboard1.setForeground(new Color(0xC9E8FC));
        scoreboard1.setFont(new Font("Consolas", Font.PLAIN, 35));
        scoreboard1.setBounds(100, 25, 100, 50);
        scoreboard1.setHorizontalAlignment(JLabel.CENTER);
        scoreboard1.setVerticalAlignment(JLabel.CENTER);

        scoreboard2.setBackground(new Color(0xF43652));
        scoreboard2.setOpaque(true);
        scoreboard2.setForeground(new Color(0xFDCBD3));
        scoreboard2.setFont(new Font("Consolas", Font.PLAIN, 35));
        scoreboard2.setBounds(600, 25, 100, 50);
        scoreboard2.setHorizontalAlignment(JLabel.CENTER);
        scoreboard2.setVerticalAlignment(JLabel.CENTER);

        //generating the board
        grid.setLayout(new GridLayout(HEIGHT, LENGTH));
        grid.setBounds(100, 100, 600, 480);

        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < LENGTH; j++) {
                int tile;
                tile = rand.nextInt(2);
                
                spots[i][j] = tile;
                panels[i][j] = new JPanel();
                panels[i][j].setBackground(colors[tile]);
                grid.add(panels[i][j]);
            }
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("", Font.PLAIN, 20));
            buttons[i].setForeground(new Color(0x808080));
            buttons[i].addActionListener(this);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colors[i]);
            buttons[i].setBounds(100 + 104 * i, 650, 80, 80);
            frame.add(buttons[i]);
        }
        
        /*while(spots[HEIGHT - 1][0] == spots[0][LENGTH - 1]) {
            int tile = rand.nextInt(6);
            spots[HEIGHT - 1][0] = tile;
            panels[HEIGHT - 1][0].setBackground(colors[tile]);
            
        }*/
        // disableButton(player1Color);
        // disableButton(player2Color);
        spots[HEIGHT - 1][0] = -1;
        spots[0][LENGTH - 1] = -2;
        
        displayText.setBounds(200, 10, 400, 80);
        displayText.setHorizontalAlignment(JLabel.CENTER);
        displayText.setFont(new Font("MV Boli", Font.PLAIN, 40));
        displayText.setText("Player 1's turn");

        frame.add(displayText);
        frame.add(scoreboard1);
        frame.add(scoreboard2);
        frame.add(grid);
        frame.setVisible(true);
    }

    public void playerTurn1(int b) {
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < LENGTH; j++) {
                if(spots[i][j] == -1) panels[i][j].setBackground(colors[b]);
                else if(spots[i][j] == b && 
                ((i > 0 && spots[i-1][j] == -1) || (i < HEIGHT-1 && spots[i+1][j] == -1) || (j > 0 && spots[i][j-1] == -1) || (j < LENGTH-1 && spots[i][j+1] == -1))) {
                    spots[i][j] = -1;
                    panels[i][j].setBackground(colors[b]);
                    player1Score++;
                }
            }
        }
        scoreboard1.setText(String.valueOf(player1Score));
        displayText.setText("Player 2's turn");
        player1Turn = !player1Turn;
        checkGameOver();
    }

    public void playerTurn2(int b) {
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < LENGTH; j++) {
                if(spots[i][j] == -2) panels[i][j].setBackground(colors[b]);
                if(spots[i][j] == b && 
                ((i > 0 && spots[i-1][j] == -2) || (i < HEIGHT-1 && spots[i+1][j] == -2) || (j > 0 && spots[i][j-1] == -2) || (j < LENGTH-1 && spots[i][j+1] == -2))) {
                    spots[i][j] = -2;
                    panels[i][j].setBackground(colors[b]);
                    player2Score++;
                }
            }
        }
        scoreboard2.setText(String.valueOf(player2Score));
        displayText.setText("Player 1's turn");
        player1Turn = !player1Turn;
        checkGameOver();
    }

    public void checkGameOver() {
        if(player1Score + player2Score != LENGTH * HEIGHT) return;

        if(player1Score > player2Score) {
            displayText.setText("Player 1 wins");
        }
        else if(player2Score > player1Score) {
            displayText.setText("Player 2 wins");
        }
        else {
            displayText.setText("Tie");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int b = 0; b < buttons.length; b++) {
            if(e.getSource() == buttons[b]) {
                if(player1Turn) {
                    playerTurn1(b);
                }
                else {
                    playerTurn2(b);
                }
            }
        }
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == 49 || e.getKeyCode() == 50) {
            if(player1Turn) {
                playerTurn1(e.getKeyCode() - 49);
            }
            else {
                playerTurn2(e.getKeyCode() - 49);
            }
        }
        else if(e.getKeyCode() == 97 || e.getKeyCode() == 98) {
            if(player1Turn) {
                playerTurn1(e.getKeyCode() - 97);
            }
            else {
                playerTurn2(e.getKeyCode() - 97);
            }
        }
        else if(e.getKeyCode() == 18) {
            for(int i = 0; i < buttons.length; i++) {
                buttons[i].setText(String.valueOf(i+1));
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 18) {
            for(int i = 0; i < buttons.length; i++) {
                buttons[i].setText("");
            }
        }
    }

}