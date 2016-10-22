package com.github.jjYBdx4IL.audio.examples;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class SinePlaybackTest extends AudioInputOutputBase {

    private static final Logger LOG = LoggerFactory.getLogger(SinePlaybackTest.class);

    public static final float HZ = 440f;
    public static final int BUFSIZE_FRAMES = Math.round(SAMPLE_RATE / HZ);

    public static byte[] getSineBuf() {
        byte[] buf = new byte[BUFSIZE_FRAMES * FRAME_SIZE];
        for (int i = 0; i < BUFSIZE_FRAMES; i += FRAME_SIZE) {
            double fraction = i / (double) BUFSIZE_FRAMES;
            double value = Math.sin(2f * Math.PI * fraction);
            putLeft(buf, i, value);
            putRight(buf, i, value);
            LOG.trace("frame " + i + ": " + buf[i * FRAME_SIZE + 0] + " " + buf[i * FRAME_SIZE + 1]);
        }
        return buf;
    }

    @Test
    public void testSinePlayback() throws LineUnavailableException {
        SourceDataLine line = getOutputLine();
        byte[] buf = getSineBuf();
        while (true) {
            int numBytesRemaining = BUFSIZE_FRAMES * FRAME_SIZE;
            do {
                numBytesRemaining -= line.write(buf, BUFSIZE_FRAMES * FRAME_SIZE - numBytesRemaining, numBytesRemaining);
            } while (numBytesRemaining > 0);
        }
    }
}
