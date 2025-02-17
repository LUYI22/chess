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
        squares[0][0] = new Rook(Player.white);
        squares[0][1] = new Knight(Player.white);
        squares[0][2] = new Bishop(Player.white);
        squares[0][3] = new Queen(Player.white);
        squares[0][4] = new King(Player.white);
        squares[0][5] = new Bishop(Player.white);
        squares[0][6] = new Knight(Player.white);
        squares[0][7] = new Rook(Player.white);
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(Player.white);
        }

        // Initialize black pieces
        squares[7][0] = new Rook(Player.black);
        squares[7][1] = new Knight(Player.black);
        squares[7][2] = new Bishop(Player.black);
        squares[7][3] = new Queen(Player.black);
        squares[7][4] = new King(Player.black);
        squares[7][5] = new Bishop(Player.black);
        squares[7][6] = new Knight(Player.black);
        squares[7][7] = new Rook(Player.black);
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new Pawn(Player.black);
        }
    }

    public boolean isValidMove(String from, String to, Player player) {
        // Convert from "e2" to coordinates (x, y)
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        // Check if the piece belongs to the player
        Piece piece = squares[fromY][fromX];
        if (piece == null || piece.getPlayer() != player) {
            return false;
        }

        // Check if the move is valid for the piece
        return piece.isValidMove(fromX, fromY, toX, toY, squares);
    }

    public void movePiece(String from, String to) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];
        squares[toY][toX] = piece;
        squares[fromY][fromX] = null;
    }

    public List<ReturnPiece> getPieces() {
        List<ReturnPiece> pieces = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (squares[y][x] != null) {
                    ReturnPiece rp = new ReturnPiece();
                    rp.pieceType = squares[y][x].getType();
                    rp.pieceFile = (char) ('a' + x);
                    rp.pieceRank = y + 1;
                    pieces.add(rp);
                }
            }
        }
        return pieces;
    }

    public boolean isCheck(Player player) {
        // Implement logic to check if the player's king is in check
        return false;
    }

    public boolean isCheckmate(Player player) {
        // Implement logic to check if the player is in checkmate
        return false;
    }
}