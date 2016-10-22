package com.github.jjYBdx4IL.audio.examples;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class DesktopSoundLoopbackTest extends AudioInputOutputBase {

    private static final Logger LOG = LoggerFactory.getLogger(DesktopSoundLoopbackTest.class);

    /**
     * not working, cannot access desktop audio mixer monitor line on Ubuntu 16.10, maybe because we need
     * pulseaudio access for that.
     * 
     * @throws LineUnavailableException 
     */
    @Ignore
    @Test
    public void test() throws LineUnavailableException {
        TargetDataLine inputLine = getDesktopInputLine();
        SourceDataLine outputLine = getOutputLine();
        byte[] buf = new byte[1024];
        int numBytesRead = -1;
        do {
            numBytesRead = inputLine.read(buf, 0, buf.length);

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

//            int written = 0;
//            while (written < numBytesRead) {
//                written += outputLine.write(buf, written, numBytesRead - written);
//            }
            LOG.info("max amplitude: " + maxAmplitude);
        } while (numBytesRead != 1024);
    }

}
