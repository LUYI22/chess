package chess;

public abstract class Piece extends ReturnPiece {
    protected String color;
    protected Board board;  // Add this line

    public Piece(PieceType piecetype, PieceFile pieceFile, int pieceRank, String color, Board board) {
        super();
        this.pieceType = piecetype;
        this.pieceFile = pieceFile;
        this.pieceRank = pieceRank;
        this.color = color;
        this.board = board;  // Add this line
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