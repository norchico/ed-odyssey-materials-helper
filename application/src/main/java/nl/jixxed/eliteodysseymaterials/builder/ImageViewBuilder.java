package nl.jixxed.eliteodysseymaterials.builder;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.jixxed.eliteodysseymaterials.service.ImageService;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageViewBuilder {
    private final List<String> styleClasses = new ArrayList<>();
    private Image image;
    private boolean preserveRatio = true;
    private final Map<EventType<MouseEvent>, EventHandler<? super MouseEvent>> eventHandlers = new HashMap<>();
    private ObservableValue<? extends Number> fitHeightBinding;

    public static ImageViewBuilder builder() {
        return new ImageViewBuilder();
    }

    public ImageViewBuilder withStyleClass(final String styleClass) {
        this.styleClasses.add(styleClass);
        return this;
    }

    public ImageViewBuilder withPreserveRatio(final boolean preserveRatio) {
        this.preserveRatio = preserveRatio;
        return this;
    }

    public ImageViewBuilder withImage(final Image image) {
        this.image = image;
        return this;
    }

    public ImageViewBuilder withImage(final String imageResource) {
        this.image = ImageService.getImage(imageResource);
        return this;
    }

    public ImageViewBuilder withFitHeightBinding(final ObservableValue<? extends Number> fitHeightBinding) {
        this.fitHeightBinding = fitHeightBinding;
        return this;
    }

    public ImageViewBuilder withStyleClasses(final String... styleClasses) {
        this.styleClasses.addAll(Arrays.asList(styleClasses));
        return this;
    }

    public ImageViewBuilder addEventHandler(final EventType<MouseEvent> mouseEvent, final EventHandler<MouseEvent> eventHandler) {
        this.eventHandlers.put(mouseEvent, eventHandler);
        return this;
    }

    public ImageView build() {
        final ImageView imageView = new ImageView();
        imageView.getStyleClass().addAll(this.styleClasses);
        imageView.setImage(this.image);
        imageView.setPreserveRatio(this.preserveRatio);
        if (this.fitHeightBinding != null) {
            imageView.fitHeightProperty().bind(this.fitHeightBinding);
        }
        if (!this.eventHandlers.isEmpty()) {
            this.eventHandlers.forEach(imageView::addEventHandler);

        }
        return imageView;
    }
}
