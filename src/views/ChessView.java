package views;

/**
 * L'interface ChessView définit les méthodes pour la vue d'un jeu d'échecs.
 * Les classes implémentant cette interface doivent fournir des méthodes pour mettre à jour l'affichage,
 * afficher de l'aide, des messages et des erreurs.
 */
public interface ChessView {

    /**
     * Met à jour l'affichage de la vue.
     */
    void update();

    /**
     * Affiche l'aide pour le joueur.
     */
    void showHelp();

    /**
     * Affiche un message à l'utilisateur.
     *
     * @param message le message à afficher.
     */
    void showMessage(String message);

    /**
     * Affiche un message d'erreur à l'utilisateur.
     *
     * @param message le message d'erreur à afficher.
     */
    void showError(String message);
}
