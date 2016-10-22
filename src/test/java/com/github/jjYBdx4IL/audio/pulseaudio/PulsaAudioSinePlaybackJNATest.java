package com.github.jjYBdx4IL.audio.pulseaudio;

import com.github.jjYBdx4IL.audio.examples.AudioInputOutputBase;
import com.github.jjYBdx4IL.audio.examples.SinePlaybackTest;
import com.sun.jna.Pointer;

import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class PulsaAudioSinePlaybackJNATest extends AudioInputOutputBase {

    private static final Logger LOG = LoggerFactory.getLogger(PulsaAudioSinePlaybackJNATest.class);

    @Test
    public void test() {

        final PulsaAudioSimpleLibrary.pa_sample_spec.ByReference e3ref = new PulsaAudioSimpleLibrary.pa_sample_spec.ByReference();
        e3ref.format = SampleFormat.PA_SAMPLE_S16BE.ordinal();
        e3ref.rate = 44100;
        e3ref.channels = 2;

        Pointer paSimple = PulsaAudioSimpleLibrary.INSTANCE.pa_simple_new(null, "testclient",
                StreamDirection.PA_STREAM_PLAYBACK.ordinal(), null, "playback stream", e3ref, null, null, null);
        assertNotNull(paSimple);

        byte[] buf = SinePlaybackTest.getSineBuf();
        while (true) {
            assertTrue(PulsaAudioSimpleLibrary.INSTANCE.pa_simple_write(paSimple, buf, buf.length, null) != -1);
        }
    }
}
