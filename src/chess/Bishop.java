package chess;

public class Bishop extends Piece {
    public Bishop(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super(piecetype, pieceFile, pieceRank, color, board);
    }

    @Override
    public boolean canMove(String destination) {
        int currentRank = this.pieceRank;
        int finalRank = Chess.positionRank(destination);
        PieceFile currentFile = this.pieceFile;
        PieceFile finalFile = PieceFile.valueOf(Chess.positionFile(destination));

        if (isDiagonalMove(currentFile, finalFile, currentRank, finalRank)) {
            return isPathClear(currentFile, finalFile, currentRank, finalRank) &&
                   isDestinationValid(finalFile, finalRank);
        }
        return false;
    }

    private boolean isDiagonalMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        int fileDifference = Math.abs(currentFile.ordinal() - finalFile.ordinal());
        int rankDifference = Math.abs(currentRank - finalRank);
        return fileDifference == rankDifference && fileDifference > 0;
    }

    private boolean isPathClear(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        int fileStep = currentFile.ordinal() < finalFile.ordinal() ? 1 : -1;
        int rankStep = currentRank < finalRank ? 1 : -1;

        int file = currentFile.ordinal() + fileStep;
        int rank = currentRank + rankStep;

        while (file != finalFile.ordinal() && rank != finalRank) {
            if (board.getPieceAt(PieceFile.values()[file], rank) != null) {
                return false; // Path is blocked
            }
            file += fileStep;
            rank += rankStep;
        }
        return true; // Path is clear
    }

    private boolean isDestinationValid(PieceFile finalFile, int finalRank) {
        Piece pieceAtDestination = board.getPieceAt(finalFile, finalRank);
        return pieceAtDestination == null || !pieceAtDestination.getColor().equals(this.color);
    }
}
