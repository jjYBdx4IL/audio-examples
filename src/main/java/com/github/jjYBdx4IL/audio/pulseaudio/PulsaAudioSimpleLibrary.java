package com.github.jjYBdx4IL.audio.pulseaudio;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.ArrayList;
import java.util.List;

/**
 * https://freedesktop.org/software/pulseaudio/doxygen/
 *
 * @author jjYBdx4IL
 */
public interface PulsaAudioSimpleLibrary extends Library {

    public static class pa_sample_spec extends Structure {

        public static class ByReference extends pa_sample_spec implements Structure.ByReference {
        }

        public int format;
        public int rate;
        public byte channels;

        @Override
        protected List getFieldOrder() {
            List<String> list = new ArrayList<>();
            list.add("format");
            list.add("rate");
            list.add("channels");
            return list;
        }

    }

    public Pointer pa_simple_new(String serverName, String clientName, int dir,
            String sinkName, String streamName, pa_sample_spec.ByReference sampleSpec,  String map, String attr, String error);

    /**
     *
     * @param paSimple
     * @param buf
     * @param len
     * @param error int ref, set to null for now
     * @return -1 on error
     */
    public int pa_simple_read(Pointer paSimple, byte[] buf, int len, String error);
    /**
     *
     * @param paSimple
     * @param buf
     * @param len
     * @param error int ref, set to null for now
     * @return -1 on error
     */
    public int pa_simple_write(Pointer paSimple, byte[] buf, int len, String error);

    public static final PulsaAudioSimpleLibrary INSTANCE
            = (PulsaAudioSimpleLibrary) Native.loadLibrary("pulse-simple", PulsaAudioSimpleLibrary.class);
}
