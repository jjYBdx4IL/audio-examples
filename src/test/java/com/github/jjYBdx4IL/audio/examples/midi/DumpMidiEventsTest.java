package com.github.jjYBdx4IL.audio.examples.midi;

import com.github.jjYBdx4IL.audio.midi.MidiLoggerReceiver;
import com.github.jjYBdx4IL.utils.env.Surefire;
import java.util.Locale;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import static org.junit.Assert.assertNotNull;
import org.junit.Assume;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class DumpMidiEventsTest {

    private static final Logger LOG = LoggerFactory.getLogger(DumpMidiEventsTest.class);
    
    @Test
    public void testDumpMidiEvents() throws MidiUnavailableException, InterruptedException {
        // interactive test that needs adjuestments according to your own midi setup, ie. the device selection
        Assume.assumeTrue(Surefire.isSingleTestExecution());
        
        MidiDevice dev = null;
        
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            LOG.info(String.format(Locale.ROOT, "%s/%s/%s/%s",
                    info.getVendor(), info.getName(), info.getVersion(), info.getDescription()));
            if ("2- UM-ONE".equals(info.getName()) && !"External MIDI Port".equals(info.getDescription())) {
                dev = MidiSystem.getMidiDevice(info);
            }
        }
        
        assertNotNull(dev);
        
        Transmitter trans = dev.getTransmitter();
        trans.setReceiver(new MidiLoggerReceiver());
        
        
        dev.open();
        Thread.sleep(3600L*1000L);
        //Unknown vendor/2- UM-ONE/10.0/External MIDI Port"
    }
}
