package com.github.jjYBdx4IL.audio.midi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class DevSelUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DevSelUtils.class);

    public static MidiDevice getSwOutDevice() {
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(toString(info));
            }
            if (!isSwOutDevice(info)) {
                continue;
            }
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                if (dev.getReceiver() != null) {
                    LOG.info("out device selected: " + toString(info));
                    return dev;
                }
            } catch (MidiUnavailableException ex) {
            }
        }
        return null;
    }

    public static MidiDevice getHwOutDevice() {
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(toString(info));
            }
            if (isSwOutDevice(info)) {
                continue;
            }
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                if (dev.getReceiver() != null) {
                    LOG.info("out device selected: " + toString(info));
                    return dev;
                }
            } catch (MidiUnavailableException ex) {
            }
        }
        return null;
    }
    
    public static boolean isSwOutDevice(MidiDevice.Info info) {
        return info.getName().toLowerCase().contains("gervill");
    }

    public static String toString(MidiDevice.Info info) {
        return String.format(Locale.ROOT, "%s/%s/%s/%s",
            info.getVendor(), info.getName(), info.getVersion(), info.getDescription());
    }
}
