package chess;

public class Pawn extends Piece {
    public Pawn(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super(piecetype, pieceFile, pieceRank, color, board);
    }

    @Override
    public boolean canMove(String destination) {
        System.out.println("Checking if pawn can move to " + destination);
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

        if (isEnPassantMove(currentFile, finalFile, currentRank, finalRank)) {
            System.out.println("Calling isEnPassantMove for destination " + destination);
            return true;
        } else {
            System.out.println("Not calling isEnPassantMove for destination " + destination);
            System.out.println("currentFile: " + currentFile);
            System.out.println("finalFile: " + finalFile);
            System.out.println("currentRank: " + currentRank);
            System.out.println("finalRank: " + finalRank);
        }
        return false;
    }

    private boolean isEnPassantMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        System.out.println("Checking en passant move...");
        // Basic en passant conditions: diagonal move, correct rank
        if (Math.abs(currentFile.ordinal() - finalFile.ordinal()) == 1) {
            if ((this.color.equals("white") && finalRank == currentRank + 1) ||
                (this.color.equals("black") && finalRank == currentRank - 1)) {
                return board.isEnPassantPossible(currentFile, finalFile, currentRank, finalRank, this.color);
            }
        }
        return false;
    }
    

    @Override
    public void move(String destination) {
        super.move(destination);
        if (Math.abs(this.pieceFile.ordinal() - PieceFile.valueOf(Chess.positionFile(destination)).ordinal()) == 1) {
            int capturedPieceRank = this.color.equals("white") ? this.pieceRank - 1 : this.pieceRank + 1;
            PieceFile capturedPieceFile = PieceFile.valueOf(Chess.positionFile(destination));
            if (board.getPieceAt(capturedPieceFile, capturedPieceRank) != null) {
                board.setPieceAt(capturedPieceFile, capturedPieceRank, null);
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
