package chess;

import java.util.ArrayList;
import chess.ReturnPiece.PieceFile;

public class Board {
    private Piece[][] squares;
    private String lastMove;
    protected boolean whiteKingMoved = false;
    protected boolean blackKingMoved = false;
    protected boolean whiteRookMovedLeft = false;
    protected boolean whiteRookMovedRight = false;
    protected boolean blackRookMovedLeft = false;
    protected boolean blackRookMovedRight = false;

    public Board() {
        squares = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize white pieces
        squares[0][0] = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 1, "white", this);
        squares[0][1] = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 1, "white", this);
        squares[0][2] = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 1, "white", this);
        squares[0][3] = new Queen(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 1, "white", this);
        King whiteKing = new King(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 1, "white", this);
        squares[0][4] = whiteKing;
        squares[0][5] = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 1, "white", this);
        squares[0][6] = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 1, "white", this);
        Rook whiteRightRook = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 1, "white", this);
        squares[0][7] = whiteRightRook;
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(ReturnPiece.PieceType.WP, ReturnPiece.PieceFile.values()[i], 2, "white", this);
        }

        // Initialize black pieces
        Rook blackLeftRook = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 8, "black",this);
        squares[7][0] = blackLeftRook;
        squares[7][1] = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 8, "black", this);
        squares[7][2] = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 8, "black", this);
        squares[7][3] = new Queen(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 8, "black", this);
        King blackKing = new King(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 8, "black", this);
        squares[7][4] = blackKing;
        squares[7][5] = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 8, "black", this);
        squares[7][6] = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 8, "black", this);
        Rook blackRightRook = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 8, "black", this);
        squares[7][7] = blackRightRook;
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new Pawn(ReturnPiece.PieceType.BP, ReturnPiece.PieceFile.values()[i], 7, "black", this);
        }
    }
    public boolean isValidMove(String from, String to, String player) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;
        Piece piece = squares[fromY][fromX];

        if (piece == null || !piece.getColor().equals(player)) {
            return false;
        }

        // En Passant Check - before general canMove
         if (piece instanceof Pawn) {
            if (isEnPassantMove(from, to, player)) {
                return true;
            }
        }
        // Castling Check
        if (piece instanceof King && Math.abs(fromX - toX) == 2) {
            return isValidCastlingMove(piece, from, to);
        }

        if(!piece.canMove(to)){
            return false;
        }
         // Simulate the move to check if it results in a check for the current player
        Piece originalPieceAtDestination = squares[toY][toX];
        squares[toY][toX] = piece;
        squares[fromY][fromX] = null;

        boolean isInCheck = isCheck(player);

        // Undo the move
        squares[fromY][fromX] = piece;
        squares[toY][toX] = originalPieceAtDestination;

        if (isInCheck) {
            return false; // The move would result in a check, so it's invalid
        }

        return true;
    }
    private boolean isValidCastlingMove(Piece king, String from, String to) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        //int toY = Character.getNumericValue(to.charAt(1)) - 1;

        if (((King)king).hasMoved()) {
            return false;
        }

        // Kingside Castling
        if (toX == 6) {
            if ((king.getColor().equals("white") && whiteRookMovedRight) || (king.getColor().equals("black") && blackRookMovedRight)) {
                return false;
            }
            // Check if the path is clear
            if (!isPathClear(ReturnPiece.PieceFile.values()[fromX], ReturnPiece.PieceFile.values()[7], fromY + 1)) {
                return false;
            }
            // Check if the king is in check, or passes through a square that is attacked
            if (isSquareAttacked(fromX, fromY, king.getColor()) || isSquareAttacked(5, fromY, king.getColor()) || isSquareAttacked(6, fromY, king.getColor())) {
                return false;
            }
            return true;
        }
        // Queenside Castling
        else if (toX == 2) {
            if ((king.getColor().equals("white") && whiteRookMovedLeft) || (king.getColor().equals("black") && blackRookMovedLeft)) {
                return false;
            }
            // Check if the path is clear
            if (!isPathClear(ReturnPiece.PieceFile.values()[fromX], ReturnPiece.PieceFile.values()[0], fromY + 1)) {
                return false;
            }
            // Check if the king is in check, or passes through a square that is attacked
            if (isSquareAttacked(fromX, fromY, king.getColor()) || isSquareAttacked(3, fromY, king.getColor()) || isSquareAttacked(2, fromY, king.getColor())) {
                return false;
            }
            return true;
        }

        return false;
    }

    private boolean isSquareAttacked(int x, int y, String playerColor) {
        String opponentColor = playerColor.equals("white") ? "black" : "white";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = squares[j][i];
                if (piece != null && piece.getColor().equals(opponentColor)) {
                    String to = fileRankToString(ReturnPiece.PieceFile.values()[x], y + 1);
                    if (piece.canMove(to)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isEnPassantMove(String from, String to, String playerColor) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];

        if (!(piece instanceof Pawn)) {
            return false;
        }

        // En passant can only happen on the 5th rank for white and 4th rank for black
        if ((playerColor.equals("white") && fromY != 4) || (playerColor.equals("black") && fromY != 3)) {
            return false;
        }

         // The destination must be one file away
        if (Math.abs(fromX - toX) != 1 || toY - fromY != (playerColor.equals("white") ? 1 : -1)) {
            return false;
        }

        // Check if there's a pawn of the opposite color in the correct square
        Piece targetPawn = squares[fromY][toX];  // y, x
        if (targetPawn == null || !(targetPawn instanceof Pawn) || targetPawn.getColor().equals(playerColor)) {
            return false;
        }

        // Finally, check if the last move was a two-square pawn advance by the target pawn
        if (lastMove != null) {
            String[] lastMoveParts = lastMove.split(" ");
            if (lastMoveParts.length == 2) {
                String lastMoveFrom = lastMoveParts[0];
                String lastMoveTo = lastMoveParts[1];

                int lastMoveFromX = lastMoveFrom.charAt(0) - 'a';
                int lastMoveFromY = Character.getNumericValue(lastMoveFrom.charAt(1)) - 1;
                int lastMoveToX = lastMoveTo.charAt(0) - 'a';
                int lastMoveToY = Character.getNumericValue(lastMoveTo.charAt(1)) - 1;

                // Verify that the pawn that moved last turn is the same pawn we're targeting for en passant
                 if (lastMoveToX == toX && lastMoveToY == fromY && lastMoveFromX == toX &&
                    Math.abs(lastMoveFromY - lastMoveToY) == 2 && squares[lastMoveToY][lastMoveToX] == targetPawn) {
                        return true;
                }
            }
        }
        return false;
    }

    public void movePiece(String from, String to) {
        int fromX = from.charAt(0) - 'a';
        int fromY = Character.getNumericValue(from.charAt(1)) - 1;
        int toX = to.charAt(0) - 'a';
        int toY = Character.getNumericValue(to.charAt(1)) - 1;

        Piece piece = squares[fromY][fromX];
        if (piece == null) {
            throw new NullPointerException("Cannot move a null piece from " + from + " to " + to);
        }

        // En Passant capture
        if (piece instanceof Pawn && isEnPassantMove(from, to, piece.getColor())) {
            int captureY = fromY;
            int captureX = toX;
            squares[captureY][captureX] = null;
        }

        squares[toY][toX] = piece;
        squares[fromY][fromX] = null;
        piece.move(to);

        // Update castling flags
        if (piece instanceof King) {
            if (piece.getColor().equals("white")) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        } else if (piece instanceof Rook) {
            if (piece.getColor().equals("white")) {
                if (fromX == 0 && fromY == 0) {
                    whiteRookMovedLeft = true;
                } else if (fromX == 7 && fromY == 0) {
                    whiteRookMovedRight = true;
                }
            } else {
                if (fromX == 0 && fromY == 7) {
                    blackRookMovedLeft = true;
                } else if (fromX == 7 && fromY == 7) {
                    blackRookMovedRight = true;
                }
            }
        }

        // Handle castling move
        if (piece instanceof King && Math.abs(fromX - toX) == 2) {
            if (toX == 2) { // Queen-side castling
                movePiece("a" + (fromY + 1), "d" + (fromY + 1));
            } else if (toX == 6) { // King-side castling
                movePiece("h" + (fromY + 1), "f" + (fromY + 1));
            }
        }

        // track the last move
        lastMove = from + " " + to;
    }

    public boolean isEnPassantPossible(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank, String color) {
        if (lastMove == null) {
            return false;
        }
        String[] parts = lastMove.split(" ");
        if (parts.length < 2) {
            return false;
        }
        String lastFrom = parts[0];
        String lastTo = parts[1];

        int lastFromX = lastFrom.charAt(0) - 'a';
        int lastFromY = Character.getNumericValue(lastFrom.charAt(1)) - 1;
        int lastToX = lastTo.charAt(0) - 'a';
        int lastToY = Character.getNumericValue(lastTo.charAt(1)) - 1;

        Piece lastMovedPiece = squares[lastToY][lastToX];
        if (!(lastMovedPiece instanceof Pawn)) {
            return false;
        }

        if (Math.abs(lastFromY - lastToY) == 2 && lastFromX == lastToX) {
            //Check if the en passant target is one file away from the current pawn
            if (Math.abs(currentFile.ordinal() - lastToX) !=1){
                return false;
            }
            if (color.equals("white") && currentRank == 5 && finalRank == 6 && lastToY == 4) {
                return true;
            } else if (color.equals("black") && currentRank == 4 && finalRank == 3 && lastToY == 3) {
                return true;
            }
        }

        return false;
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
        String opponentColor = player.equals("white") ? "black" : "white";

        // Check if any opposite color piece can attack the king
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = squares[y][x];
                if (piece != null && piece.getColor().equals(opponentColor)) {
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

    public Piece getPieceAt(ReturnPiece.PieceFile file, int rank) {
        int x = file.ordinal();
        int y = rank - 1;  // Convert to 0-based index
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return squares[y][x];
        }
        return null;  // Return null if the position is out of bounds
    }

    public void setPieceAt(ReturnPiece.PieceFile file, int rank, Piece piece) {
        int x = file.ordinal();
        int y = rank - 1;  // Convert to 0-based index
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            squares[y][x] = piece;
        }
    }

    private String fileRankToString(ReturnPiece.PieceFile file, int rank) {
        return file.toString() + rank;
    }
    public boolean isPathClear(ReturnPiece.PieceFile fromFile, ReturnPiece.PieceFile toFile, int rank) {
        int step = fromFile.ordinal() < toFile.ordinal() ? 1 : -1;
        for (int file = fromFile.ordinal() + step; file != toFile.ordinal(); file += step) {
            if (getPieceAt(ReturnPiece.PieceFile.values()[file], rank) != null) {
                return false; // Path is blocked
            }
        }
        return true; // Path is clear
    }
}
