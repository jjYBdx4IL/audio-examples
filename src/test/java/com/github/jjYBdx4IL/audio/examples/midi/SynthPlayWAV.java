package com.github.jjYBdx4IL.audio.examples.midi;

import com.github.jjYBdx4IL.audio.examples.jlayer.JLayerTest;
import com.github.jjYBdx4IL.utils.env.Maven;
import java.io.File;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javazoom.jl.converter.Converter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class SynthPlayWAV {

    private static final Logger LOG = LoggerFactory.getLogger(SynthPlayWAV.class);
    private static final File TMP_DIR = new File(Maven.getMavenBasedir(), "target");
    private static final File WAV_FILE = new File(TMP_DIR, SynthPlayWAV.class.getName() + ".wav");
    
    private File getWAVFile() throws Exception {
        if (WAV_FILE.exists()) {
            return WAV_FILE;
        }
        Converter conv = new Converter();
        conv.convert(JLayerTest.MP3_FILE.getAbsolutePath(), WAV_FILE.getAbsolutePath());
        return WAV_FILE;
    }
    
    @Test
    public void testcrap() throws Exception {
        Synthesizer syn = MidiSystem.getSynthesizer();
        syn.open();
        syn.unloadAllInstruments(syn.getDefaultSoundbank());
        syn.loadAllInstruments(MidiSystem.getSoundbank(getWAVFile()));
        for (Instrument instr : syn.getLoadedInstruments()) {
            LOG.info(instr.toString());
        }
        Instrument instr = syn.getLoadedInstruments()[0];
        MidiChannel mc = syn.getChannels()[0];
        mc.programChange(instr.getPatch().getBank(), instr.getPatch().getProgram());
        mc.noteOn(48, 127);
        Thread.sleep(3000L);
        mc.allNotesOff();
    }
}
