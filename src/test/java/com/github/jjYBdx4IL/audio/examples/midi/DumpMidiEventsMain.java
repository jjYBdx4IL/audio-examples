package com.github.jjYBdx4IL.audio.examples.midi;

import com.github.jjYBdx4IL.audio.midi.DevSelUtils;
import com.github.jjYBdx4IL.audio.midi.MidiLoggerReceiver;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Transmitter;

/**
 *
 * @author jjYBdx4IL
 */
public class DumpMidiEventsMain {

    public static void main(String[] args) throws Exception {
        MidiDevice dev = DevSelUtils.getHwInDevice();
        Transmitter trans = dev.getTransmitter();
        trans.setReceiver(new MidiLoggerReceiver());
        dev.open();
        Thread.sleep(3600L * 1000L);
    }
}
