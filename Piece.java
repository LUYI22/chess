package chess;

public abstract class Piece extends ReturnPiece{
    String color; //white or black

    public Piece (PieceType piecetype, PieceFile pieceFile, int pieceRank, String color){
        super(); 
        this.pieceType = piecetype;
        this.pieceFile = pieceFile;
        this.pieceRank = pieceRank;
        this.color = color;
    }

    public String getColor(){
        return color;
    }

    //check if the piece can move to the destination
    public abstract boolean canMove(String destination);

    //moves the piece to the destination
    public void move(String destination){
        this.pieceFile = PieceFile.valueOf(Chess.positionFile(destination));
        this.pieceRank = Chess.positionRank(destination);
    }
}