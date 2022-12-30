package nl.jixxed.eliteodysseymaterials.templates.odyssey.trade;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import nl.jixxed.eliteodysseymaterials.enums.OdysseyMaterial;
import nl.jixxed.eliteodysseymaterials.enums.OdysseyStorageType;
import nl.jixxed.eliteodysseymaterials.enums.TradeOdysseyMaterial;
import nl.jixxed.eliteodysseymaterials.service.LocaleService;
import nl.jixxed.eliteodysseymaterials.service.event.EventListener;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.StorageEvent;
import nl.jixxed.eliteodysseymaterials.templates.odyssey.OdysseyMaterialIngredient;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S2160")
class OdysseyTradeIngredient extends OdysseyMaterialIngredient {

    private static final String INGREDIENT_FILLED_CLASS = "trade-ingredient-filled";
    private static final String INGREDIENT_UNFILLED_CLASS = "trade-ingredient-unfilled";
    private final Boolean isGiven;

    private final List<EventListener<?>> eventListeners = new ArrayList<>();
    OdysseyTradeIngredient(final OdysseyStorageType storageType, final OdysseyMaterial odysseyMaterial, final Integer amountAvailable, final Integer tradeAmount, final boolean isGiven) {
        super(storageType, odysseyMaterial, amountAvailable, tradeAmount);
        this.isGiven = isGiven;
        initComponents();
        initEventHandling();
    }

    @SuppressWarnings("java:S2177")
    private void initComponents() {
        this.getStyleClass().add("trade-ingredient");
        setLeftDescriptionLabel(LocaleService.getStringBinding("blueprint.header.available"));
        if (Boolean.TRUE.equals(this.isGiven)) {
            setRightDescriptionLabel(LocaleService.getStringBinding("blueprint.header.give"));
        } else {
            setRightDescriptionLabel(LocaleService.getStringBinding("blueprint.header.receive"));
        }

        final boolean isNothing = getOdysseyMaterial().equals(TradeOdysseyMaterial.NOTHING);
        getLeftAmountLabel().setVisible(!isNothing);
        getRightAmountLabel().setVisible(!isNothing);
        getLeftDescriptionLabel().setVisible(!isNothing);
        getRightDescriptionLabel().setVisible(!isNothing);
        update();
    }

    @SuppressWarnings("java:S2177")
    private void initEventHandling() {
        if (Boolean.TRUE.equals(this.isGiven)) {
            this.eventListeners.add(EventService.addListener(this, StorageEvent.class, storageEvent -> this.update()));
        }
    }

    @Override
    protected void update() {
        if (Boolean.TRUE.equals(this.isGiven)) {
            this.getRightAmountLabel().setText(this.getRightAmount().toString());
            if (this.getLeftAmount() >= Integer.parseInt(this.getRightAmountLabel().getText())) {
                setCanTrade(true);
                this.getStyleClass().removeAll(INGREDIENT_FILLED_CLASS, INGREDIENT_UNFILLED_CLASS);
                this.getStyleClass().addAll(INGREDIENT_FILLED_CLASS);
            } else {
                setCanTrade(false);
                this.getStyleClass().removeAll(INGREDIENT_FILLED_CLASS, INGREDIENT_UNFILLED_CLASS);
                this.getStyleClass().addAll(INGREDIENT_UNFILLED_CLASS);
            }
        }
    }

    private BooleanProperty canTrade;

    private void setCanTrade(final boolean value) {
        canTradeProperty().set(value);
    }

    final BooleanProperty canTradeProperty() {
        if (this.canTrade == null) {
            this.canTrade = new SimpleBooleanProperty(true) {

                @Override
                public Object getBean() {
                    return OdysseyTradeIngredient.this;
                }

                @Override
                public String getName() {
                    return "canTrade";
                }
            };
        }
        return this.canTrade;
    }
}
