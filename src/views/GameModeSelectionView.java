package views;

import controllers.GameModeSelectionController;

/**
 * La classe GameModeSelectionView représente la vue pour la sélection du mode de jeu dans un jeu d'échecs.
 * Elle implémente les interfaces ChessView et Observer pour gérer l'affichage et les mises à jour de la vue.
 */
public class GameModeSelectionView implements ChessView, Observer {

    private GameModeSelectionController gameModeSelectionController;

    /**
     * Constructeur de la classe GameModeSelectionView.
     * Initialise le contrôleur de sélection du mode de jeu.
     */
    public GameModeSelectionView() {
        this.gameModeSelectionController = new GameModeSelectionController(this);
    }

    /**
     * Affiche les options de sélection du mode de jeu.
     */
    public void showGameModeSelection() {
        System.out.println("\nWelcome to Chess Game!");
        System.out.println("Available game mode: (type 'help' for commands)");
        System.out.println("pvp : Player vs Player");
        System.out.println("pva : Player vs AI");
        System.out.println("exit : Exit");
        this.gameModeSelectionController.handleCommand();
    }

    /**
     * Met à jour l'affichage de la vue.
     */
    @Override
    public void update() {
        showGameModeSelection();
    }

    /**
     * Affiche l'aide pour le joueur.
     */
    @Override
    public void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("- Start Player vs Player game : pvp");
        System.out.println("- Start Player vs AI game : pva");
        System.out.println("- Show help : help");
        System.out.println("- Exit game : exit");
    }

    /**
     * Affiche un message à l'utilisateur.
     *
     * @param message le message à afficher.
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Affiche un message d'erreur à l'utilisateur.
     *
     * @param message le message d'erreur à afficher.
     */
    @Override
    public void showError(String message) {
        System.out.println("Error: " + message);
    }
}
