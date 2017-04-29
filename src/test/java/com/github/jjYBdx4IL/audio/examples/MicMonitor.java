package com.github.jjYBdx4IL.audio.examples;

import com.github.jjYBdx4IL.utils.env.Surefire;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class MicMonitor extends AudioInputOutputBase {

    private static final Logger LOG = LoggerFactory.getLogger(MicMonitor.class);
    
    @Test
    public void test() throws LineUnavailableException {
        assumeTrue(Surefire.isSingleTestExecution());
        
        TargetDataLine inputLine = getInputLine();
        assertNotNull(inputLine);
        
        playback(inputLine);
    }
}
