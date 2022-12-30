package nl.jixxed.eliteodysseymaterials.service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event indicating an empty cargo message was received in the journal
 */
@AllArgsConstructor
@Getter
public class OutfittingEvent implements Event {
    private final String timestamp;
}
