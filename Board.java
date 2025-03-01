package chess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
        initializeBoard();
    }

    /**
     * Initializes the board with pieces in their starting positions.
     */
    private void initializeBoard() {
        // Initialize white pieces
        squares[0][0] = new Rook(Chess.Player.white);
        squares[0][1] = new Knight(Chess.Player.white);
        squares[0][2] = new Bishop(Chess.Player.white);
        squares[0][3] = new Queen(Chess.Player.white);
        squares[0][4] = new King(Chess.Player.white);
        squares[0][5] = new Bishop(Chess.Player.white);
        squares[0][6] = new Knight(Chess.Player.white);
        squares[0][7] = new Rook(Chess.Player.white);
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(Chess.Player.white);
        }

        // Initialize black pieces
        squares[7][0] = new Rook(Chess.Player.black);
        squares[7][1] = new Knight(Chess.Player.black);
        squares[7][2] = new Bishop(Chess.Player.black);
        squares[7][3] = new Queen(Chess.Player.black);
        squares[7][4] = new King(Chess.Player.black);
        squares[7][5] = new Bishop(Chess.Player.black);
        squares[7][6] = new Knight(Chess.Player.black);
        squares[7][7] = new Rook(Chess.Player.black);
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new Pawn(Chess.Player.black);
        }
    }

    /**
     * Validates if a move is legal for the given player.
     *
     * @param from   The starting position (e.g., "e2").
     * @param to     The target position (e.g., "e4").
     * @param player The player making the move.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidMove(String from, String to, Chess.Player player) {
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

    /**
     * Moves a piece from one square to another.
     *
     * @param from The starting position (e.g., "e2").
     * @param to   The target position (e.g., "e4").
     */
    public void movePiece(String from, String to) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];
        squares[toY][toX] = piece;
        squares[fromY][fromX] = null;
    }

    /**
     * Returns a list of all pieces on the board in the format required by ReturnPiece.
     *
     * @return A list of ReturnPiece objects representing the current board state.
     */
    public List<ReturnPiece> getPieces() {
        List<ReturnPiece> pieces = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (squares[y][x] != null) {
                    ReturnPiece rp = new ReturnPiece();
                    rp.pieceType = squares[y][x].getType();
                    rp.pieceFile = ReturnPiece.PieceFile.values()[x];
                    rp.pieceRank = y + 1;
                    pieces.add(rp);
                }
            }
        }
        return pieces;
    }

    /**
     * Checks if the given player's king is in check.
     *
     * @param player The player to check (white or black).
     * @return True if the player's king is in check, false otherwise.
     */
    public boolean isCheck(Chess.Player player) {
        // Find the player's king
        int kingX = -1, kingY = -1;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getType() == ReturnPiece.PieceType.WK && piece.getPlayer() == player) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
            if (kingX != -1) break;
        }

        // Check if any opponent's piece can attack the king
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getPlayer() != player) {
                    if (piece.isValidMove(x, y, kingX, kingY, squares)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if the given player is in checkmate.
     *
     * @param player The player to check (white or black).
     * @return True if the player is in checkmate, false otherwise.
     */
    public boolean isCheckmate(Chess.Player player) {
        // Check if the player is in check
        if (!isCheck(player)) {
            return false;
        }

        // Check if any move can get the player out of check
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getPlayer() == player) {
                    for (int toY = 0; toY < 8; toY++) {
                        for (int toX = 0; toX < 8; toX++) {
                            if (piece.isValidMove(x, y, toX, toY, squares)) {
                                // Simulate the move
                                Piece temp = squares[toY][toX];
                                squares[toY][toX] = piece;
                                squares[y][x] = null;

                                // Check if the player is still in check
                                boolean stillInCheck = isCheck(player);

                                // Undo the move
                                squares[y][x] = piece;
                                squares[toY][toX] = temp;

                                if (!stillInCheck) {
                                    return false; // Found a move that gets out of check
                                }
                            }
                        }
                    }
                }
            }
        }

        return true; // No moves to get out of check
    }
}