package com.timohenderson.RPGMusicServer.DirectoryScanner;


import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.musemes.MusemeData;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.sections.AdaptiveSection;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileWalker {
    @Autowired
    private ApplicationContext ctx;

    public Path getResourcePath(String path) {

        Resource resource = ctx.getResource("classpath:/static/tunes/" + path);
        try {
            String pathString = resource.getURL().getPath();
            return Path.of(pathString);

        } catch (IOException e) {
            throw new RuntimeException("Error getting resource path", e);
        }
    }

    public List<Tune> walkFiles() throws IOException {
        Path startPath = getResourcePath("");

        List<Tune> tunes = buildTunes(startPath);
        return tunes;
    }

    private List<Tune> buildTunes(Path startPath) throws IOException {
        List<Tune> tunes = Files.list(startPath)
                .map((f) -> new Tune(f.getFileName().toString()))
                .collect(Collectors.toList());

        for (Tune tune : tunes) {
            Path tunePath = startPath.resolve(tune.getName());
            tune.setMovements(buildMovements(tunePath));
        }
        return tunes;
    }

    private List<Movement> buildMovements(Path tunePath) throws IOException {

        List<Movement> movements = Files.list(tunePath)
                .map((f) -> new Movement(f.getFileName().toString()))
                .collect(Collectors.toList());

        for (Movement movement : movements) {
            Path movementPath = tunePath.resolve(movement.getName());
            movement.setSections(buildSections(movementPath));
        }

        return movements;
    }

    private List<Section> buildSections(Path movementPath) throws IOException {
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

    private Section buildSection(Path sectionPath) throws IOException {
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

    private LinearPart buildParts(Path sectionPath, RenderedSection section) throws IOException {
        Path partPath = sectionPath.resolve("Master/master");
        LinearPart part = new LinearPart();
        part.setName("master");
        Path musemePath = Files.list(partPath).findFirst().get();
        part.setMuseme(buildMuseme(musemePath, section.getSectionData()));
        return part;
    }

    private Museme buildMuseme(Path musemePath, SectionData sectionData) throws IOException {
        String fileName = musemePath.getFileName().toString();
        MusemeData musemeData = parseMusemeData(fileName, sectionData);
        return new Museme(musemePath, musemeData);
    }

    private MusemeData parseMusemeData(String fileName, SectionData sectionData) {
        ArrayList<Integer> startBars = new ArrayList<>();
        int length = 0;
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        String[] parts = fileName.split("_");
        String name = parts[0];
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].startsWith("b")) {
                parts[i] = parts[i].substring(1);
                String[] bars = parts[i].split("&");
                for (int j = 0; j < bars.length; j++) {
                    startBars.add(Integer.parseInt(bars[j]));
                }
                continue;
            }
            if (parts[i].startsWith("l")) {
                parts[i] = parts[i].substring(1);
                length = Integer.parseInt(parts[i]);
                continue;
            }
        }
        if (startBars.isEmpty()) {
            startBars.add(1);
        }
        if (length == 0) {
            length = sectionData.numBars();
        }
        MusemeData musemeData = new MusemeData(name, length, startBars);
        return musemeData;
    }

    // todo: buildLinearPart needs generalizing
    private LinearPart buildLinearPart(Path sectionPath, SectionData sectionData) throws IOException {
        Path partPath = sectionPath.resolve("Master/master");
        LinearPart part = new LinearPart();
        part.setName("Master");
        Path musemePath = partPath.resolve("Master.wav");
        Museme museme = new Museme(musemePath, sectionData.numBars());
        museme.addStartBar(1);
        part.setMuseme(museme);
        return part;
    }

//    private Museme buildMuseme(Path partPath, RenderedSection section) throws IOException {
//
//    }

    private void buildParts(Path sectionPath, AdaptiveSection section) throws IOException {
        // get list of all folders in sectionPath
        // for each folder, create a list for each folder inside
    }
}
