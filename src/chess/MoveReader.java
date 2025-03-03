package chess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MoveReader {

    public static void main(String[] args) {
        String filePath;
        if (args.length > 0) {
            filePath = args[0];
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the path to the moves file: ");
            filePath = sc.nextLine();
            sc.close();
        }
        readMovesFromFile(filePath);
    }

    public static void readMovesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Chess.start();
            boolean isWhiteTurn = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] moves = line.split(" ");
                for (int i = 0; i < moves.length; i += 2) {
                    if (i + 1 >= moves.length) {
                        System.out.println("Invalid move format in file.");
                        return;
                    }

                    String from = moves[i];
                    String to = moves[i + 1];
                    String move = from + " " + to;

                    ReturnPlay result = Chess.play(move);
                    System.out.println((isWhiteTurn ? "White" : "Black") + " move: " + move);
                    System.out.println("Result: " + result.message);
                    printBoard(result.piecesOnBoard);

                    isWhiteTurn = !isWhiteTurn;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printBoard(ArrayList<ReturnPiece> pieces) {
        String[][] board = makeBlankBoard();
        if (pieces != null) {
            printPiecesOnBoard(pieces, board);
        }
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println(8 - r);
        }
        System.out.println(" a  b  c  d  e  f  g  h");
    }

    public static String[][] makeBlankBoard() {
        String[][] board = new String[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (r % 2 == 0) {
                    board[r][c] = c % 2 == 0 ? "  " : "##";
                } else {
                    board[r][c] = c % 2 == 0 ? "##" : "  ";
                }
            }
        }
        return board;
    }

    public static void printPiecesOnBoard(ArrayList<ReturnPiece> pieces, String[][] board) {
        for (ReturnPiece rp : pieces) {
            int file = ("" + rp.pieceFile).charAt(0) - 'a';
            String pieceStr = "" + rp.pieceType;
            String ppstr = "";
            ppstr += Character.toLowerCase(pieceStr.charAt(0));
            ppstr += pieceStr.charAt(1) == 'P' ? 'p' : pieceStr.charAt(1);
            board[8 - rp.pieceRank][file] = ppstr;
        }
    }
}