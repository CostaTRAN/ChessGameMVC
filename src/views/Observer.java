package views;

/**
 * L'interface Observer définit la méthode pour mettre à jour un observateur dans un modèle de conception Observer.
 * Les classes implémentant cette interface doivent fournir une méthode pour mettre à jour l'état de l'observateur.
 */
public interface Observer {

    /**
     * Met à jour l'état de l'observateur.
     */
    public void update();
}
