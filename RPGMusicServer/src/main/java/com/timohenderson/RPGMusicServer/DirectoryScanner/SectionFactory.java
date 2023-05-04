package com.timohenderson.RPGMusicServer.DirectoryScanner;

import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.sections.AdaptiveSection;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.PartFactory.buildParts;

class SectionFactory {
    static List<Section> buildSections(Path movementPath) throws IOException {
        List<Section> sections = Files.list(movementPath)
                .map((f) -> {
                    try {
                        return buildSection(movementPath.resolve(f.getFileName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return sections;
    }

    private static Section buildSection(Path sectionPath) throws IOException {
        Path sectionDataPath = sectionPath.resolve("section_data.json");
        String sectionDataString = Files.readString(sectionDataPath);
        Gson gson = new Gson();
        SectionData sectionData = gson.fromJson(sectionDataString, SectionData.class);
        String name = sectionPath.getFileName().toString();
        if (!sectionData.preRendered()) {
            AdaptiveSection section = new AdaptiveSection(name, sectionData);
            // PartLists partLists = buildPartLists(sectionPath,sectionData);
            return section;
        } else {
            RenderedSection section = new RenderedSection(name, sectionData);
            LinearPart part = buildParts(sectionPath, section);
            section.setPart(part);
            return section;
        }
    }
}
