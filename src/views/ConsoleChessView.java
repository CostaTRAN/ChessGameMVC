package views;

import models.*;

import java.util.Scanner;

import controllers.ChessController;

import java.util.List;

public class ConsoleChessView implements ChessView, Observer {
    private Game game;
    private Scanner scanner;
    private static String ANSI_RESET = "\u001B[0m";
    private static String ANSI_WHITE = "\u001B[37m";
    private static String ANSI_BLACK = "\u001B[30m";
    private static String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static String ANSI_GRAY_BACKGROUND = "\u001B[100m";

    public ConsoleChessView(Game game) {
        this.game = game;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void updateBoard() {
        clearScreen();
        printMoveHistory();
        printGameInfo();
        printBoard();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printGameInfo() {
        System.out.println("\nChess Game");
        System.out.println("==========");
        System.out.println("Current turn: " + game.getCurrentTurn());
        if (game.getStatus() != GameStatus.ACTIVE) {
            System.out.println("Game Status: " + game.getStatus());
        }
        System.out.println();
    }

    private void printBoard() {
        Board board = game.getBoard();
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
/* 
    private String getPieceSymbol(Piece piece) {
        return switch (piece.getType()) {
            case PAWN -> "P";
            case ROOK -> "T";
            case KNIGHT -> "C";
            case BISHOP -> "F";
            case QUEEN -> "D";
            case KING -> "K";
        };
    }
*/
    private void printMoveHistory() {
        List<String> notation = game.getMoveNotation();
        if (!notation.isEmpty()) {
            System.out.println("\nMove History:");
            for (String move : notation) {
                System.out.println(move);
            }
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println("\n" + message);
    }

    @Override
    public void showError(String message) {
        System.err.println("\nError: " + message);
    }

    public void startGameLoop() {
        ChessController controller = new ChessController(game, this);
        game.notifyObservers();

        while (game.getStatus() != GameStatus.CHECKMATE && 
               game.getStatus() != GameStatus.STALEMATE) {
            
            System.out.print("\nEnter command (move: 'e2 e4', or type 'help'): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "quit", "exit" -> {
                    System.out.println("Game ended by player.");
                    game.removeObserver(this);
                    game.addObserver(new GameModeSelectionView());
                    game.notifyObservers();
                    game = null;
                    return;
                }
                case "help" -> showHelp();
                case "undo" -> {
                    game.undoMove();
                    game.notifyObservers();
                }
                case "save" -> {
                    try {
                        game.saveGame();
                        showMessage("Game saved successfully!");
                    } catch (Exception e) {
                        showError("Failed to save game: " + e.getMessage());
                    }
                }
                case "load" -> {
                    try {
                        game = Game.loadGame();
                        showMessage("Game loaded successfully!");
                        // Update game reference and refresh view
                        game.notifyObservers();
                    } catch (Exception e) {
                        showError("Failed to load game: " + e.getMessage());
                    }
                }
                default -> controller.handleCommand(input);
            }
        }

        // Game over
        game.notifyObservers();
        printGameOverMessage();
    }

    private void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("- Move a piece: e2 e4 (from square to square)");
        System.out.println("- Undo last move: undo");
        System.out.println("- Save game: save");
        System.out.println("- Load game: load");
        System.out.println("- Show this help: help");
        System.out.println("- Quit game: quit or exit");
        System.out.println("\nSquare notation:");
        System.out.println("- Files (columns): a-h");
        System.out.println("- Ranks (rows): 1-8");
        System.out.println("Example: e2 e4 moves the piece from e2 to e4");
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
                choice = Integer.parseInt(scanner.nextLine().trim());
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

    private void printGameOverMessage() {
        System.out.println("\nGame Over!");
        if (game.getStatus() == GameStatus.CHECKMATE) {
            Color winner = game.getCurrentTurn() == Color.WHITE ? Color.BLACK : Color.WHITE;
            System.out.println("Checkmate! " + winner + " wins!");
        } else if (game.getStatus() == GameStatus.STALEMATE) {
            System.out.println("Stalemate! The game is a draw.");
        }
    }

    @Override
    public void update() {
        updateBoard();
    }
}