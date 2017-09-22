package com.github.jjYBdx4IL.audio.examples.midi;

import com.github.jjYBdx4IL.audio.midi.DevSelUtils;
import com.github.jjYBdx4IL.parser.midi.events.AllNotesOffMsg;
import com.github.jjYBdx4IL.parser.midi.events.NoteOnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 *
 * @author jjYBdx4IL
 */
public class SendMidiEventsTest {

    private static final Logger LOG = LoggerFactory.getLogger(SendMidiEventsTest.class);

    MidiDevice dev = null;
    Receiver receiver = null;
    
    public static void main(String[] args) throws Exception {
        new SendMidiEventsTest().run();
    }
    
    public void run() throws Exception {
        dev = DevSelUtils.getHwOutDevice();
        receiver = dev.getReceiver();

        dev.open();
        LOG.info("" + dev.getMicrosecondPosition());
        
        send(NoteOnMsg.create(0, "C3", 120));
        send(NoteOnMsg.create(0, "E3", 120));
        send(NoteOnMsg.create(0, "G3", 120));
        send(NoteOnMsg.create(0, "C4", 120));
        send(NoteOnMsg.create(0, "E4", 120));
        send(NoteOnMsg.create(0, "G4", 120));
        send(NoteOnMsg.create(0, "C5", 120));
        send(NoteOnMsg.create(0, "E5", 120));
        send(NoteOnMsg.create(0, "G5", 120));
        Thread.sleep(1L * 1000L);
        send(AllNotesOffMsg.create(0));
        
        send(NoteOnMsg.create(0, "d3", 120));
        send(NoteOnMsg.create(0, "f3", 120));
        send(NoteOnMsg.create(0, "a3", 120));
        send(NoteOnMsg.create(0, "d4", 120));
        send(NoteOnMsg.create(0, "f4", 120));
        send(NoteOnMsg.create(0, "a4", 120));
        send(NoteOnMsg.create(0, "d5", 120));
        send(NoteOnMsg.create(0, "f5", 120));
        send(NoteOnMsg.create(0, "a5", 120));
        Thread.sleep(1L * 1000L);
        send(AllNotesOffMsg.create(0));
        
        LOG.info("" + dev.getMicrosecondPosition());
    }
    
    private void send(MidiMessage msg) {
        receiver.send(msg, dev.getMicrosecondPosition());
    }
    
}
