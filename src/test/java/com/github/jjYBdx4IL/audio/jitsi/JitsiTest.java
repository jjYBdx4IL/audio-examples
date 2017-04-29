package com.github.jjYBdx4IL.audio.jitsi;

import static org.jitsi.impl.neomedia.jmfext.media.protocol.wasapi.WASAPI.IAudioClient_GetBufferSize;
import static org.jitsi.impl.neomedia.jmfext.media.protocol.wasapi.WASAPI.IAudioClient_GetCurrentPadding;
import static org.jitsi.impl.neomedia.jmfext.media.protocol.wasapi.WASAPI.IAudioClient_GetDefaultDevicePeriod;
import static org.jitsi.impl.neomedia.jmfext.media.protocol.wasapi.WASAPI.IAudioClient_GetService;
import static org.jitsi.impl.neomedia.jmfext.media.protocol.wasapi.WASAPI.IID_IAudioRenderClient;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.media.format.AudioFormat;

import org.jitsi.impl.neomedia.device.AudioSystem;
import org.jitsi.impl.neomedia.device.AudioSystem.DataFlow;
import org.jitsi.impl.neomedia.device.CaptureDeviceInfo2;
import org.jitsi.impl.neomedia.device.CoreAudioDevice;
import org.jitsi.impl.neomedia.device.WASAPISystem;
import org.jitsi.impl.neomedia.jmfext.media.protocol.wasapi.HResultException;
import org.jitsi.service.libjitsi.LibJitsi;
import org.junit.Assume;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jjYBdx4IL.utils.env.Surefire;

/**
 *
 * @author jjYBdx4IL
 */
public class JitsiTest {

    private static final Logger LOG = LoggerFactory.getLogger(JitsiTest.class);

    @Test
    public void test() throws HResultException {
        Assume.assumeTrue(Surefire.isSingleTestExecution());
        
        LibJitsi.start();
        assertTrue(CoreAudioDevice.isLoaded);

        WASAPISystem.initializeDeviceSystems();
        WASAPISystem wasapi = (WASAPISystem) AudioSystem.getAudioSystem(AudioSystem.LOCATOR_PROTOCOL_WASAPI);
        assertNotNull(wasapi);

        int features = wasapi.getFeatures();
        LOG.info("AGC:               " + ((features & AudioSystem.FEATURE_AGC) != 0 ? "yes" : "no"));
        LOG.info("DENOISE:           " + ((features & AudioSystem.FEATURE_DENOISE) != 0 ? "yes" : "no"));
        LOG.info("ECHO CANCELLATION: " + ((features & AudioSystem.FEATURE_ECHO_CANCELLATION) != 0 ? "yes" : "no"));

        List<CaptureDeviceInfo2> devices = wasapi.getDevices(DataFlow.PLAYBACK);
        LOG.info("number of playback devices: " + devices.size());
        CaptureDeviceInfo2 selectedDeviceInfo = null;
        for (CaptureDeviceInfo2 deviceInfo : devices) {
            LOG.info(deviceInfo.toString());
            if (deviceInfo.getName().contains("Realtek High Definition Audio")
                    && !deviceInfo.getName().contains("Realtek Digital Output")) {
                selectedDeviceInfo = deviceInfo;
            }
        }
        assertNotNull(selectedDeviceInfo);

//        WASAPIRenderer renderer = (WASAPIRenderer) wasapi.createRenderer();
//        assertNotNull(renderer);
//        
//        renderer.setLocator(selectedDeviceInfo.getLocator());
//        
//        Format[] formats = renderer.getSupportedInputFormats();
//        Format selectedFormat = null;
//        for (Format format : formats) {
//            LOG.info("supported format: " + format);
//            if (!(format instanceof AudioFormat)) {
//                continue;
//            }
//            AudioFormat af = (AudioFormat) format;
//            if (af.getChannels() == 1 && af.getSampleRate() == 48000d && af.getSampleSizeInBits() == 16) {
//                selectedFormat = format;
//                break;
//            }
//        }
//        assertNotNull(selectedFormat);
//        LOG.info(selectedFormat.toString());
        
//        renderer.setInputFormat(selectedFormat);
//        renderer.start();
        
        AudioFormat format = new AudioFormat(AudioFormat.LINEAR, 48000d, 16, 1, AudioFormat.LITTLE_ENDIAN,
                AudioFormat.SIGNED);
        
        // buffer duration: one unit is 3 ms
        long iAudioClient = wasapi.initializeIAudioClient(selectedDeviceInfo.getLocator(),
                DataFlow.PLAYBACK, 0, 0, 2, new AudioFormat[]{format});
        assertFalse(iAudioClient == 0);
        
        long iAudioRenderClient = IAudioClient_GetService(iAudioClient, IID_IAudioRenderClient);
        assertFalse(iAudioRenderClient == 0);
        
        int numBufferFrames = IAudioClient_GetBufferSize(iAudioClient);
        LOG.info("numBufferFrames: "+numBufferFrames);
        
        int numPaddingFrames = IAudioClient_GetCurrentPadding(iAudioClient);
        LOG.info("numPaddingFrames: " + numPaddingFrames);
        
        long devicePeriod = IAudioClient_GetDefaultDevicePeriod(iAudioClient) / 10000L;

    }
}
