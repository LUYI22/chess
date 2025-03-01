package chess;

import java.util.ArrayList;

public class Chess {
    private String currentPlayer;
    private Board board;

    public Chess() {
        board = new Board();
        currentPlayer = "white";
    }

    public static ReturnPlay play(String move) {
        // This method should be implemented to handle each move
        // For now, we'll return a placeholder
        return new ReturnPlay();
    }

    public ReturnPlay makeMove(String from, String to) {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = new ArrayList<>();

        if (!board.isValidMove(from, to, currentPlayer)) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }

        board.movePiece(from, to);
        returnPlay.piecesOnBoard = board.getPieces();

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

    public static int positionRank(String position) {
        return Character.getNumericValue(position.charAt(1));
    }
}
