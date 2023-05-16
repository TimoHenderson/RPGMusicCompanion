package com.timohenderson.RPGMusicServer.audio;

import org.springframework.stereotype.Component;

import javax.sound.sampled.*;

@Component
public class VBMixer {
    private Mixer mixer;
    private TargetDataLine targetDataLine;

    public VBMixer() {
        Mixer.Info[] mixersInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixersInfo.length; i++) {
            System.out.println(mixersInfo[i].getClass());
            if (mixersInfo[i].getName().contains("VB")) {
                mixer = AudioSystem.getMixer(mixersInfo[i]);
            }
        }

        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!mixer.isLineSupported(info)) {
            System.out.println("Line not supported");
        }

        try {
            targetDataLine = (TargetDataLine) mixer.getLine(info);
            System.out.println(targetDataLine.toString());
            System.out.println(targetDataLine.getFormat().toString());


        } catch (LineUnavailableException ex) {

        }
    }

    public Mixer getMixer() {
        return mixer;
    }

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }
}


