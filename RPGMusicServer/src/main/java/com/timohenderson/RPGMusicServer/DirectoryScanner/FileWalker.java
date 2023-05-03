package com.timohenderson.RPGMusicServer.DirectoryScanner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileWalker {
    @Autowired
    private ApplicationContext ctx;

    public String getResourcePath() {

        Resource resource = ctx.getResource("classpath:/static/tunes/");
        try {

            return resource.getURL().getPath();
        } catch (IOException e) {
            throw new RuntimeException("Error getting resource path", e);
        }
    }

    public void walkFiles() throws IOException {
        Path startPath = Path.of(getResourcePath());
//        BuildObjects buildObjects = new BuildObjects();
//        EnumSet<FileVisitOption> opts = EnumSet.of();
//        Files.walkFileTree(startPath, buildObjects);
        Files.walk(startPath, 6).map((f) -> f.toFile().isFile()).forEach(System.out::println);
    }
}
