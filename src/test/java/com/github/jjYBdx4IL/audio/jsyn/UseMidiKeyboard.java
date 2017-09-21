package com.github.jjYBdx4IL.audio.jsyn;

import com.github.jjYBdx4IL.parser.midi.MidiMessageParser;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.javasound.MidiDeviceTools;
import com.jsyn.instruments.DualOscillatorSynthVoice;
import com.jsyn.instruments.SubtractiveSynthVoice;
import com.jsyn.midi.MidiSynthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.util.MultiChannelSynthesizer;
import com.jsyn.util.VoiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class UseMidiKeyboard {
    
    private static final Logger LOG = LoggerFactory.getLogger(UseMidiKeyboard.class);
    
    private static final int NUM_CHANNELS = 16;
    private static final int VOICES_PER_CHANNEL = 12;

    private Synthesizer synth;
    private LineOut lineOut;
    private MidiSynthesizer midiSynthesizer;
    private VoiceDescription voiceDescription;
    private MultiChannelSynthesizer multiSynth;

    public static void main(String[] args) {
        UseMidiKeyboard app = new UseMidiKeyboard();
        try {
            app.test();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Write a Receiver to get the messages from a Transmitter.
    class CustomReceiver implements Receiver {
        @Override
        public void close() {
            System.out.print("Closed.");
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            byte[] bytes = message.getMessage();
            LOG.info(MidiMessageParser.toString(message));
            midiSynthesizer.onReceive(bytes, 0, bytes.length);
        }
    }

    public int test() throws MidiUnavailableException, IOException, InterruptedException {
        setupSynth();

        int result = 2;
        MidiDevice keyboard = MidiDeviceTools.findKeyboard();
        Receiver receiver = new CustomReceiver();
        // Just use default synthesizer.
        if (keyboard != null) {
            // If you forget to open them you will hear no sound.
            keyboard.open();
            // Put the receiver in the transmitter.
            // This gives fairly low latency playing.
            keyboard.getTransmitter().setReceiver(receiver);
            System.out.println("Play MIDI keyboard: " + keyboard.getDeviceInfo().getDescription());
            result = 0;
        } else {
            System.out.println("Could not find a keyboard.");
        }
        return result;
    }


    private void setupSynth() {
        synth = JSyn.createSynthesizer();

        voiceDescription = DualOscillatorSynthVoice.getVoiceDescription();
        voiceDescription = SubtractiveSynthVoice.getVoiceDescription();

        multiSynth = new MultiChannelSynthesizer();
        final int startChannel = 0;
        multiSynth.setup(synth, startChannel, NUM_CHANNELS, VOICES_PER_CHANNEL, voiceDescription);
        midiSynthesizer = new MidiSynthesizer(multiSynth);

        // Create a LineOut for the entire synthesizer.
        synth.add(lineOut = new LineOut());
        multiSynth.getOutput().connect(0,lineOut.input, 0);
        multiSynth.getOutput().connect(1,lineOut.input, 1);

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();
        lineOut.start();

    }

}
