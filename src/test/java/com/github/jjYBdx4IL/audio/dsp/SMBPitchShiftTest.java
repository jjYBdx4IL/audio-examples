package com.github.jjYBdx4IL.audio.dsp;

import org.junit.Test;

import java.awt.GraphicsEnvironment;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;

import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author jjYBdx4IL
 */
public class SMBPitchShiftTest {

    @Before
    public void before() {
        assumeFalse(GraphicsEnvironment.isHeadless());
    }
    
    /**
     * Test of smbPitchShift method, of class SMBPitchShift.
     */
    @Test
    public void testSmbPitchShift() throws Exception {
        final float[] indata = new float[16384];
        final float[] outdata = new float[indata.length];
        final float absMax = 1f;
        final float hz = 440f;
        final int sampleRate = 44100;
        for (int i = 0; i < indata.length; i++) {
            indata[i] = (float) (Math.sin((i * hz / sampleRate) * Math.PI) * absMax);
        }
        
        SMBPitchShift smbPitchShift = new SMBPitchShift();
        smbPitchShift.smbPitchShift(1.1f, indata.length, 4096, 10, sampleRate, indata, outdata);
        
        final float[] vizdata = new float[indata.length * 2];
        for (int i = 0; i < indata.length; i++) {
            vizdata[2 * i] = indata[i];
            vizdata[2 * i + 1] = outdata[i];
        }
        new WaveJFrame(vizdata, 2).run();
        Thread.sleep(10000);
    }

    /**
     * Test of smbFft method, of class SMBPitchShift.
     */
    @Ignore
    @Test
    public void testSmbFft() {
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
