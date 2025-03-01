package chess;

import java.util.ArrayList;

public class Chess {
    private static String currentPlayer;
    private static Board board;

    public static void start() {
        board = new Board();
        currentPlayer = "white";
    }

    public static ReturnPlay play(String move) {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = new ArrayList<>();

        // Handle resign
        if (move.toLowerCase().equals("resign")) {
            returnPlay.piecesOnBoard = board.getPieces();
            returnPlay.message = currentPlayer.equals("white") ? 
                ReturnPlay.Message.RESIGN_BLACK_WINS : ReturnPlay.Message.RESIGN_WHITE_WINS;
            return returnPlay;
        }

        // Parse the move
        String[] parts = move.split(" ");
        if (parts.length < 2) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }

        String from = parts[0];
        String to = parts[1];

        if (!board.isValidMove(from, to, currentPlayer)) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }

        // Execute the move
        board.movePiece(from, to);
        returnPlay.piecesOnBoard = board.getPieces();

        // Handle draw offer
        if (parts.length > 2 && parts[2].toLowerCase().equals("draw?")) {
            returnPlay.message = ReturnPlay.Message.DRAW;
            return returnPlay;
        }

        String opponent = (currentPlayer.equals("white")) ? "black" : "white";

        if (board.isCheckmate(opponent)) {
            returnPlay.message = (currentPlayer.equals("white")) ? 
                ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
        } else if (board.isCheck(opponent)) {
            returnPlay.message = ReturnPlay.Message.CHECK;
        } else if (board.isStalemate(opponent)) {
            returnPlay.message = ReturnPlay.Message.STALEMATE;
        }

        currentPlayer = opponent;
        return returnPlay;
    }

    public static String positionFile(String position) {
        return position.substring(0, 1);
    }

    public static String getCurrentPlayer() {
        return currentPlayer;
    }

    public static int positionRank(String position) {
        return Character.getNumericValue(position.charAt(1));
    }
}
