package com.github.jjYBdx4IL.audio.examples.midi;

import java.util.Locale;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class DeviceSelectionTest {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceSelectionTest.class);

    @Test
    public void testListMIDIDevices() {
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            LOG.info(String.format(Locale.ROOT, "%s/%s/%s/%s",
                    info.getVendor(), info.getName(), info.getVersion(), info.getDescription()));
        }
    }
}
