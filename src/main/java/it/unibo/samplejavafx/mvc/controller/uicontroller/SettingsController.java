package it.unibo.samplejavafx.mvc.controller.uicontroller;

import it.unibo.samplejavafx.mvc.controller.coordinator.GameCoordinator;
import it.unibo.samplejavafx.mvc.model.settings.GameSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;

/**
 * Controller for the Settings scene.
 */
public final class SettingsController {
    private static final String UNIT_MINUTES = "Minuti";
    private static final String UNIT_SECONDS = "Secondi";

    private final GameCoordinator coordinator;

    @FXML
    private ChoiceBox<String> timeUnitChoice;

    @FXML
    private Spinner<Integer> timeValueSpinner;

    @FXML
    private Label statusLabel;

    /**
     * Constructor.
     *
     * @param coordinator the game coordinator
     */
    public SettingsController(final GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @FXML
    public void initialize() {
        timeUnitChoice.getItems().setAll(UNIT_MINUTES, UNIT_SECONDS);
        timeUnitChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                final String sourceUnit = oldValue != null ? oldValue : newValue;
                configureSpinner(newValue, getSecondsForUnit(sourceUnit));
            }
        });

        applyNumericTextFormatter();
        loadFromCoordinator();
    }

    /**
     * Handles the "Back" button action.
     *
     * @param e the action event
     */
    public void backToMenu(final ActionEvent e) {
        this.coordinator.initMainMenu();
    }

    @FXML
    public void onSaveTime(final ActionEvent e) {
        statusLabel.setText("");

        final String unit = timeUnitChoice.getValue();
        final Integer value = readSpinnerValue();
        if (unit == null || value == null) {
            statusLabel.setText("Valore non valido.");
            return;
        }

        final long seconds = UNIT_MINUTES.equals(unit) ? value.longValue() * 60L : value.longValue();
        if (seconds < GameSettings.MIN_INITIAL_TIME_SECONDS || seconds > GameSettings.MAX_INITIAL_TIME_SECONDS) {
            statusLabel.setText("Inserisci un valore tra "
                    + formatSeconds(GameSettings.MIN_INITIAL_TIME_SECONDS) + " e "
                    + formatSeconds(GameSettings.MAX_INITIAL_TIME_SECONDS) + ".");
            return;
        }

        coordinator.setInitialTimeSeconds(seconds);
        statusLabel.setText("Tempo salvato: " + formatSeconds(seconds) + ".");
    }

    @FXML
    public void onResetTime(final ActionEvent e) {
        coordinator.resetInitialTimeSeconds();
        loadFromCoordinator();
        statusLabel.setText("Ripristinato il valore predefinito.");
    }

    private void loadFromCoordinator() {
        final long seconds = coordinator.getInitialTimeSeconds();
        final String unit = seconds % 60 == 0 ? UNIT_MINUTES : UNIT_SECONDS;
        timeUnitChoice.setValue(unit);
        configureSpinner(unit, seconds);
        statusLabel.setText("");
    }

    private void configureSpinner(final String unit, final long seconds) {
        if (UNIT_MINUTES.equals(unit)) {
            final int min = 1;
            final int max = Math.toIntExact(GameSettings.MAX_INITIAL_TIME_SECONDS / 60);
            final int minutes = clampInt((seconds + 59) / 60, min, max);
            timeValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, minutes));
        } else {
            final int min = Math.toIntExact(GameSettings.MIN_INITIAL_TIME_SECONDS);
            final int max = Math.toIntExact(GameSettings.MAX_INITIAL_TIME_SECONDS);
            final int value = clampInt(seconds, min, max);
            timeValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value));
        }
    }

    private long getSecondsForUnit(final String unit) {
        final Integer value = readSpinnerValue();
        if (unit == null || value == null) {
            return coordinator.getInitialTimeSeconds();
        }
        return UNIT_MINUTES.equals(unit) ? value.longValue() * 60L : value.longValue();
    }

    private Integer readSpinnerValue() {
        final String raw = timeValueSpinner.getEditor().getText();
        if (raw == null || raw.isBlank()) {
            return timeValueSpinner.getValue();
        }
        try {
            final int parsed = Integer.parseInt(raw.trim());
            final var vf = timeValueSpinner.getValueFactory();
            if (vf instanceof SpinnerValueFactory.IntegerSpinnerValueFactory ivf) {
                final int clamped = Math.max(ivf.getMin(), Math.min(ivf.getMax(), parsed));
                ivf.setValue(clamped);
                return clamped;
            }
            if (vf != null) {
                vf.setValue(parsed);
            }
            return parsed;
        } catch (final NumberFormatException ex) {
            return null;
        }
    }

    private void applyNumericTextFormatter() {
        final UnaryOperator<TextFormatter.Change> filter = change -> {
            final String text = change.getControlNewText();
            return text.matches("\\d*") ? change : null;
        };
        timeValueSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, filter));
    }

    private static int clampInt(final long value, final int min, final int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return (int) value;
    }

    private static String formatSeconds(final long seconds) {
        final long min = seconds / 60;
        final long sec = seconds % 60;
        return String.format("%d:%02d", min, sec);
    }
}
