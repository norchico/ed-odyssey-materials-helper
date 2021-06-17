package nl.jixxed.eliteodysseymaterials.templates;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import jfxtras.styles.jmetro.JMetroStyleClass;
import nl.jixxed.eliteodysseymaterials.AppConstants;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.JournalProcessedEvent;

import java.io.File;

public class BottomBar extends HBox {

    final Label watchedFileLabel = new Label();
    final Label lastTimeStampLabel = new Label();

    public BottomBar() {
        super();
        this.getChildren().addAll(this.watchedFileLabel, new Separator(Orientation.VERTICAL), this.lastTimeStampLabel);
        this.getStyleClass().add("bottom-bar");
        this.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        final File watchedFolder = new File(AppConstants.WATCHED_FOLDER);

        this.watchedFileLabel.setText("None - No Odyssey journals found at " + watchedFolder.getAbsolutePath());

        EventService.addListener(JournalProcessedEvent.class, journalProcessedEvent -> {
            Platform.runLater(() -> {
                this.watchedFileLabel.setText("Watching: " + journalProcessedEvent.getFile().getAbsolutePath());
                this.lastTimeStampLabel.setText("Latest observed relevant message: " + journalProcessedEvent.getTimestamp() + " (" + journalProcessedEvent.getJournalEventType().friendlyName() + ")");
            });
        });
    }
}
