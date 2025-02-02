package views;

import java.util.Scanner;
import controllers.GameModeSelectionController;

/**
 * La classe GameModeSelectionView représente la vue pour la sélection du mode de jeu dans un jeu d'échecs.
 * Elle implémente les interfaces ChessView et Observer pour gérer l'affichage et les mises à jour de la vue.
 */
public class GameModeSelectionView implements ChessView, Observer {

    private GameModeSelectionController gameModeSelectionController;
    private Scanner scanner;

    /**
     * Constructeur de la classe GameModeSelectionView.
     * Initialise le contrôleur de sélection du mode de jeu et le scanner pour les entrées utilisateur.
     */
    public GameModeSelectionView() {
        this.gameModeSelectionController = new GameModeSelectionController(this);
        this.scanner = new Scanner(System.in);
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
        String command = this.scanner.nextLine().trim().toLowerCase();
        this.gameModeSelectionController.handleCommand(command);
    }

    /**
     * Affiche les options de choix de couleur pour le joueur.
     *
     * @return la commande de choix de couleur entrée par l'utilisateur.
     */
    public String showColorChoice() {
        System.out.println("Choose a Color");
        System.out.println("w : White");
        System.out.println("b : Black");
        System.out.println("r : Random");
        String command = this.scanner.nextLine().trim().toLowerCase();
        return command;
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
        System.out.println("- Quit game : quit or exit");
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
