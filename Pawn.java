package chess;

public class Pawn extends Piece {
    public Pawn(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color) {
        super(piecetype, pieceFile, pieceRank, color);
    }

    @Override
    public boolean canMove(String destination) {
        int currentRank = this.pieceRank;
        int finalRank = Chess.positionRank(destination);
        PieceFile currentFile = this.pieceFile;
        PieceFile finalFile = PieceFile.valueOf(Chess.positionFile(destination));

        //normal move
        if (isNormalMove(currentFile, finalFile, currentRank, finalRank)) {
            return true;
        }

        //capture move
        if (isCaptureMove(currentFile, finalFile, currentRank, finalRank)) {
            return true;
        }

        //first move
        if (isFirstMove(currentFile, finalFile, currentRank, finalRank)) {
            return true;
        }

        return false;
    }

    private boolean isNormalMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
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
        if(Math.abs(currentFile.ordinal() - finalFile.ordinal()) == 1){
            if (this.color.equals("white") && finalRank == currentRank + 1) {
                return true;
            } else if (this.color.equals("black") && finalRank == currentRank - 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isFirstMove(PieceFile currentFile, PieceFile finalFile, int currentRank, int finalRank) {
        if(currentFile == finalFile){
            if (this.color.equals("white") && finalRank == currentRank + 2 && currentRank == 2) {
                return true;
            } else if (this.color.equals("black") && finalRank == currentRank - 2 && currentRank == 7) {
                return true;
            }
        }
        return false;
    }
}