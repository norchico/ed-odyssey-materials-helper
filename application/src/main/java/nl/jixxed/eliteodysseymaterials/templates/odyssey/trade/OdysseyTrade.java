package nl.jixxed.eliteodysseymaterials.templates.odyssey.trade;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.jixxed.eliteodysseymaterials.builder.ButtonBuilder;
import nl.jixxed.eliteodysseymaterials.domain.StarSystem;
import nl.jixxed.eliteodysseymaterials.domain.TradeSpec;
import nl.jixxed.eliteodysseymaterials.enums.NotificationType;
import nl.jixxed.eliteodysseymaterials.enums.OdysseyMaterial;
import nl.jixxed.eliteodysseymaterials.enums.TradeStatus;
import nl.jixxed.eliteodysseymaterials.enums.TradeType;
import nl.jixxed.eliteodysseymaterials.service.LocaleService;
import nl.jixxed.eliteodysseymaterials.service.NotificationService;
import nl.jixxed.eliteodysseymaterials.service.event.EventListener;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.trade.XMessageWebSocketEvent;
import nl.jixxed.eliteodysseymaterials.trade.message.common.XMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
abstract class OdysseyTrade extends FlowPane {
    @EqualsAndHashCode.Include
    private String offerId;
    private OdysseyMaterial offerOdysseyMaterial;
    private OdysseyMaterial receiveOdysseyMaterial;
    private Integer offerAmount;
    private Integer receiveAmount;
    private Button contactButton;
    private StarSystem starSystem;
    private TradeSpec tradeSpec;

    protected OdysseyTradeIngredient offerIngredient;
    protected OdysseyTradeIngredient receiveIngredient;

    private final List<EventListener<?>> eventListeners = new ArrayList<>();
    @Setter
    private Optional<Consumer<TradeSpec>> callback = Optional.empty();
    private Stage chatStage;

    OdysseyTrade(final TradeSpec tradeSpec) {
        this.offerId = tradeSpec.getOfferId();
        this.offerOdysseyMaterial = tradeSpec.getOfferOdysseyMaterial();
        this.receiveOdysseyMaterial = tradeSpec.getReceiveOdysseyMaterial();
        this.offerAmount = tradeSpec.getOfferAmount();
        this.receiveAmount = tradeSpec.getReceiveAmount();
        this.starSystem = tradeSpec.getStarSystem();
        this.tradeSpec = tradeSpec;
        initComponents();
        initEventHandling();
        this.setPrefWrapLength(300);
        tradeSpec.setCallback(Optional.of(tradeSpec1 -> handleTradeSpecStatus(tradeSpec.getTradeStatus())));
    }

    void handleTradeSpecStatus(final TradeStatus tradeStatus) {
        Platform.runLater(() -> {
            switch (tradeStatus) {
                case AVAILABLE -> available();
                case PUSHED -> pushed();
                case OFFLINE -> offline();
                case REMOVED -> removed();
                case PULLED -> pulled();
                case ACCEPTED -> accepted();
                case REJECTED -> rejected();
            }
            this.callback.ifPresent(c -> c.accept(this.tradeSpec));
        });
    }

    protected abstract void available();

    protected abstract void pushed();

    protected abstract void accepted();

    protected void offline() {
        //NOOP
    }

    protected void removed() {
        //NOOP
    }

    protected void rejected() {
        //NOOP
    }

    protected void pulled() {
        //NOOP
    }

    private void initEventHandling() {
        this.eventListeners.add(EventService.addListener(this, XMessageWebSocketEvent.class, xMessageWebSocketEvent -> {
            final XMessage message = xMessageWebSocketEvent.getXMessageMessage().getMessage();
            if (message.getOfferId().equals(this.offerId)) {
                if (this.chatStage != null && this.chatStage.isShowing()) {
                    this.chatStage.toFront();
                }
                if (this.chatStage == null || !this.chatStage.isFocused()) {
                    NotificationService.showInformation(NotificationType.TRADE, LocaleService.getLocalizedStringForCurrentLocale("notification.trade.message.from", message.getInfo().getNickname()), message.getText());
                }
            }
        }));
    }

    private void initComponents() {
        this.contactButton = ButtonBuilder.builder()
                .withText(LocaleService.getStringBinding("trade.button.chat"))
                .withOnAction(event -> createModal())
                .build();
    }

    private void createModal() {
        this.chatStage = new Stage();
        final String otherPartyHash = TradeType.REQUEST.equals(this.tradeSpec.getTradeType()) ? this.tradeSpec.getOwnerHash() : this.tradeSpec.getAcceptedTokenHash();
        final OdysseyChatDialog chatDialog = new OdysseyChatDialog(this.chatStage, this.tradeSpec.getChat(), this.offerId, otherPartyHash);
        final Scene chatScene = new Scene(chatDialog, 640, 480);
        this.chatStage.initModality(Modality.WINDOW_MODAL);
        final JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(chatScene);
        chatScene.getStylesheets().add(getClass().getResource("/nl/jixxed/eliteodysseymaterials/style/style.css").toExternalForm());
        this.contactButton.setDisable(true);
        this.chatStage.setScene(chatScene);
        this.chatStage.titleProperty().bind(LocaleService.getStringBinding("trade.chat.title", LocaleService.getLocalizedStringForCurrentLocale(this.offerOdysseyMaterial.getLocalizationKey()), this.offerAmount, LocaleService.getLocalizedStringForCurrentLocale(this.receiveOdysseyMaterial.getLocalizationKey()), this.receiveAmount));
        this.chatStage.show();
        this.chatStage.setOnCloseRequest(event -> {
            this.contactButton.setDisable(false);
            chatDialog.onDestroy();
        });
    }

    void onDestroy() {
        this.tradeSpec.setCallback(Optional.empty());
        EventService.removeListener(this);
        this.offerIngredient.onDestroy();
        this.receiveIngredient.onDestroy();
    }

}
