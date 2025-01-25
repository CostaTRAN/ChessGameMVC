package controllers;

import java.util.Scanner;

import models.Board;
import models.Game;
import views.GameModeSelectionView;
import views.GameView;

public class GameModeSelectionController {

    private GameModeSelectionView view;
    private Scanner scanner;

    public GameModeSelectionController(GameModeSelectionView view) {
        this.view = view;
        this.scanner = new Scanner(System.in);
    }
    
    public void handleCommand() {
        boolean asking = true;
        while(asking) {
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "pvp":
                    this.view.showMessage("Starting Player vs Player game...");
                    GameView gameView = new GameView();
                    Game.getGameInstance().removeObserver(this.view);
                    Game.getGameInstance().addObserver(gameView);
                    Board.getBoardInstance().initializeBoard();
                    gameView.startGameLoop();
                    asking = false;
                    break;
                case "pva":
                    System.out.println("Starting Player vs AI game...");
                    asking = false;
                    break;
                case "load":
                    System.out.println("Loading saved game...");
                    asking = false;
                    break;
                case "exit":
                    System.out.println("Exiting game...");
                    asking = false;
                    break;
                case "help":
                    this.view.showHelp();
                    break;
                default:
                    System.out.println("Invalid command! Please enter 'pvp', 'pva', 'load', 'help' or 'exit'.");
                    break;
            }
        }
    }
}
