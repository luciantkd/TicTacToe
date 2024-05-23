import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TicTacToe implements ActionListener {

    Random random = new Random(); // Random generator for deciding the first turn
    JFrame frame = new JFrame(); // Main window frame
    JPanel title_panel = new JPanel(); // Panel for the game title
    JPanel button_panel = new JPanel(); // Panel for the game buttons
    JLabel textField = new JLabel(); // Label to display game status
    JButton[] buttons = new JButton[9]; // Array of buttons for the Tic Tac Toe grid
    boolean player1_turn; // Boolean to track which player's turn it is

    // Constructor to initialize the game
    TicTacToe(){
        // Setup frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.getContentPane().setBackground(new Color(40, 44, 52));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        // Setup text field for the game title/status
        textField.setBackground(new Color (60, 63, 65));
        textField.setForeground(new Color (255, 255, 255));
        textField.setFont(new Font("Helvetica", Font.BOLD, 75));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic-Tac-Toe");
        textField.setOpaque(true);

        // Setup title panel
        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);
        title_panel.add(textField, BorderLayout.CENTER);
        frame.add(title_panel, BorderLayout.NORTH);

        // Setup button panel
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

    // Action performed method to handle button clicks
    public void actionPerformed(ActionEvent e){
        for (int i=0; i<9; i++){
            if (e.getSource()==buttons[i]){
                if (player1_turn) {
                    if (buttons[i].getText().equals("")){
                        buttons[i].setForeground(new Color(255, 105, 97));
                        buttons[i].setText("X");
                        player1_turn = false;
                        textField.setText("O turn");
                        check(); // Check if there's a winner after the move
                        if (!isGameOver()){
                            // Delay for AI move
                            new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() throws Exception {
                                    Thread.sleep(1000);
                                    return null;
                                }
                                @Override
                                protected void done() {
                                    aiMove();
                                }
                            }.execute();
                        }
                    }
                } 
            }
        }
    }

    // Method to determine the first turn
    public void firstTurn(){
        if (random.nextInt(2) == 0){
            player1_turn = true;
            textField.setText("X turn");
        } else {
            player1_turn = false;
            textField.setText("O turn");
            // Delay for AI move if it goes first
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(1000);
                    return null;
                }

                @Override
                protected void done() {
                    aiMove();
                }
            }.execute();
        }
    }

    // AI move method
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

    // Minimax algorithm to determine the best move for the AI
    public int minimax(JButton[] board, int depth, boolean isMaximizing) {
        String result = checkWinner();
        if (result != null){
            if (result.equals("X")){
                return -10 + depth; // Return score for X win
            } else if (result.equals("O")){
                return 10 - depth; // Return score for O win
            } else{
                return 0; // Return score for a tie
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

    // Method to check the winner
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

    // Helper method to check if three buttons have the same player's symbol
    private boolean checkCombination(String player, int a, int b, int c) {
        return buttons[a].getText().equals(player) && buttons[b].getText().equals(player) && buttons[c].getText().equals(player);
    }

    // Method to check the game state and determine if there's a winner or a tie
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

    // Method to handle X winning the game
    public void xWins(){
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("X")) {
                buttons[i].setBackground(new Color(144, 238, 144)); // Highlight winning X buttons
            }
            buttons[i].setEnabled(false); // Disable all buttons
        }
        textField.setText("X wins");
    }

    // Method to handle O winning the game
    public void oWins(){
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("O")) {
                buttons[i].setBackground(new Color(173, 216, 230)); // Highlight winning O buttons
            }
            buttons[i].setEnabled(false); // Disable all buttons
        }
        textField.setText("O wins");
    }

    // Method to check if the game is over
    public boolean isGameOver(){
        return checkWinner() != null;
    }
}
