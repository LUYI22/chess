package chess;

import java.util.ArrayList;

public class Chess {

    enum Player { white, black }

    private static Board board;
    private static Player currentPlayer;

    /**
     * Plays the next move for whichever player has the turn.
     *
     * @param move String for next move, e.g., "a2 a3"
     * @return A ReturnPlay instance that contains the result of the move.
     */
    public static ReturnPlay play(String move) {
        ReturnPlay result = new ReturnPlay();
        result.piecesOnBoard = new ArrayList<>();

        // Parse the move
        String[] parts = move.trim().split(" ");
        if (parts.length < 2) {
            result.message = ReturnPlay.Message.ILLEGAL_MOVE;
            result.piecesOnBoard = board.getPieces();
            return result;
        }

        String from = parts[0];
        String to = parts[1];

        // Validate the move
        if (!board.isValidMove(from, to, currentPlayer)) {
            result.message = ReturnPlay.Message.ILLEGAL_MOVE;
            result.piecesOnBoard = board.getPieces();
            return result;
        }

        // Execute the move
        board.movePiece(from, to);

        // Check for checkmate or check
        if (board.isCheckmate(Player.black)) {
            result.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
        } else if (board.isCheckmate(Player.white)) {
            result.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
        } else if (board.isCheck(currentPlayer)) {
            result.message = ReturnPlay.Message.CHECK;
        } else {
            result.message = null; // No special message
        }

        // Update the board state
        result.piecesOnBoard = board.getPieces();

        // Switch players
        currentPlayer = (currentPlayer == Player.white) ? Player.black : Player.white;

        return result;
    }

    /**
     * This method should reset the game, and start from scratch.
     */
    public static void start() {
        board = new Board(); // Reset the board to the initial state
        currentPlayer = Player.white; // White starts first
    }
}