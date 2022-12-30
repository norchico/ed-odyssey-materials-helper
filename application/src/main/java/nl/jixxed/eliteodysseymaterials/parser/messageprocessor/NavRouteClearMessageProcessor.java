package nl.jixxed.eliteodysseymaterials.parser.messageprocessor;

import nl.jixxed.eliteodysseymaterials.schemas.journal.NavRouteClear.NavRouteClear;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.NavRouteClearEvent;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NavRouteClearMessageProcessor implements MessageProcessor<NavRouteClear> {
    @Override
    public void process(final NavRouteClear event) {
        if(event.getRoute().map(List::isEmpty).orElse(true)) {
            EventService.publish(new NavRouteClearEvent(event.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))));
        }
    }

    @Override
    public Class<NavRouteClear> getMessageClass() {
        return NavRouteClear.class;
    }
}
