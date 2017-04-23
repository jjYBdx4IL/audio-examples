package com.github.jjYBdx4IL.audio.midi;

import java.util.Locale;
import javax.sound.midi.MidiMessage;

/**
 *
 * @author jjYBdx4IL
 */
public class MidiMessageParser {

    public static final String[] KEY_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static boolean isNoteOn(MidiMessage e) {
        byte[] data = e.getMessage();
        return data.length == 3 && (data[0] & 0xF0) == 0x90;
    }

    public static boolean isNoteOff(MidiMessage e) {
        byte[] data = e.getMessage();
        return data.length == 3 && (data[0] & 0xF0) == 0x80;
    }

    public static Key getKey(MidiMessage e) {
        byte note = e.getMessage()[1];
        return Key.values()[note % KEY_NAMES.length];
    }
    
    public static int getOctave(MidiMessage e) {
        byte note = e.getMessage()[1];
        return (note / KEY_NAMES.length) - 1;
    }
    
    public static String getNote(MidiMessage e) {
        return getKey(e).toString() + getOctave(e);
    }

    public static int getVelocity(MidiMessage e) {
        if (!isNoteOn(e) && !isNoteOff(e)) {
            return Integer.MIN_VALUE;
        }
        return e.getMessage()[2];
    }

    public static String toString(MidiMessage e) {
        if (isNoteOn(e)) {
            return String.format(Locale.ROOT, "note on %s velocity %d", getNote(e), getVelocity(e));
        } else if (isNoteOff(e)) {
            return String.format(Locale.ROOT, "note off %s velocity %d", getNote(e), getVelocity(e));
        } else {
            byte[] data = e.getMessage();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(String.format(Locale.ROOT, "0x%02x", data[i]));
            }
            return sb.toString();
        }
    }
}
