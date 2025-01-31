import views.GameModeSelectionView;

/**
 * La classe Main contient le point d'entrée principal de l'application.
 * Elle initialise et met à jour la vue de sélection du mode de jeu.
 */
public class Main {
    /**
     * Le point d'entrée principal de l'application.
     * Initialise et met à jour la vue de sélection du mode de jeu.
     *
     * @param args les arguments de la ligne de commande (non utilisés dans cette application).
     */
    public static void main(String[] args) {
        new GameModeSelectionView().update();
    }
}
