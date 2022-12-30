package nl.jixxed.eliteodysseymaterials.templates.generic;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import nl.jixxed.eliteodysseymaterials.builder.BoxBuilder;
import nl.jixxed.eliteodysseymaterials.builder.LabelBuilder;
import nl.jixxed.eliteodysseymaterials.enums.StorageType;
import nl.jixxed.eliteodysseymaterials.service.LocaleService;

public class MissionIngredient extends Ingredient {
    private final StorageType storageType;
    private final String text;

    private Label nameLabel;
    private HBox line;
    private Region region;

    public MissionIngredient(final String text, final StorageType storageType) {
        this.storageType = storageType;
        this.text = text;
        initComponents();
    }

    private void initComponents() {
        this.getStyleClass().addAll("ingredient", "ingredient-without-amount");
        this.nameLabel = LabelBuilder.builder()
                .withStyleClass("ingredient-name")
                .withText(LocaleService.getStringBinding(this.text))
                .build();
        this.line = BoxBuilder.builder()
                .withNodes(this.nameLabel)
                .buildHBox();
        this.region = new Region();
        HBox.setHgrow(this.region, Priority.ALWAYS);
        this.getChildren().addAll(this.line, this.region);
    }


    @Override
    public StorageType getType() {
        return this.storageType;
    }


    @Override
    public String getName() {
        return this.nameLabel.getText();
    }


}
