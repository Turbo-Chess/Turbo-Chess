package it.unibo.turbochess.controller.uicontroller.api;

/**
 * Interface for the Game Over screen controller.
 */
@FunctionalInterface
public interface GameOverController {

    /**
     * Sets the text for the status, message, and score labels.
     *
     * @param statusText  the text for the status label.
     * @param messageText the text for the message label.
     * @param scoreText   the text for the score label.
     */
    void setTextLabel(String statusText, String messageText, String scoreText);
}
