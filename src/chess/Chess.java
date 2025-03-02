package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;

public class Chess {

    enum Player { white, black }
    
    private static Board board;
    private static Player currentPlayer;

    /**
     * Plays the next move for whichever player has the turn.
     * 
     * @param move String for next move, e.g. "e1 g1" for castling
     * 
     * @return A ReturnPlay instance that contains the result of the move.
     *         See the section "The Chess class" in the assignment description for details of
     *         the contents of the returned ReturnPlay instance.
     */
    public static ReturnPlay play(String move) {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = new ArrayList<>();

        if (move.toLowerCase().equals("resign")) {
            returnPlay.piecesOnBoard = board.getPieces();
            returnPlay.message = (currentPlayer == Player.white) ? 
                ReturnPlay.Message.RESIGN_BLACK_WINS : ReturnPlay.Message.RESIGN_WHITE_WINS;
            return returnPlay;
        }

        String[] parts = move.split(" ");
        if (parts.length < 2) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }

        String from = parts[0];
        String to = parts[1];

        if (!board.isValidMove(from, to, currentPlayer.toString().toLowerCase())) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }

        // Handle castling
        Piece movedPiece = board.getPieceAt(ReturnPiece.PieceFile.valueOf(positionFile(from)), positionRank(from));
        if (movedPiece instanceof King && Math.abs(positionFile(from).charAt(0) - positionFile(to).charAt(0)) == 2) {
            if (isCastlingMove(movedPiece, from, to)) {
                performCastling(movedPiece, from, to);
            } else {
                returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
                return returnPlay;
            }
        } else {
            board.movePiece(from, to);
        }

        returnPlay.piecesOnBoard = board.getPieces();

        movedPiece = board.getPieceAt(ReturnPiece.PieceFile.valueOf(positionFile(to)), positionRank(to));
        if (movedPiece instanceof Pawn && (positionRank(to) == 1 || positionRank(to) == 8)) {
            String promotionPiece = parts.length > 2 ? parts[2].toUpperCase() : "Q";
            Piece promotedPiece = ((Pawn) movedPiece).promote(promotionPiece);
            board.setPieceAt(ReturnPiece.PieceFile.valueOf(positionFile(to)), positionRank(to), promotedPiece);
            returnPlay.piecesOnBoard = board.getPieces();
        }
        if (parts.length > 2 && parts[2].toLowerCase().equals("draw?")) {
            returnPlay.message = ReturnPlay.Message.DRAW;
            return returnPlay;
        }

        Player opponent = (currentPlayer == Player.white) ? Player.black : Player.white;

        if (board.isCheckmate(opponent.toString().toLowerCase())) {
            returnPlay.message = (currentPlayer == Player.white) ? 
                ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
        } else if (board.isCheck(opponent.toString().toLowerCase())) {
            returnPlay.message = ReturnPlay.Message.CHECK;
        } else if (board.isStalemate(opponent.toString().toLowerCase())) {
            returnPlay.message = ReturnPlay.Message.STALEMATE;
        }

        currentPlayer = opponent;
        return returnPlay;
    }

    /**
     * This method should reset the game, and start from scratch.
     */
    public static void start() {
        board = new Board();
        currentPlayer = Player.white;
    }

    // Helper methods
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static String positionFile(String position) {
        return position.substring(0, 1);
    }

    public static int positionRank(String position) {
        return Character.getNumericValue(position.charAt(1));
    }

    private static boolean isCastlingMove(Piece king, String from, String to) {
        if (!(king instanceof King)) return false;

        int rank = positionRank(from);
        int fileDiff = Math.abs(positionFile(from).charAt(0) - positionFile(to).charAt(0));
        if (fileDiff != 2) return false;

        PieceFile rookFile = (positionFile(to).charAt(0) == 'g') ? PieceFile.h : PieceFile.a;
        Piece rook = board.getPieceAt(rookFile, rank);
        if (!(rook instanceof Rook)) return false;

        return !((King) king).hasMoved && !((Rook) rook).hasMoved && board.isPathClear(king.pieceFile, rookFile, rank);
    }

    private static void performCastling(Piece king, String from, String to) {
        int rank = positionRank(from);
        PieceFile rookFile = (positionFile(to).charAt(0) == 'g') ? PieceFile.h : PieceFile.a;
        Piece rook = board.getPieceAt(rookFile, rank);
    
        if (rook == null) {
            throw new NullPointerException("Rook is null during castling");
        }
    
        board.movePiece(from, to);
        if (rookFile == PieceFile.h) {
            board.movePiece(rookFile.name() + rank, "f" + rank);
        } else {
            board.movePiece(rookFile.name() + rank, "d" + rank);
        }
    }
}