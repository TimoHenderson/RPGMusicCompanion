package com.timohenderson.RPGMusicServer.DirectoryScanner.factories;

import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.factories.MusemeFactory.buildMuseme;

class PartFactory {

    static LinearPart buildRenderedParts(Path sectionPath, SectionData sectionData) throws IOException {
        Path partPath = sectionPath.resolve("Master/master");
        Path musemePath = Files.list(partPath).findFirst().get();
        Museme museme = buildMuseme(musemePath, sectionData);
        PartData partData = parsePartData(partPath.getFileName().toString(), sectionData.numBars());
        return new LinearPart(partData, museme);

    }

    static HashMap<MusicalType, List<AdaptivePart>> buildPartLists(Path sectionPath, SectionData sectionData) throws IOException {
        ArrayList<Path> partGroups = Files.list(sectionPath)
                .filter(Files::isDirectory)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        HashMap<MusicalType, List<AdaptivePart>> partLists = new HashMap<>();
        System.out.println(partGroups);
        for (Path path : partGroups) {

            String directoryName = path.getFileName().toString();
            MusicalType musicalType = MusicalType.valueOf(directoryName);
            ArrayList<AdaptivePart> parts = buildParts(path, sectionData);
            partLists.put(musicalType, parts);
        }
        return partLists;
    }

    private static ArrayList<AdaptivePart> buildParts(Path partGroupPath, SectionData sectionData) throws IOException {
        ArrayList<Path> partPaths = Files.list(partGroupPath)
                .filter(Files::isDirectory)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(partPaths);
        ArrayList<AdaptivePart> parts = new ArrayList<>();
        for (Path path : partPaths) {
            parts.add(buildPart(path, sectionData));
        }
        return parts;
    }

    private static AdaptivePart buildPart(Path partPath, SectionData sectionData) throws IOException {
        PartData partData = parsePartData(partPath.getFileName().toString(), sectionData.numBars());
        ArrayList<Path> musemePaths = Files.list(partPath)
                .filter(Files::isRegularFile)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

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

        return new AdaptivePart(partData, populateMap(musemeList));
    }

    private static HashMap<Integer, List<Museme>> populateMap(List<Museme> musemes) {
        HashMap<Integer, List<Museme>> musemesByStartBars = new HashMap<>();
        musemes.stream()
                .forEach(m -> {
                    for (Integer startBar : m.getMusemeData().startBars()) {
                        if (musemesByStartBars.containsKey(startBar)) {
                            musemesByStartBars.get(startBar).add(m);
                        } else {
                            List<Museme> newList = new ArrayList<>();
                            newList.add(m);
                            musemesByStartBars.put(startBar, newList);
                        }
                    }
                });
        return musemesByStartBars;
    }


    private static PartData parsePartData(String directoryName, int sectionLength) {
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

        return new PartData(name, intensity, darkness, sectionLength);

    }
}
