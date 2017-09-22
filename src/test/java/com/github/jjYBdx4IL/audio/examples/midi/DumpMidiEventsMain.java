package com.github.jjYBdx4IL.audio.examples.midi;

import com.github.jjYBdx4IL.audio.midi.MidiLoggerReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

/**
 *
 * @author jjYBdx4IL
 */
public class DumpMidiEventsMain {

    private static final Logger LOG = LoggerFactory.getLogger(DumpMidiEventsMain.class);

    public static void main(String[] args) throws Exception {

        MidiDevice dev = null;
        Transmitter trans = null;

        // connect to first MIDI device that can send us MIDI events:
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            LOG.info(String.format(Locale.ROOT, "%s/%s/%s/%s",
                info.getVendor(), info.getName(), info.getVersion(), info.getDescription()));
            try {
                dev = MidiSystem.getMidiDevice(info);
                trans = dev.getTransmitter();
                break;
            } catch (MidiUnavailableException ex) {
            }
        }

        trans.setReceiver(new MidiLoggerReceiver());

        dev.open();
        Thread.sleep(3600L * 1000L);
    }
}
