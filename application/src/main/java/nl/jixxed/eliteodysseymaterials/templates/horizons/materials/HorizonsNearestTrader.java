package nl.jixxed.eliteodysseymaterials.templates.horizons.materials;

import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nl.jixxed.eliteodysseymaterials.builder.FlowPaneBuilder;
import nl.jixxed.eliteodysseymaterials.builder.LabelBuilder;
import nl.jixxed.eliteodysseymaterials.builder.ResizableImageViewBuilder;
import nl.jixxed.eliteodysseymaterials.domain.Location;
import nl.jixxed.eliteodysseymaterials.enums.HorizonsStorageType;
import nl.jixxed.eliteodysseymaterials.enums.MaterialTrader;
import nl.jixxed.eliteodysseymaterials.enums.NotificationType;
import nl.jixxed.eliteodysseymaterials.service.LocaleService;
import nl.jixxed.eliteodysseymaterials.service.LocationService;
import nl.jixxed.eliteodysseymaterials.service.MaterialTraderService;
import nl.jixxed.eliteodysseymaterials.service.NotificationService;
import nl.jixxed.eliteodysseymaterials.service.event.EventListener;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.LocationChangedEvent;
import nl.jixxed.eliteodysseymaterials.templates.Template;
import nl.jixxed.eliteodysseymaterials.templates.destroyables.DestroyableResizableImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HorizonsNearestTrader extends VBox implements Template {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    static {
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }

    private final HorizonsStorageType type;
    private Label location;
    private Label title;
    private Label distance;
    private String system = "";
    private DestroyableResizableImageView copyIcon;
    private final List<EventListener<?>> eventListeners = new ArrayList<>();

    HorizonsNearestTrader(final HorizonsStorageType type) {
        this.type = type;
        initComponents();
        initEventHandling();
    }

    @Override
    public void initComponents() {
        this.getStyleClass().add("nearest-trader");
        this.title = LabelBuilder.builder().withStyleClass("nearest-trader-title").withText(LocaleService.getStringBinding(this.type.getLocalizationKey())).build();
        this.getChildren().add(this.title);
        this.getChildren().add(getTraderLocation());
        update();
    }

    @Override
    public void initEventHandling() {
        this.eventListeners.add(EventService.addListener(this, LocationChangedEvent.class, locationEvent -> {
            update();
        }));
    }

    private void update() {
        final Location currentLocation = LocationService.getCurrentLocation();
        final MaterialTrader closest = MaterialTraderService.findClosest(currentLocation.getStarSystem(), this.type);
        this.location.setText(closest.getName() + " | " + closest.getStarSystem().getName());
        this.distance.setText("(" + NUMBER_FORMAT.format(MaterialTraderService.getDistance(closest.getStarSystem(), currentLocation.getStarSystem())) + "Ly)");
        this.system = closest.getStarSystem().getName();
    }

    private FlowPane getTraderLocation() {
        this.location = LabelBuilder.builder()
                .withStyleClass("nearest-trader-location")
                .withNonLocalizedText("")
                .build();

        this.distance = LabelBuilder.builder()
                .withStyleClass("nearest-trader-distance")
                .withNonLocalizedText("(0Ly)")
                .build();

        this.copyIcon = ResizableImageViewBuilder.builder()
                .withStyleClass("nearest-trader-copy-icon")
                .withImage("/images/other/copy.png")
                .build();

        return FlowPaneBuilder.builder().withStyleClass("nearest-trader-location-line")
                .withOnMouseClicked(event -> {
                    copyLocationToClipboard();
                    NotificationService.showInformation(NotificationType.COPY, "Clipboard", "System name copied.");
                })
                .withNodes(this.location, new StackPane(this.copyIcon), this.distance)
                .build();

    }

    private void copyLocationToClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(this.system);
        clipboard.setContent(content);
    }
}
