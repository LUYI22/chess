package chess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize white pieces
        squares[0][0] = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 1, "white");
        squares[0][1] = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 1, "white");
        squares[0][2] = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 1, "white");
        squares[0][3] = new Queen(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 1, "white");
        squares[0][4] = new King(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 1, "white");
        squares[0][5] = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 1, "white");
        squares[0][6] = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 1, "white");
        squares[0][7] = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 1, "white");
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(ReturnPiece.PieceType.WP, ReturnPiece.PieceFile.values()[i], 2, "white");
        }

        // Initialize black pieces
        squares[7][0] = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 8, "black");
        squares[7][1] = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 8, "black");
        squares[7][2] = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 8, "black");
        squares[7][3] = new Queen(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 8, "black");
        squares[7][4] = new King(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 8, "black");
        squares[7][5] = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 8, "black");
        squares[7][6] = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 8, "black");
        squares[7][7] = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 8, "black");
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new Pawn(ReturnPiece.PieceType.BP, ReturnPiece.PieceFile.values()[i], 7, "black");
        }
    }

    public boolean isValidMove(String from, String to, Chess.Player player) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];
        if (piece == null || !piece.getColor().equals(player.toString())) {
            return false;
        }

        return piece.canMove(to);
    }

    public void movePiece(String from, String to) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];
        squares[toY][toX] = piece;
        squares[fromY][fromX] = null;
        piece.move(to);
    }

    public ArrayList<ReturnPiece> getPieces() {
        ArrayList<ReturnPiece> pieces = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (squares[y][x] != null) {
                    pieces.add(squares[y][x]);
                }
            }
        }
        return pieces;
    }

    public boolean isCheck(Chess.Player player) {
        // Implementation needed
        return false;
    }

    public boolean isCheckmate(Chess.Player player) {
        // Implementation needed
        return false;
    }

    public boolean isStalemate(Chess.Player player) {
        // Implementation needed
        return false;
    }
}
