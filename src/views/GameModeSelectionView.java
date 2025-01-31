package views;

import controllers.GameModeSelectionController;

public class GameModeSelectionView implements ChessView, Observer {

    private GameModeSelectionController gameModeSelectionController;

    public GameModeSelectionView() {
        this.gameModeSelectionController = new GameModeSelectionController(this);
    }
    
    public void showGameModeSelection() {
        System.out.println("\nWelcome to Chess Game!");
        System.out.println("Available game mode: (type 'help' for commands)");
        System.out.println("pvp : Player vs Player");
        System.out.println("pva : Player vs AI");
        System.out.println("exit : Exit");
        this.gameModeSelectionController.handleCommand();
    }

    @Override
    public void update() {
        showGameModeSelection();
    }

    @Override
    public void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("- Start Player vs Player game : pvp");
        System.out.println("- Start Player vs AI game : pva");
        System.out.println("- Show help : help");
        System.out.println("- Exit game : exit");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        System.out.println("Error: " + message);
    }
}
