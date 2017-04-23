package com.github.jjYBdx4IL.audio.midi;

/**
 *
 * @author jjYBdx4IL
 */
public enum Key {
    C("C"),
    C_SHARP("C#"),
    D("D"),
    D_SHARP("D#"),
    E("E"),
    F("F"),
    F_SHARP("F#"),
    G("G"),
    G_SHARP("G#"),
    A("A"),
    A_SHARP("A#"),
    B("B");
    
    private final String name;
    
    Key(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
