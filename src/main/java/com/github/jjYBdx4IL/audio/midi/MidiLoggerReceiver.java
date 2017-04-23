package com.github.jjYBdx4IL.audio.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * see http://www.onicos.com/staff/iz/formats/midi-event.html
 * 
 * @author jjYBdx4IL
 */
public class MidiLoggerReceiver implements Receiver {
    
    private static final Logger LOG = LoggerFactory.getLogger(MidiLoggerReceiver.class);
    
    public MidiLoggerReceiver() {
    }
    
    public void send(MidiMessage msg, long timeStamp) {
        LOG.info(MidiMessageParser.toString(msg));
    }

    @Override
    public void close() {
    }
}
