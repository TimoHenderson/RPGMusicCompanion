package com.timohenderson.RPGMusicServer.DirectoryScanner;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.MusemeFactory.buildMuseme;

class PartFactory {

    static LinearPart buildParts(Path sectionPath, RenderedSection section) throws IOException {
        Path partPath = sectionPath.resolve("Master/master");

        Path musemePath = Files.list(partPath).findFirst().get();
        Museme museme = buildMuseme(musemePath, section.getSectionData());
        PartData partData = parsePartData(partPath.getFileName().toString());
        LinearPart part = new LinearPart(partData, museme);
        return part;
    }

    private static PartData parsePartData(String directoryName) {
        int intensity = 0;
        int darkness = 0;
        String[] parts = directoryName.split("_");
        String name = parts[0];
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].startsWith("i")) {
                parts[i] = parts[i].substring(1);
                intensity = Integer.parseInt(parts[i]);
                continue;
            }
            if (parts[i].startsWith("d")) {
                parts[i] = parts[i].substring(1);
                darkness = Integer.parseInt(parts[i]);
            }
        }

        PartData partData = new PartData(name, intensity, darkness);
        return partData;
    }
}
