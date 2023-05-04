package com.timohenderson.RPGMusicServer.DirectoryScanner;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.musemes.MusemeData;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

class MusemeFactory {
    static Museme buildMuseme(Path musemePath, SectionData sectionData) throws IOException {
        String fileName = musemePath.getFileName().toString();
        String fullPath = musemePath.toString();
        String relativePath = fullPath.substring(fullPath.indexOf("/static/"));
        MusemeData musemeData = parseMusemeData(fileName, sectionData);
        return new Museme(relativePath, musemeData);
    }

    private static MusemeData parseMusemeData(String fileName, SectionData sectionData) {
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
}
