package com.github.jjYBdx4IL.audio.pulseaudio;

/**
 *
 * @author jjYBdx4IL
 */
public enum SampleFormat {
    /**
     * Unsigned 8 Bit PCM.
     */
    PA_SAMPLE_U8,
    /**
     * 8 Bit a-Law
     */
    PA_SAMPLE_ALAW,
    /**
     * 8 Bit mu-Law
     */
    PA_SAMPLE_ULAW,
    /**
     * Signed 16 Bit PCM, little endian (PC)
     */
    PA_SAMPLE_S16LE,
    /**
     * Signed 16 Bit PCM, big endian.
     */
    PA_SAMPLE_S16BE,
    /**
     * 32 Bit IEEE floating point, little endian (PC), range -1.0 to 1.0
     */
    PA_SAMPLE_FLOAT32LE,
    /**
     * 32 Bit IEEE floating point, big endian, range -1.0 to 1.0
     */
    PA_SAMPLE_FLOAT32BE,
    /**
     * Signed 32 Bit PCM, little endian (PC)
     */
    PA_SAMPLE_S32LE,
    /**
     * Signed 32 Bit PCM, big endian.
     */
    PA_SAMPLE_S32BE,
    /**
     * Signed 24 Bit PCM packed, little endian (PC). Since 0.9.15
     *
     */
    PA_SAMPLE_S24LE,
    /**
     * Signed 24 Bit PCM packed, big endian.
     *
     * Since 0.9.15
     *
     */
    PA_SAMPLE_S24BE,
    /**
     * Signed 24 Bit PCM in LSB of 32 Bit words, little endian (PC).
     *
     * Since 0.9.15
     *
     */
    PA_SAMPLE_S24_32LE,
    /**
     * Signed 24 Bit PCM in LSB of 32 Bit words, big endian.
     *
     * Since 0.9.15
     *
     */
    PA_SAMPLE_S24_32BE,
    /**
     * Upper limit of valid sample types.
     */
    PA_SAMPLE_MAX,
    /**
     * An invalid value.
     *
     */
    PA_SAMPLE_INVALID
}
