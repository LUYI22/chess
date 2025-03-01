package chess;

public class Knight extends Piece {
    public Knight(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super(piecetype, pieceFile, pieceRank, color, board);
    }

    @Override
    public boolean canMove(String destination) {
        int currentRank = this.pieceRank;
        int finalRank = Chess.positionRank(destination);
        PieceFile currentFile = this.pieceFile;
        PieceFile finalFile = PieceFile.valueOf(Chess.positionFile(destination));

        if (isLShapedMove(currentFile, finalFile, currentRank, finalRank)) {
            return isDestinationValid(finalFile, finalRank);
        }
        return false;
    }

    private boolean isLShapedMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        int fileDifference = Math.abs(currentFile.ordinal() - finalFile.ordinal());
        int rankDifference = Math.abs(currentRank - finalRank);
        return (fileDifference == 2 && rankDifference == 1) || (fileDifference == 1 && rankDifference == 2);
    }

    private boolean isDestinationValid(PieceFile finalFile, int finalRank) {
        Piece pieceAtDestination = board.getPieceAt(finalFile, finalRank);
        return pieceAtDestination == null || !pieceAtDestination.getColor().equals(this.color);
    }
}
