package nl.jixxed.eliteodysseymaterials.parser.messageprocessor;

import lombok.extern.slf4j.Slf4j;
import nl.jixxed.eliteodysseymaterials.enums.Commodity;
import nl.jixxed.eliteodysseymaterials.enums.HorizonsMaterial;
import nl.jixxed.eliteodysseymaterials.enums.NotificationType;
import nl.jixxed.eliteodysseymaterials.enums.StoragePool;
import nl.jixxed.eliteodysseymaterials.schemas.journal.Synthesis.Synthesis;
import nl.jixxed.eliteodysseymaterials.service.NotificationService;
import nl.jixxed.eliteodysseymaterials.service.StorageService;
import nl.jixxed.eliteodysseymaterials.service.event.EventService;
import nl.jixxed.eliteodysseymaterials.service.event.StorageEvent;

@Slf4j
public class SynthesisMessageProcessor implements MessageProcessor<Synthesis> {
    @Override
    public void process(final Synthesis event) {
        event.getMaterials().forEach(material -> {
            try {
                final HorizonsMaterial horizonsMaterial = HorizonsMaterial.subtypeForName(material.getName());
                if (horizonsMaterial instanceof Commodity commodity && !horizonsMaterial.isUnknown()) {
                    StorageService.removeCommodity(commodity, StoragePool.SHIP, material.getCount().intValue());
                }
                if (!horizonsMaterial.isUnknown()) {
                    StorageService.removeMaterial(horizonsMaterial, material.getCount().intValue());
                }

            } catch (final IllegalArgumentException e) {
                log.error(e.getMessage());
                NotificationService.showWarning(NotificationType.ERROR, "Unknown Material Detected", material.getName() + "\nPlease report!");
            }
        });
        EventService.publish(new StorageEvent(StoragePool.SHIP));
    }

    @Override
    public Class<Synthesis> getMessageClass() {
        return Synthesis.class;
    }
}