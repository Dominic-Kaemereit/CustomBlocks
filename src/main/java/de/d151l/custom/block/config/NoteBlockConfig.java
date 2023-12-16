package de.d151l.custom.block.config;

import org.bukkit.Instrument;

public record NoteBlockConfig(
        Instrument instrument,
        int note,
        boolean powered,
        String placementSound,
        String breakSound
) {
}
