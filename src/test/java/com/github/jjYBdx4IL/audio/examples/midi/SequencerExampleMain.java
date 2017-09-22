package com.github.jjYBdx4IL.audio.examples.midi;

import com.github.jjYBdx4IL.audio.midi.DevSelUtils;
import com.github.jjYBdx4IL.audio.midi.MidiLoggerReceiver;
import com.github.jjYBdx4IL.parser.midi.MidiMessageParser;
import com.github.jjYBdx4IL.parser.midi.events.ProgramChangeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

public class SequencerExampleMain implements MetaEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(SequencerExampleMain.class);

    public static void main(String[] args) throws Exception {
        new SequencerExampleMain().run();
    }

    public void run() throws Exception {

        Sequence sequence = MidiSystem.getSequence(new File(getClass().getResource("swallows.mid").toURI()));
        dumpInfo(sequence);
        sequence = MidiMessageParser.remapChannels(sequence, 0);
        
        Sequencer sequencer = MidiSystem.getSequencer(false);
        MidiDevice outdev = DevSelUtils.getHwOutDevice();
        outdev.open();
        Receiver receiver = outdev.getReceiver();
        sequencer.getTransmitter().setReceiver(receiver);
        
        
        receiver.send(ProgramChangeMsg.create(0, 4), outdev.getMicrosecondPosition());
        

        sequencer.getTransmitter().setReceiver(new MidiLoggerReceiver());

        sequencer.open();
        sequencer.addMetaEventListener(this);

        sequencer.setSequence(sequence);
        sequencer.start();
//        while (sequencer.isRunning()) {
            Thread.sleep(5000L);
//        }
        sequencer.stop();
        sequencer.close();
        outdev.close();
    }

    @Override
    public void meta(MetaMessage meta) {
        LOG.info(MidiMessageParser.toString(meta));
    }

    public static void dumpInfo(Sequence sequence) {
        for (Patch p : sequence.getPatchList()) {
            LOG.info("Patch: " + p);
        }
        for (Track t : sequence.getTracks()) {
            LOG.info("Track: " + t.size());
        }
        LOG.info(String.format(Locale.ROOT, "seqence length (s): %.3f", sequence.getMicrosecondLength() / 1e6));
    }

}
