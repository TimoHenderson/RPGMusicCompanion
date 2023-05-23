package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;

public class SectionLoadedEvent extends Event {
    public SectionLoadedEvent(Object source, Section payload) {
        super(source, payload);
    }

    @Override
    public Section getPayload() {
        return (Section) super.getPayload();
    }
}
