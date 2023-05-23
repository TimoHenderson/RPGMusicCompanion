package com.timohenderson.RPGMusicServer.DirectoryScanner;


import com.timohenderson.RPGMusicServer.DirectoryScanner.factories.TunesFactory;
import com.timohenderson.RPGMusicServer.models.Tune;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
        List<Tune> tunes = TunesFactory.buildTunes(startPath);
        return tunes;
    }
}
