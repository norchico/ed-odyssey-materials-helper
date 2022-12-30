package nl.jixxed.eliteodysseymaterials.templates.dialog;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.jixxed.eliteodysseymaterials.FXApplication;
import nl.jixxed.eliteodysseymaterials.builder.BoxBuilder;
import nl.jixxed.eliteodysseymaterials.builder.ButtonBuilder;
import nl.jixxed.eliteodysseymaterials.builder.HyperlinkBuilder;
import nl.jixxed.eliteodysseymaterials.builder.LabelBuilder;
import nl.jixxed.eliteodysseymaterials.helper.ScalingHelper;
import nl.jixxed.eliteodysseymaterials.service.LocaleService;
import nl.jixxed.eliteodysseymaterials.templates.Template;

@Slf4j
public class VersionDialog extends VBox implements Template {
    private final Stage stage;
    private final Hyperlink link;

    public VersionDialog(final Stage stage, final FXApplication fxApplication) {
        super();
        this.stage = stage;
        this.link = HyperlinkBuilder.builder().withStyleClass("version-dialog-download-link").withText(LocaleService.getStringBinding("version.dialog.download")).withAction(actionEvent -> {
            fxApplication.getHostServices().showDocument("https://github.com/jixxed/ed-odyssey-materials-helper/releases/latest");
            System.exit(0);
        }).build();
        this.initComponents();
        this.initEventHandling();
    }

    @Override
    public void initComponents() {
        //labels
        final Label explain = LabelBuilder.builder().withStyleClass("version-dialog-text").withText(LocaleService.getStringBinding("version.dialog.text")).build();
        final Label explain2 = LabelBuilder.builder().withStyleClass("version-dialog-text").withText(LocaleService.getStringBinding("version.dialog.text2")).build();

        //buttons
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        final Region regionV = new Region();
        VBox.setVgrow(regionV, Priority.ALWAYS);
        final HBox buttons = BoxBuilder.builder().withNodes(region,
                ButtonBuilder.builder().withText(LocaleService.getStringBinding("version.dialog.ok")).withOnAction(event -> {
                    this.stage.close();
                }).build()
        ).buildHBox();
        buttons.spacingProperty().bind(ScalingHelper.getPixelDoubleBindingFromEm(0.25));
        this.getStyleClass().add("urlscheme-dialog");
        this.getChildren().addAll(explain, explain2, this.link, regionV, buttons);
    }

    @Override
    public void initEventHandling() {
        //NOOP
    }
}
