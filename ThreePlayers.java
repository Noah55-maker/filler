package Filler;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

public class ThreePlayers implements ActionListener, KeyListener{

    private final int LENGTH = 17, HEIGHT = 17;

    private JFrame frame = new JFrame("Filler");
    private JLabel scoreboard1 = new JLabel("1");
    private JLabel scoreboard2 = new JLabel("1");
    private JLabel displayText = new JLabel();
    private int player1Color, player2Color, player3Color;
    private int player1Score = 1, player2Score = 1, player3Score = 1;
    private Color[] colors = {new Color(0xF84762), new Color(0xFFE01C), new Color(0x90BE47), new Color(0x42ACE9), new Color(0x6A4BA2), new Color(0x404040), new Color(0xFF8800)};
    
    private JPanel grid = new JPanel();
    private JPanel[][] panels = new JPanel[HEIGHT][LENGTH];
    private int[][] spots = new int[HEIGHT][LENGTH];
    /*
    -1: player 1
    -2: player 2
    -3: player 3
    0-5: colors
    7: disabled
    */
    private int playerTurn = 1;
    private JButton[] buttons = new JButton[colors.length];
    private Random rand = new Random();

    public static void main(String[] args) {
        new ThreePlayers();
    }

    ThreePlayers() {
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
        grid.setBounds(150, 100, 500, 500);

        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < LENGTH; j++) {
                int tile;
                // if(i > j || i > 2 * LENGTH - j) {
                    if( (i < (double)LENGTH - (double)j * Math.sqrt(3) - 1) || (i < (double)(j-LENGTH+1) * Math.sqrt(3) + LENGTH - 1) ) {
                    spots[i][j] = 7;
                    panels[i][j] = new JPanel();
                    panels[i][j].setBackground(new Color(0xA0A0A0));
                    grid.add(panels[i][j]);
                }
                else {
                    do {
                        tile = rand.nextInt(6);
                    }
                    while((i > 0 && tile == spots[i-1][j]) || (j > 0 && tile == spots[i][j-1]));
                    
                    spots[i][j] = tile;
                    panels[i][j] = new JPanel();
                    panels[i][j].setBackground(colors[tile]);
                    grid.add(panels[i][j]);
                }
            }
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("", Font.PLAIN, 20));
            buttons[i].setForeground(new Color(0xFFFFFF));
            buttons[i].addActionListener(this);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(colors[i]);
            buttons[i].setBounds(100 + 104 * i, 650, 80, 80);
            frame.add(buttons[i]);
        }
        
        while(spots[HEIGHT-1][0] == spots[HEIGHT-1][LENGTH-1]) {
            int tile = rand.nextInt(6);
            spots[HEIGHT-1][0] = tile;
            panels[HEIGHT-1][0].setBackground(colors[tile]);
            
        }

        while(spots[3][8] == spots[HEIGHT-1][0] || spots[3][8] == spots[HEIGHT-1][LENGTH-1]) {
            int tile = rand.nextInt(6);
            spots[3][8] = tile;
            panels[3][8].setBackground(colors[tile]);
            
        }
        
        player1Color = spots[HEIGHT-1][0];
        player2Color = spots[HEIGHT-1][LENGTH-1];
        player3Color = spots[3][8];
        disableButton(player1Color);
        disableButton(player2Color);
        disableButton(player3Color);
        spots[HEIGHT-1][0] = -1;
        spots[HEIGHT-1][LENGTH-1] = -2;
        spots[3][8] = -3;
        
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
        if(b == player1Color || b == player2Color || b == player3Color) return;
        enableButton(player1Color);
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
        player1Color = b;
        disableButton(b);
        playerTurn = (playerTurn % 3) + 1;
        checkGameOver();
    }

    public void playerTurn2(int b) {
        if(b == player1Color || b == player2Color || b == player3Color) return;
        enableButton(player2Color);
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
        displayText.setText("Player 3's turn");
        player2Color = b;
        disableButton(b);
        playerTurn = (playerTurn % 3) + 1;
        checkGameOver();
    }

    public void playerTurn3(int b) {
        if(b == player1Color || b == player2Color || b == player3Color) return;
        enableButton(player3Color);
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < LENGTH; j++) {
                if(spots[i][j] == -3) panels[i][j].setBackground(colors[b]);
                else if(spots[i][j] == b && 
                ((i > 0 && spots[i-1][j] == -3) || (i < HEIGHT-1 && spots[i+1][j] == -3) || (j > 0 && spots[i][j-1] == -3) || (j < LENGTH-1 && spots[i][j+1] == -3))) {
                    spots[i][j] = -3;
                    panels[i][j].setBackground(colors[b]);
                    player3Score++;
                }
            }
        }
        // scoreboard1.setText(String.valueOf(player3Score));
        displayText.setText("Player 1's turn");
        player3Color = b;
        disableButton(b);
        playerTurn = (playerTurn % 3) + 1;
        checkGameOver();
    }

    public void enableButton(int b) {
        buttons[b].setEnabled(true);
        buttons[b].setBounds(buttons[b].getX()-10, buttons[b].getY()-10, 80, 80);
    }

    public void disableButton(int b) {
        buttons[b].setEnabled(false);
        buttons[b].setBounds(buttons[b].getX()+10, buttons[b].getY()+10, 60, 60);
    }

    public void checkGameOver() {
        if(player1Score + player2Score + player3Score != 120) return;

        if(player1Score > player2Score && player1Score > player3Score) {
            displayText.setText("Player 1 wins");
        }
        else if(player2Score > player1Score && player2Score > player3Score) {
            displayText.setText("Player 2 wins");
        }
        else if(player3Score > player1Score && player3Score > player2Score) {
            displayText.setText("Player 3 wins");
        }
        else {
            displayText.setText("Tie");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int b = 0; b < buttons.length; b++) {
            if(e.getSource() == buttons[b]) {
                if(playerTurn == 1) {
                    playerTurn1(b);
                }
                else if(playerTurn == 2){
                    playerTurn2(b);
                }
                else {
                    playerTurn3(b);
                }
            }
        }
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() >= 49 && e.getKeyCode() <= 54) {
            if(playerTurn == 1) {
                playerTurn1(e.getKeyCode() - 49);
            }
            else if(playerTurn == 2) {
                playerTurn2(e.getKeyCode() - 49);
            }
            else {
                playerTurn3(e.getKeyCode() - 49); 
            }
        }
        else if(e.getKeyCode() >= 97 && e.getKeyCode() <= 102) {
            if(playerTurn == 1) {
                playerTurn1(e.getKeyCode() - 97);
            }
            else if(playerTurn == 2) {
                playerTurn2(e.getKeyCode() - 97);
            }
            else {
                playerTurn3(e.getKeyCode() - 97);
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