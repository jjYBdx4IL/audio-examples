package com.github.jjYBdx4IL.audio.examples.jlayer;


import com.github.jjYBdx4IL.utils.env.CI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static org.junit.Assert.assertEquals;
import org.junit.Assume;
import org.junit.Test;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.Player;

/**
 *
 * @author jjYBdx4IL
 */
public class JLayerTest {

    public static File MP3_FILE = new File(JLayerTest.class.getResource("applause2.mp3").toExternalForm().substring(5));

    @Test
    public void testDirectPlayback() throws IOException, BitstreamException, DecoderException, FileNotFoundException, LineUnavailableException {
        Assume.assumeFalse(CI.isCI());

        long totalSize = Files.size(Paths.get(MP3_FILE.getAbsolutePath()));
        assertEquals(61440, totalSize);

        assertEquals(160, playFromBytePos(0));
        assertEquals(80, playFromBytePos(totalSize / 2));
        assertEquals(79, playFromBytePos(totalSize / 2 + 10));
        assertEquals(79, playFromBytePos(totalSize / 2 + 20));
    }

    @Test
    public void testPlayer() throws FileNotFoundException, IOException, JavaLayerException {
        Assume.assumeFalse(CI.isCI());
        
        try (InputStream is = new FileInputStream(MP3_FILE)) {
            Player player = new Player(is);
            player.play();
        }
    }

    private int playFromBytePos(long pos) throws DecoderException, FileNotFoundException, IOException, BitstreamException, LineUnavailableException {

        int nFrames = 0;
        SourceDataLine line = null;

        try (InputStream is = new FileInputStream(MP3_FILE)) {

            is.skip(pos);

            Decoder decoder = new Decoder();
            Bitstream stream = new Bitstream(is);

            Header header = stream.readFrame();
            if (header == null) {
                return nFrames;
            }
            int channels = (header.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;
            int freq = header.frequency();
            stream.unreadFrame();
            AudioFormat format = new AudioFormat(
                    freq,
                    16,
                    2,
                    true,
                    false);
            SampleBuffer output = new SampleBuffer(freq, channels);
            decoder.setOutputBuffer(output);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // this example is only valid for mono input
            assertEquals(1, channels);

            for (;;) {
                header = stream.readFrame();
                if (header == null) {
                    break;
                }

                nFrames++;

                decoder.decodeFrame(header, stream);

                byte[] buf = new byte[output.getBufferLength() * 4];
                short[] _buf = output.getBuffer();
                for (int i = 0; i < output.getBufferLength(); i++) {
                    short wordValue = _buf[i];
                    // left
                    buf[i * 4 + 0] = (byte) wordValue;
                    buf[i * 4 + 1] = (byte) (wordValue >> 8);
                    // right
                    buf[i * 4 + 2] = (byte) wordValue;
                    buf[i * 4 + 3] = (byte) (wordValue >> 8);
                }
                assertEquals(buf.length, line.write(buf, 0, buf.length));

                stream.closeFrame();
            }

            line.drain();
        } finally {
            if (line != null) {
                line.close();
            }
        }

        return nFrames;
    }
}
