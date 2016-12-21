package com.github.jjYBdx4IL.audio.pulseaudio;

import com.github.jjYBdx4IL.audio.examples.AudioInputOutputBase;
import com.sun.jna.Pointer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class PulsaAudioSimpleJNATest extends AudioInputOutputBase {

    private static final Logger LOG = LoggerFactory.getLogger(PulsaAudioSimpleJNATest.class);
    public static final int BUFSIZE = 1024;

    @Test
    public void test() throws LineUnavailableException {

        final PulsaAudioSimpleLibrary.pa_sample_spec.ByReference e3ref = new PulsaAudioSimpleLibrary.pa_sample_spec.ByReference();
        e3ref.format = SampleFormat.PA_SAMPLE_S16BE.ordinal();
        e3ref.rate = 44100;
        e3ref.channels = 2;

        Pointer paSimple = PulsaAudioSimpleLibrary.INSTANCE.pa_simple_new(null, "testclient",
                StreamDirection.PA_STREAM_RECORD.ordinal(),
                // use "pactl list" to find your monitor's name:
                "alsa_output.pci-0000_00_1b.0.analog-stereo.monitor",
                "record stream", e3ref, null, null, null);
        assertNotNull(paSimple);

        SourceDataLine outputLine = getOutputLine();

        byte[] buf = new byte[BUFSIZE];
        while (true) {
            assertTrue(PulsaAudioSimpleLibrary.INSTANCE.pa_simple_read(paSimple, buf, buf.length, null) != -1);
            int numBytesRead = buf.length;

            // amplify amplitude
            double maxAmplitude = 0d;
            for (int frame = 0; frame < numBytesRead / FRAME_SIZE; frame++) {
                double leftAmplitude = getLeft(buf, frame);
                double rightAmplitude = getRight(buf, frame);

                if (Math.abs(leftAmplitude) > maxAmplitude) {
                    maxAmplitude = Math.abs(leftAmplitude);
                }
                if (Math.abs(rightAmplitude) > maxAmplitude) {
                    maxAmplitude = Math.abs(rightAmplitude);
                }

                leftAmplitude *= 2d;
                rightAmplitude *= 2d;
                if (leftAmplitude > 1d) {
                    leftAmplitude = 1d;
                }
                if (leftAmplitude < -1d) {
                    leftAmplitude = -1d;
                }
                if (rightAmplitude > 1d) {
                    rightAmplitude = 1d;
                }
                if (rightAmplitude < -1d) {
                    rightAmplitude = -1d;
                }
            }

            int written = 0;
            while (written < numBytesRead) {
                written += outputLine.write(buf, written, numBytesRead - written);
            }
            LOG.info("max amplitude: " + maxAmplitude);
        }
    }
}
