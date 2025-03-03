package chess;

public class Pawn extends Piece {
    public Pawn(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super(piecetype, pieceFile, pieceRank, color, board);
    }

    @Override
    public boolean canMove(String destination) {
        int currentRank = this.pieceRank;
        int finalRank = Chess.positionRank(destination);
        PieceFile currentFile = this.pieceFile;
        PieceFile finalFile = PieceFile.valueOf(Chess.positionFile(destination));

        // Forward move
        if (isForwardMove(currentFile, finalFile, currentRank, finalRank)) {
            return board.getPieceAt(finalFile, finalRank) == null; // Ensure the square is empty
        }

        // Capture move
        if (isCaptureMove(currentFile, finalFile, currentRank, finalRank)) {
            Piece pieceAtDestination = board.getPieceAt(finalFile, finalRank);
            return pieceAtDestination != null && !pieceAtDestination.getColor().equals(this.color);
        }

        // First move (two squares forward)
        if (isFirstMove(currentFile, finalFile, currentRank, finalRank)) {
            return board.getPieceAt(finalFile, finalRank) == null && 
                   board.getPieceAt(currentFile, (currentRank + finalRank) / 2) == null; // Check both squares are empty
        }
        return false;
    }

    @Override
    public void move(String destination) {
        super.move(destination);
        // Remove the captured pawn for En Passant
        int currentRank = this.pieceRank;
        PieceFile currentFile = this.pieceFile;
        PieceFile finalFile = PieceFile.valueOf(Chess.positionFile(destination));
        if (Math.abs(currentFile.ordinal() - finalFile.ordinal()) == 1) {
            if (this.color.equals("white")) {
                 board.setPieceAt(finalFile, currentRank -1, null);
            }
            if (this.color.equals("black")) {
                 board.setPieceAt(finalFile, currentRank + 1, null);
            }
        }
    }

    public Piece promote(String promotionPiece) {
        Piece promotedPiece;
        switch (promotionPiece.toUpperCase()) {
            case "R":
                promotedPiece = new Rook(PieceType.valueOf(this.color.toUpperCase().charAt(0) + "R"), this.pieceFile, this.pieceRank, this.color, this.board);
                break;
            case "N":
                promotedPiece = new Knight(PieceType.valueOf(this.color.toUpperCase().charAt(0) + "N"), this.pieceFile, this.pieceRank, this.color, this.board);
                break;
            case "B":
                promotedPiece = new Bishop(PieceType.valueOf(this.color.toUpperCase().charAt(0) + "B"), this.pieceFile, this.pieceRank, this.color, this.board);
                break;
            case "Q":
                promotedPiece = new Queen(PieceType.valueOf(this.color.toUpperCase().charAt(0) + "Q"), this.pieceFile, this.pieceRank, this.color, this.board);
                break;
            default:
                promotedPiece = new Queen(PieceType.valueOf(this.color.toUpperCase().charAt(0) + "Q"), this.pieceFile, this.pieceRank, this.color, this.board);
                break;
        }
        return promotedPiece;
    }

    private boolean isForwardMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        if (currentFile == finalFile) {
            if (this.color.equals("white") && finalRank == currentRank + 1) {
                return true;
            } else if (this.color.equals("black") && finalRank == currentRank - 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isCaptureMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        if (Math.abs(currentFile.ordinal() - finalFile.ordinal()) == 1) {
            if (this.color.equals("white") && finalRank == currentRank + 1) {
                return true;
            } else if (this.color.equals("black") && finalRank == currentRank - 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isFirstMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        if (currentFile == finalFile) {
            if (this.color.equals("white") && finalRank == currentRank + 2 && currentRank == 2) {
                return true;
            } else if (this.color.equals("black") && finalRank == currentRank - 2 && currentRank == 7) {
                return true;
            }
        }
        return false;
    }
}
