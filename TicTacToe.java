import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TicTacToe implements ActionListener {

    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textField = new JLabel();
    JButton[] buttons = new JButton[9];
    boolean player1_turn;

    TicTacToe(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.getContentPane().setBackground(new Color(40, 44, 52));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textField.setBackground(new Color (60, 63, 65));
        textField.setForeground(new Color (255, 255, 255));
        textField.setFont(new Font("Helvetica", Font.BOLD, 75));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic-Tac-Toe");
        textField.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);
        title_panel.add(textField, BorderLayout.CENTER);
        frame.add(title_panel, BorderLayout.NORTH);

        button_panel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("Helvetica", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        frame.add(button_panel);  // Add the button panel to the frame
        frame.setVisible(true);   // Make the frame visible
        firstTurn();              // Initialize the first turn
    }

    public void actionPerformed(ActionEvent e){
        for (int i=0; i<9; i++){
            if (e.getSource()==buttons[i]){
                if (player1_turn) {
                    if (buttons[i].getText().equals("")){
                        buttons[i].setForeground(new Color(255, 105, 97));
                        buttons[i].setText("X");
                        player1_turn = false;
                        textField.setText("O turn");
                        check();
                        if (!isGameOver()){
                            aiMove();
                        }
                    }
                } 
            }
        }
    }

    public void firstTurn(){
        try {
            Thread.sleep(2000);            
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (random.nextInt(2) == 0){
            player1_turn = true;
            textField.setText("X turn");
        } else {
            player1_turn = false;
            textField.setText("O turn");
            aiMove();
        }
    }

    public void aiMove(){
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++){
            if (buttons[i].getText().equals("")){
                buttons[i].setText("O");
                int score = minimax(buttons, 0, false);
                buttons[i].setText("");
                if (score > bestScore){
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            buttons[bestMove].setForeground(new Color(135, 206, 235));
            buttons[bestMove].setText("O");
            player1_turn = true;
            textField.setText("X turn");
            check();
        }
    }

    public int minimax(JButton[] board, int depth, boolean isMaximizing) {
        String result = checkWinner();
        if (result != null){
            if (result.equals("X")){
                return -10 + depth;
            } else if (result.equals("O")){
                return 10 - depth;
            } else{
                return 0;
            }
        }

        if (isMaximizing){
            int bestScore = Integer.MIN_VALUE;
            for (int i=0; i<9; i++){
                if (board[i].getText().equals("")){
                    board[i].setText("O");
                    int score = minimax(board, depth + 1, false);
                    board[i].setText("");
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++){
                if (board[i].getText().equals("")){
                    board[i].setText("X");
                    int score = minimax(board, depth + 1, true);
                    board[i].setText("");
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }

    }

    public String checkWinner(){
        // Check X win conditions
        if (checkCombination("X", 0, 1, 2) || checkCombination("X", 3, 4, 5) || checkCombination("X", 6, 7, 8) ||
            checkCombination("X", 0, 3, 6) || checkCombination("X", 1, 4, 7) || checkCombination("X", 2, 5, 8) ||
            checkCombination("X", 0, 4, 8) || checkCombination("X", 2, 4, 6)) {
            return "X";
        }
        // Check O win conditions
        else if (checkCombination("O", 0, 1, 2) || checkCombination("O", 3, 4, 5) || checkCombination("O", 6, 7, 8) ||
                 checkCombination("O", 0, 3, 6) || checkCombination("O", 1, 4, 7) || checkCombination("O", 2, 5, 8) ||
                 checkCombination("O", 0, 4, 8) || checkCombination("O", 2, 4, 6)) {
            return "O";
        }
        // Check for a tie
        boolean boardFull = true;
        for (int i = 0; i < 9; i++){
            if (buttons[i].getText().equals("")){
                boardFull = false;
                break;
            }
        }
        if (boardFull){
            return "Tie";
        }
        return null;

    }

    private boolean checkCombination(String player, int a, int b, int c) {
        return buttons[a].getText().equals(player) && buttons[b].getText().equals(player) && buttons[c].getText().equals(player);
    }

    public void check() {
        String winner = checkWinner();
        if (winner != null) {
            if (winner.equals("X")) {
                xWins();
            } else if (winner.equals("O")) {
                oWins();
            } else {
                textField.setText("Tie");
                for (int i = 0; i < 9; i++) {
                    buttons[i].setEnabled(false);
                }
            }
        }
    }


    public void xWins(){
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("X")) {
                buttons[i].setBackground(new Color(144, 238, 144));
            }
            buttons[i].setEnabled(false);
        }
        textField.setText("X wins");
    }

    public void oWins(){
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("O")) {
                buttons[i].setBackground(new Color(173, 216, 230)); 
            }
            buttons[i].setEnabled(false);
        }
        textField.setText("O wins");
    }

    public boolean isGameOver(){
        return checkWinner() != null;
    }
}
