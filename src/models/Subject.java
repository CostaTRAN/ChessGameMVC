package models;

import views.Observer;

/**
 * L'interface Subject définit les méthodes pour gérer les observateurs dans un modèle de conception Observer.
 * Les classes implémentant cette interface doivent fournir des méthodes pour ajouter, retirer et notifier les observateurs.
 */
public interface Subject {

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    public void addObserver(Observer observer);

    /**
     * Retire un observateur de la liste des observateurs.
     *
     * @param observer l'observateur à retirer.
     */
    public void removeObserver(Observer observer);

    /**
     * Notifie tous les observateurs de la mise à jour du sujet.
     */
    public void notifyObservers();
}
