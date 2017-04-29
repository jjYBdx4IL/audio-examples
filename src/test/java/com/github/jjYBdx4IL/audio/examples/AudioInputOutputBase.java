package com.github.jjYBdx4IL.audio.examples;

import com.github.jjYBdx4IL.utils.env.Surefire;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class AudioInputOutputBase {

    private static final Logger LOG = LoggerFactory.getLogger(AudioInputOutputBase.class);
    public static final float SAMPLE_RATE = 44100f;
    public static final int CHANNELS = 2;
    public static final int SAMPLE_SIZE_BITS = 16;
    public static final boolean BIG_ENDIAN = true;
    public static final int FRAME_SIZE = SAMPLE_SIZE_BITS * CHANNELS / 8;
    public static final AudioFormat.Encoding ENCODING = AudioFormat.Encoding.PCM_SIGNED;

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue(Surefire.isSingleTestExecution());
    }

    /**
     * Write a normalized (-1..+1) amplitude sample to the left channel.
     *
     * @param buf
     * @param frame
     * @param value
     */
    public static void putLeft(byte[] buf, int frame, double value) {
        // if (Math.abs(value) > 1d) {
        // throw new IllegalArgumentException();
        // }
        long wordValue = (long) (value * 32767d);
        buf[frame * FRAME_SIZE + 0] = (byte) (wordValue >> 8);
        buf[frame * FRAME_SIZE + 1] = (byte) (wordValue & 0xFF);
    }

    /**
     * Write a normalized (-1..+1) amplitude sample to the right channel.
     *
     * @param buf
     * @param frame
     * @param value
     */
    public static void putRight(byte[] buf, int frame, double value) {
        // if (Math.abs(value) > 1d) {
        // throw new IllegalArgumentException();
        // }
        long wordValue = (long) (value * 32767d);
        buf[frame * FRAME_SIZE + 2] = (byte) (wordValue >> 8);
        buf[frame * FRAME_SIZE + 3] = (byte) (wordValue & 0xFF);
    }

    /**
     * Return the normalized (-1..+1) amplitude sample for the left channel.
     *
     * @param buf
     * @param frame
     * @return
     */
    public static double getLeft(byte[] buf, int frame) {
        return ((buf[frame * FRAME_SIZE + 0] << 8) | buf[frame * FRAME_SIZE + 1] & 0xFF) / 32767d;
    }

    /**
     * Return the normalized (-1..+1) amplitude sample for the left channel.
     *
     * @param buf
     * @param frame
     * @return
     */
    public static double getRight(byte[] buf, int frame) {
        return ((buf[frame * FRAME_SIZE + 2] << 8) | buf[frame * FRAME_SIZE + 3] & 0xFF) / 32767d;
    }

    public static AudioFormat getAudioFormat() {
        return new AudioFormat(ENCODING, SAMPLE_RATE, SAMPLE_SIZE_BITS, CHANNELS,
                SAMPLE_SIZE_BITS * CHANNELS / 8, SAMPLE_RATE, BIG_ENDIAN);
    }
    
    public static javax.media.format.AudioFormat toMediaAF(AudioFormat format) {
        return new javax.media.format.AudioFormat(
                javax.media.format.AudioFormat.LINEAR,
                format.getSampleRate(),
                format.getSampleSizeInBits(),
                format.getChannels(),
                format.isBigEndian() ? javax.media.format.AudioFormat.BIG_ENDIAN : javax.media.format.AudioFormat.LITTLE_ENDIAN,
                format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) ? javax.media.format.AudioFormat.SIGNED : javax.media.format.AudioFormat.UNSIGNED
                );
    }

    // TODO Linux
    // https://freedesktop.org/software/pulseaudio/doxygen/parec-simple_8c-example.html
    public TargetDataLine getDesktopInputLine() throws LineUnavailableException {
        Mixer.Info selectedMixerInfo = null;
        Line.Info selectedLineInfo = null;
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            LOG.info(info.getName() + ": " + info.getDescription() + ", " + info.getVendor());
            for (Info info2 : AudioSystem.getMixer(info).getSourceLineInfo()) {
                LOG.info(" \\-> " + info2.toString());
            }
            for (Info info2 : AudioSystem.getMixer(info).getTargetLineInfo()) {
                LOG.info(" \\-> " + info2.toString());
                if (info2.toString().contains("Master target port")) {
                    selectedLineInfo = info2;
                }
            }
            if (info.getName().contains("[hw:0]")) {
                selectedMixerInfo = info;
            }
        }
        Mixer mixer = AudioSystem.getMixer(selectedMixerInfo);
        AudioFormat format = getAudioFormat();
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(selectedLineInfo);
        line.open(format, line.getBufferSize());
        line.start();
        return line;
    }

    public TargetDataLine getInputLine(Pattern regex) throws LineUnavailableException {
        Line.Info selectedLineInfo = null;
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            LOG.info(info.getName() + ": " + info.getDescription() + ", " + info.getVendor());
            for (Info info2 : AudioSystem.getMixer(info).getSourceLineInfo()) {
                LOG.info(" \\-> " + info2.toString());
            }
            for (Info info2 : AudioSystem.getMixer(info).getTargetLineInfo()) {
                LOG.info(" \\-> " + info2.toString());
                String className = info2.getClass().getCanonicalName();
                LOG.info(className);
                if (className.endsWith(".DirectDLI") && regex.matcher(info.getName()).find()) {
                    selectedLineInfo = info2;
                    LOG.info("^-- selected!");
                }
            }
        }
        LOG.info("selected target line: " + selectedLineInfo);
        AudioFormat format = getAudioFormat();
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(selectedLineInfo);
        line.open(format);
        line.start();
        return line;
    }

    public TargetDataLine getMicInputLine() throws LineUnavailableException {
        AudioFormat format = getAudioFormat();
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(Port.Info.MICROPHONE);
        line.open(format, line.getBufferSize());
        line.start();
        return line;
    }

    public TargetDataLine getInputLine() throws LineUnavailableException {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        assertTrue(AudioSystem.isLineSupported(info));
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format, line.getBufferSize());
        line.start();
        return line;
    }

    public SourceDataLine getOutputLine() throws LineUnavailableException {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        assertTrue(AudioSystem.isLineSupported(info));
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format, line.getBufferSize());
        // line.open(format);
        line.start();
        return line;
    }

    public void playback(TargetDataLine inputLine) throws LineUnavailableException {
        SourceDataLine outputLine = getOutputLine();
        cat(inputLine, outputLine);
    }

    public void cat(TargetDataLine inputLine, SourceDataLine outputLine) {

        byte[] buf = new byte[inputLine.getBufferSize()];
        LOG.info("buf size (bytes): " + buf.length);
        int numBytesRead = -1;
        int avl = -1;
        do {
            avl = inputLine.available();
            if (avl < FRAME_SIZE) {
                avl = FRAME_SIZE;
            }
            if (avl > buf.length) {
                avl = buf.length;
            }
            ;
            numBytesRead = inputLine.read(buf, 0, avl);
            // LOG.info("numBytesRead = " + numBytesRead);

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

                leftAmplitude *= 3d;

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

                putLeft(buf, frame, leftAmplitude);
                putRight(buf, frame, rightAmplitude);
            }

            int written = 0;
            while (written < numBytesRead) {
                written += outputLine.write(buf, written, numBytesRead - written);
            }
            if (maxAmplitude > 0.05d) {
                LOG.info(String.format("max amplitude: %02.0f%%", maxAmplitude * 100d));
            }
        } while (numBytesRead == avl);

    }

}
