package chess;

public class Rook extends Piece {
    protected boolean hasMoved = false;

    public Rook(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super(piecetype, pieceFile, pieceRank, color, board);
    }

    @Override
    public boolean canMove(String destination) {
        int currentRank = this.pieceRank;
        int finalRank = Chess.positionRank(destination);
        PieceFile currentFile = this.pieceFile;
        PieceFile finalFile = PieceFile.valueOf(Chess.positionFile(destination));

        if (isStraightMove(currentFile, finalFile, currentRank, finalRank)) {
            return isPathClear(currentFile, finalFile, currentRank, finalRank) &&
                    isDestinationValid(finalFile, finalRank);
        }
        return false;
    }

    private boolean isStraightMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        return (currentFile == finalFile && currentRank != finalRank) ||
                (currentFile != finalFile && currentRank == finalRank);
    }

    private boolean isPathClear(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        if (currentFile == finalFile) {
            // Vertical move
            int step = currentRank < finalRank ? 1 : -1;
            for (int rank = currentRank + step; rank != finalRank; rank += step) {
                if (board.getPieceAt(currentFile, rank) != null) {
                    return false; // Path is blocked
                }
            }
        } else {
            // Horizontal move
            int step = currentFile.ordinal() < finalFile.ordinal() ? 1 : -1;
            for (int file = currentFile.ordinal() + step; file != finalFile.ordinal(); file += step) {
                if (board.getPieceAt(PieceFile.values()[file], currentRank) != null) {
                    return false; // Path is blocked
                }
            }
        }
        return true; // Path is clear
    }

    private boolean isDestinationValid(PieceFile finalFile, int finalRank) {
        Piece pieceAtDestination = board.getPieceAt(finalFile, finalRank);
        return pieceAtDestination == null || !pieceAtDestination.getColor().equals(this.color);
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }


    @Override
    public void move(String destination) {
        super.move(destination);
        hasMoved = true;
    }
}
