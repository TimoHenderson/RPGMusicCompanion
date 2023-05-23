package com.timohenderson.RPGMusicServer.DirectoryScanner.factories;

import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.sections.AdaptiveSection;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.factories.PartFactory.buildPartLists;
import static com.timohenderson.RPGMusicServer.DirectoryScanner.factories.PartFactory.buildRenderedParts;

class SectionFactory {
    static List<Section> buildSections(Path movementPath) throws IOException {
        List<Section> sections = Files.list(movementPath)
                .map((f) -> {
                    try {
                        return buildSection(movementPath.resolve(f.getFileName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).sorted(
                        Comparator.comparingInt(s -> s.getSectionData().order()))
                .collect(Collectors.toList());
        System.out.println(sections);
        return sections;
    }

    private static Section buildSection(Path sectionPath) throws IOException {
        SectionData sectionData = buildSectionData(sectionPath);
        String name = sectionPath.getFileName().toString();
        if (!sectionData.preRendered()) {
            HashMap<MusicalType, List<AdaptivePart>> partLists = buildPartLists(sectionPath, sectionData);
            AdaptiveSection section = new AdaptiveSection(name, sectionData, partLists);
            return section;
        } else {
            LinearPart part = buildRenderedParts(sectionPath, sectionData);
            RenderedSection section = new RenderedSection(name, sectionData, part);
            return section;
        }
    }

    private static SectionData buildSectionData(Path sectionPath) throws IOException {
        Path sectionDataPath = sectionPath.resolve("section_data.json");
        String sectionDataString = Files.readString(sectionDataPath);
        Gson gson = new Gson();
        return gson.fromJson(sectionDataString, SectionData.class);
    }
}
