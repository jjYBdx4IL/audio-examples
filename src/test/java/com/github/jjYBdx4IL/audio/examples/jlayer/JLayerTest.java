package com.github.jjYBdx4IL.audio.examples.jlayer;

import com.github.jjYBdx4IL.audio.examples.AudioInputOutputBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

/**
 *
 * @author jjYBdx4IL
 */
public class JLayerTest extends AudioInputOutputBase {

    public static File MP3_FILE = new File(JLayerTest.class.getResource("applause2.mp3").toExternalForm().substring(5));

    @Test
    public void testPlayback() throws IOException, BitstreamException, DecoderException, FileNotFoundException, LineUnavailableException {
        long totalSize = Files.size(Paths.get(MP3_FILE.getAbsolutePath()));
        assertEquals(61440, totalSize);

        assertEquals(160, playFromBytePos(0));
        assertEquals(80, playFromBytePos(totalSize/2));
    }

    private int playFromBytePos(long pos) throws DecoderException, FileNotFoundException, IOException, BitstreamException, LineUnavailableException {
        SourceDataLine line = getOutputLine();

        int nFrames = 0;

        try (InputStream is = new FileInputStream(MP3_FILE)) {

            is.skip(pos);

            SampleBuffer output = null;
            Decoder decoder = new Decoder(null);
            Bitstream stream = new Bitstream(is);

            for (;;) {
                Header header = stream.readFrame();
                if (header == null) {
                    break;
                }

                nFrames++;

                if (output == null) {
                    int channels = (header.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;
                    assertEquals(1, channels);
                    int freq = header.frequency();
                    output = new SampleBuffer(freq, channels);
                    decoder.setOutputBuffer(output);
                }

                decoder.decodeFrame(header, stream);

                byte[] buf = new byte[output.getBufferLength()*4];
                short[] _buf = output.getBuffer();
                for (int i=0; i<output.getBufferLength(); i++) {
                    putLeft(buf, i, _buf[i]/32767d);
                    putRight(buf, i, _buf[i]/32767d);
                }
                line.write(buf, 0, buf.length);

                stream.closeFrame();
            }
        }

        line.drain();

        return nFrames;
    }
}
