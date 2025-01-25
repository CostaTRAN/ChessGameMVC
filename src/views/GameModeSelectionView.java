package views;

import java.util.Scanner;

import controllers.ChessController;
import models.Game;

public class GameModeSelectionView implements ChessView, Observer {

    private ChessController controller;
    private Scanner scanner;

    public GameModeSelectionView() {
        this.controller = new ChessController(this);
        this.scanner = new Scanner(System.in);
    }
    
    public void showGameModeSelection() {
        System.out.println("Select game mode:");
        System.out.println("1. Player vs Player");
        System.out.println("2. Player vs AI");
        System.out.println("3. Load saved game");
        System.out.println("4. Exit");

        int choice = 0;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Starting Player vs Player game...");
                        Game game = Game.getGameInstance();
                        ConsoleChessView view = new ConsoleChessView(this.controller);
                        game.removeObserver(this);
                        game.addObserver(view);
                        game.getBoard().initializeBoard();
                        view.startGameLoop();
                        break;
                    case 2:
                        System.out.println("Starting Player vs AI game...");
                        break;
                    case 3:
                        System.out.println("Loading saved game...");
                        break;
                    case 4:
                        System.out.println("Exiting game...");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 4.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void updateBoard() {
        // Not used in this view
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    @Override
    public void update() {
        //clearScreen();
        showGameModeSelection();
    }
}
