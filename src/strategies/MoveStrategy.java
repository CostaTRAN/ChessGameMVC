package strategies;

import models.Position;

/**
 * L'interface MoveStrategy définit les méthodes pour une stratégie de mouvement dans un jeu d'échecs.
 * Les classes implémentant cette interface doivent fournir des méthodes pour vérifier la validité d'un mouvement,
 * obtenir la position actuelle de la pièce et définir une nouvelle position pour la pièce.
 */
public interface MoveStrategy {

    /**
     * Vérifie si un mouvement vers une nouvelle position est valide.
     *
     * @param newPosition la nouvelle position à vérifier.
     * @return true si le mouvement est valide, false sinon.
     */
    public boolean isValidMove(Position newPosition);

    /**
     * Retourne la position actuelle de la pièce.
     *
     * @return la position actuelle de la pièce.
     */
    public Position getPosition();

    /**
     * Définit la position de la pièce.
     *
     * @param position la nouvelle position de la pièce.
     */
    public void setPosition(Position position);
}
