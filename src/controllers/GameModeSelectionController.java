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

    /**
     * Définit la couleur du joueur en fonction de l'entrée utilisateur.
     *
     * @param input l'entrée utilisateur pour choisir la couleur.
     */
    private void setPlayerColor(String input) {
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
                System.out.println("Invalid command! Please enter 'w', 'b' or 'r'.");
                this.setPlayerColor(this.view.showColorChoice());
                break;
        }
    }

    /**
     * Gère les commandes de sélection du mode de jeu.
     * Cette méthode entre dans une boucle pour demander à l'utilisateur de choisir un mode de jeu
     * jusqu'à ce qu'une commande valide soit entrée.
     *
     * @param command la commande entrée par l'utilisateur.
     */
    @Override
    public void handleCommand(String command) {
        switch (command) {
            case "pvp":
                this.view.showMessage("Starting Player vs Player game...");
                GameView gameView = new GameView();
                Game.getGameInstance().removeObserver(this.view);
                Game.getGameInstance().addObserver(gameView);
                Game.getGameInstance().getBoard().initializeBoard();
                gameView.startGameLoop();
                break;
            case "pva":
                this.view.showMessage("Starting Player vs AI game...");
                GameView gameViewAI = new GameView();
                Game.getGameInstance().setAiEnabled(true);
                Game.getGameInstance().removeObserver(this.view);
                Game.getGameInstance().addObserver(gameViewAI);
                Game.getGameInstance().getBoard().initializeBoard();
                this.setPlayerColor(this.view.showColorChoice());
                gameViewAI.startGameLoop();
                Game.getGameInstance().setAiEnabled(false);
                break;
            case "exit":
                System.out.println("Exiting game...");
                return;
            case "help":
                this.view.showHelp();
                break;
            default:
                System.out.println("Invalid command! Please enter 'pvp', 'pva', 'help' or 'exit'.");
                this.view.showGameModeSelection();
                break;
        }
    }
}
