package controllers;

import java.util.Scanner;
import models.Game;
import views.GameModeSelectionView;
import views.GameView;

/**
 * La classe GameModeSelectionController gère la sélection du mode de jeu pour une partie d'échecs.
 * Elle interagit avec la vue de sélection du mode de jeu (GameModeSelectionView) et le modèle du jeu (Game)
 * pour traiter les commandes de sélection du mode de jeu.
 */
public class GameModeSelectionController {

    private GameModeSelectionView view;
    private Scanner scanner;

    /**
     * Constructeur de la classe GameModeSelectionController.
     *
     * @param view la vue de sélection du mode de jeu à associer à ce contrôleur.
     */
    public GameModeSelectionController(GameModeSelectionView view) {
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Gère les commandes de sélection du mode de jeu.
     * Cette méthode entre dans une boucle pour demander à l'utilisateur de choisir un mode de jeu
     * jusqu'à ce qu'une commande valide soit entrée.
     */
    public void handleCommand() {
        boolean asking = true;
        while (asking) {
            String command = this.scanner.nextLine().trim().toLowerCase();
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
                    System.out.println("Starting Player vs AI game...");
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
                    System.out.println("Invalid command! Please enter 'pvp', 'pva', 'help' or 'exit'.");
                    break;
            }
        }
    }
}
