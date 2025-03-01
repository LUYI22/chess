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

    public boolean isValidMove(String from, String to, String player) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];
        if (piece == null || !piece.getColor().equals(player)) {
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

    public boolean isCheck(String player) {
        // Find the king
        Piece king = findKing(player);
        String oppositePlayer = player.equals("white") ? "black" : "white";

        // Check if any opposite color piece can attack the king
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getColor().equals(oppositePlayer)) {
                    if (piece.canMove(fileRankToString(king.pieceFile, king.pieceRank))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(String player) {
        if (!isCheck(player)) {
            return false;
        }

        // Try all possible moves for all pieces of the player
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getColor().equals(player)) {
                    for (int toY = 0; toY < 8; toY++) {
                        for (int toX = 0; toX < 8; toX++) {
                            String from = fileRankToString(piece.pieceFile, piece.pieceRank);
                            String to = fileRankToString(ReturnPiece.PieceFile.values()[toX], toY + 1);
                            if (isValidMove(from, to, player)) {
                                // Try the move
                                Piece capturedPiece = squares[toY][toX];
                                movePiece(from, to);
                                boolean stillInCheck = isCheck(player);
                                // Undo the move
                                movePiece(to, from);
                                squares[toY][toX] = capturedPiece;

                                if (!stillInCheck) {
                                    return false; // Found a legal move that escapes check
                                }
                            }
                        }
                    }
                }
            }
        }
        return true; // No legal moves to escape check
    }

    public boolean isStalemate(String player) {
        if (isCheck(player)) {
            return false;
        }

        // Check if the player has any legal moves
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getColor().equals(player)) {
                    for (int toY = 0; toY < 8; toY++) {
                        for (int toX = 0; toX < 8; toX++) {
                            String from = fileRankToString(piece.pieceFile, piece.pieceRank);
                            String to = fileRankToString(ReturnPiece.PieceFile.values()[toX], toY + 1);
                            if (isValidMove(from, to, player)) {
                                return false; // Found a legal move
                            }
                        }
                    }
                }
            }
        }
        return true; // No legal moves available
    }

    private Piece findKing(String player) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getColor().equals(player) && 
                    (piece.pieceType == ReturnPiece.PieceType.WK || piece.pieceType == ReturnPiece.PieceType.BK)) {
                    return piece;
                }
            }
        }
        return null; // This should never happen in a valid chess game
    }

    private String fileRankToString(ReturnPiece.PieceFile file, int rank) {
        return file.toString() + rank;
    }
}
