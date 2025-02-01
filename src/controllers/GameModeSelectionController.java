package controllers;

import models.Color;
import models.Game;
import views.GameModeSelectionView;
import views.GameView;
import java.util.Random;

/**
 * La classe GameModeSelectionController gère la sélection du mode de jeu pour une partie d'échecs.
 * Elle interagit avec la vue de sélection du mode de jeu (GameModeSelectionView) et le modèle du jeu (Game)
 * pour traiter les commandes de sélection du mode de jeu.
 */
public class GameModeSelectionController implements ChessController {

    private GameModeSelectionView view;

    /**
     * Constructeur de la classe GameModeSelectionController.
     *
     * @param view la vue de sélection du mode de jeu à associer à ce contrôleur.
     */
    public GameModeSelectionController(GameModeSelectionView view) {
        this.view = view;
    }

    private void setPlayerColor(String input){
        switch (input) {
            case "w":
                Game.setPlayerColor(Color.WHITE);
                break;
            case "b":
                Game.setPlayerColor(Color.BLACK);
                break;
            case "r":
                Random random = new Random();
                Game.setPlayerColor((random.nextInt(5) == 0) ? Color.WHITE : Color.BLACK);
                break;
            default:
                break;
        }
    }

    /**
     * Gère les commandes de sélection du mode de jeu.
     * Cette méthode entre dans une boucle pour demander à l'utilisateur de choisir un mode de jeu
     * jusqu'à ce qu'une commande valide soit entrée.
     */
    @Override
    public void handleCommand(String command) {
        boolean asking = true;
        while (asking) {
            switch (command) {
                case "pvp":
                    this.view.showMessage("Starting Player vs Player game...");
                    GameView gameView = new GameView();
                    Game.getGameInstance().removeObserver(this.view);
                    Game.getGameInstance().addObserver(gameView);
                    Game.getGameInstance().getBoard().initializeBoard();
                    gameView.startGameLoop();
                    asking = false;
                    break;
                case "pva":
                    this.view.showMessage("Starting Player vs AI game...");
                    GameView gameViewAI = new GameView();
                    Game.getGameInstance().setAiEnabled(true);
                    Game.getGameInstance().removeObserver(this.view);
                    Game.getGameInstance().addObserver(gameViewAI);
                    Game.getGameInstance().getBoard().initializeBoard();
                    setPlayerColor(this.view.showColorChoice());
                    gameViewAI.startGameLoop();
                    Game.getGameInstance().setAiEnabled(false);
                    asking = false;
                    break;
                case "exit":
                    System.out.println("Exiting game...");
                    asking = false;
                    return;
                case "help":
                    this.view.showHelp();
                    break;
                default:
                    System.out.println("Invalid command! Please enter 'pvp', 'pva', 'help' or 'exit'.");
                    break;
            }
        }
    }
}
