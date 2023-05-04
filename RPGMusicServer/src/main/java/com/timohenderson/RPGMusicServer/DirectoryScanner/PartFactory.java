package com.timohenderson.RPGMusicServer.DirectoryScanner;

import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.parts.Part;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.MusemeFactory.buildMuseme;

class PartFactory {

    static LinearPart buildRenderedParts(Path sectionPath, SectionData sectionData) throws IOException {
        Path partPath = sectionPath.resolve("Master/master");
        Path musemePath = Files.list(partPath).findFirst().get();
        Museme museme = buildMuseme(musemePath, sectionData);
        PartData partData = parsePartData(partPath.getFileName().toString());
        LinearPart part = new LinearPart(partData, museme);
        return part;
    }

    static HashMap<MusicalType, List<Part>> buildPartLists(Path sectionPath, SectionData sectionData) throws IOException {
        ArrayList<Path> partGroups = Files.list(sectionPath)
                .filter(Files::isDirectory)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        HashMap<MusicalType, List<Part>> partLists = new HashMap<>();
        System.out.println(partGroups);
        for (Path path : partGroups) {

            String directoryName = path.getFileName().toString();
            MusicalType musicalType = MusicalType.valueOf(directoryName);
            ArrayList<Part> parts = buildParts(path, sectionData);
            partLists.put(musicalType, parts);
        }
        return partLists;
    }

    private static ArrayList<Part> buildParts(Path partGroupPath, SectionData sectionData) throws IOException {
        ArrayList<Path> partPaths = Files.list(partGroupPath)
                .filter(Files::isDirectory)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(partPaths);
        ArrayList<Part> parts = new ArrayList<>();
        for (Path path : partPaths) {
            parts.add(buildPart(path, sectionData));
        }
        return parts;
    }

    private static Part buildPart(Path partPath, SectionData sectionData) throws IOException {
        PartData partData = parsePartData(partPath.getFileName().toString());
        ArrayList<Path> musemePaths = Files.list(partPath)
                .filter(Files::isRegularFile)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        if (musemePaths.size() == 1) {
            Museme museme = buildMuseme(musemePaths.get(0), sectionData);
            return new LinearPart(partData, museme);
        } else if (musemePaths.size() > 1) {
            ArrayList<Museme> musemeList =
                    musemePaths
                            .stream()
                            .map(p -> {
                                        try {
                                            return buildMuseme(p, sectionData);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            ).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            System.out.println(musemeList);
            return new AdaptivePart(partData, musemeList);

        }
        return null;
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
