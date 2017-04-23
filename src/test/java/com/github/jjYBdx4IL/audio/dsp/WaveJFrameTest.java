package com.github.jjYBdx4IL.audio.dsp;

import org.junit.Test;

/**
 *
 * @author jjYBdx4IL
 */
public class WaveJFrameTest {

    /**
     * Test of viz method, of class WaveJFrame.
     */
    @Test
    public void testViz() throws Exception {
        final float[] data = new float[16384];
        final float absMax = 1f;
        for (int i = 0; i < data.length; i+=2) {
            data[i] = (float) (Math.sin(i / 100f) * absMax);
            data[i+1] = (float) (Math.cos(i / 100f) * absMax);
        }
        new WaveJFrame(data, 2).run();
        Thread.sleep(10000);
    }

}
