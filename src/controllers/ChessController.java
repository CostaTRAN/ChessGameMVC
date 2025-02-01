package controllers;

/**
 * L'interface ChessController définit une méthode pour gérer les commandes dans un jeu d'échecs.
 * Les implémentations de cette interface doivent fournir la logique pour traiter diverses commandes
 * liées au jeu.
 */
public interface ChessController {

    /**
     * Gère une commande dans le jeu d'échecs.
     *
     * @param command la commande à gérer, représentée sous forme de chaîne de caractères.
     */
    public void handleCommand(String command);
}