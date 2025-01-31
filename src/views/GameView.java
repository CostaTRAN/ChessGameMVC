package views;

import models.*;

import java.util.ArrayList;
import java.util.Scanner;

import controllers.GameController;

public class GameView implements ChessView, Observer {
    private GameController gameController;
    private Scanner scanner;
    private String ANSI_RESET = "\u001B[0m";
    private String ANSI_WHITE = "\u001B[37m";
    private String ANSI_BLACK = "\u001B[30m";
    private String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private String ANSI_GRAY_BACKGROUND = "\u001B[100m";

    public GameView() {
        this.gameController = new GameController(this);
        this.scanner = new Scanner(System.in);
    }

    public void updateBoard() {
        this.printMoveHistory();
        this.printGameInfo();
        this.printBoard();
    }

    private void printGameInfo() {
        System.out.println("\nChess Game");
        System.out.println("==========");
        System.out.println("Current turn: " + Game.getGameInstance().getCurrentTurn());
        if (Game.getGameInstance().getStatus() != GameStatus.ACTIVE) {
            System.out.println("Game Status: " + Game.getGameInstance().getStatus());
        }
        System.out.println();
    }

    private void printBoard() {
        Board board = Game.getGameInstance().getBoard();
        System.out.println("    a  b  c  d  e  f  g  h");
        System.out.println("  --------------------------");
        
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " |");
            
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                
                // Alternate background colors for checker pattern
                String background = (row + col) % 2 == 0 ? 
                    ANSI_WHITE_BACKGROUND : ANSI_GRAY_BACKGROUND;
                
                if (piece == null) {
                    System.out.print(background + "   " + ANSI_RESET);
                } else {
                    String pieceColor = piece.getColor() == Color.WHITE ? 
                        ANSI_WHITE : ANSI_BLACK;
                    System.out.print(background + pieceColor + 
                        " " + piece + ANSI_RESET);
                }
            }
            System.out.println("| " + (row + 1));
        }
        
        System.out.println("  --------------------------");
        System.out.println("    a  b  c  d  e  f  g  h");
    }

    private void printMoveHistory() {
        ArrayList<String> notation = Game.getGameInstance().getMoveNotation();
        if (!notation.isEmpty()) {
            System.out.println("\nMove History:");
            for (String move : notation) {
                System.out.println(move);
            }
        }
    }

    public void startGameLoop() {
        updateBoard();
        while (Game.getGameInstance().getStatus() != GameStatus.CHECKMATE && 
            Game.getGameInstance().getStatus() != GameStatus.STALEMATE) {
            System.out.print("\nEnter command (move: 'e2 e4', or type 'help'): ");
            String input = this.scanner.nextLine().trim().toLowerCase();
            this.gameController.handleCommand(input);
        }
    }

    public PieceType askPromotionPawn() {
        System.out.println("Pawn promotion! Choose a piece to promote to:");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");

        int choice = -1;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter your choice (1-4): ");
            try {
                choice = Integer.parseInt(this.scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                showError("Invalid input! Please enter a number between 1 and 4.");
            }
        }

        return switch (choice) {
            case 1 -> PieceType.QUEEN;
            case 2 -> PieceType.ROOK;
            case 3 -> PieceType.BISHOP;
            case 4 -> PieceType.KNIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    @Override
    public void update() {
        updateBoard();
    }

    @Override
    public void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("- Move a piece: e2 e4 (from square to square)");
        System.out.println("- Undo last move: undo");
        System.out.println("- Show help: help");
        System.out.println("- Quit game: quit or exit");
        System.out.println("\nSquare notation:");
        System.out.println("- Files (columns): a-h");
        System.out.println("- Ranks (rows): 1-8");
        System.out.println("Example: e2 e4 moves the piece from e2 to e4");
    }

    @Override
    public void showMessage(String message) {
        System.out.println("\n" + message);
    }

    @Override
    public void showError(String message) {
        System.err.println("\nError: " + message);
    }
}