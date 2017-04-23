package com.github.jjYBdx4IL.audio.dsp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author jjYBdx4IL
 */
public class WaveJFrame extends JFrame implements Runnable {

    private final int channels;
    private final float[] data;
    private float absMax = 0f;
    private int drawingAreaHeight = 400;

    public WaveJFrame(float[] data, int channels) {
        super("wave viz");
        this.channels = channels;
        this.data = Arrays.copyOf(data, data.length);
        for (int i = 0; i < this.data.length; i++) {
            absMax = Math.max(Math.abs(this.data[i]), absMax);
        }
    }

    public void setDrawingAreaHeight(int pixels) {
        this.drawingAreaHeight = pixels;
    }

    @Override
    public void run() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this);
            return;
        }

        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        final BufferedImage image = new BufferedImage(data.length, drawingAreaHeight, BufferedImage.TYPE_3BYTE_BGR);
        final float scalingFactor = ((drawingAreaHeight / (2 * channels)) - 1) / absMax;
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, data.length, drawingAreaHeight);
        g.setColor(Color.GRAY);

        for (int j = 0; j < channels; j++) {
            final float midY = j * drawingAreaHeight / (float) channels + drawingAreaHeight / (2f * channels);
            for (int i = channels + j; i < data.length; i += channels) {
                g.drawLine(
                        i - channels, (int) (midY - scalingFactor * data[i - channels]),
                        i, (int) (midY - scalingFactor * data[i]));
            }
        }
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        JScrollPane scrollPane = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(scrollPane, c);

        pack();
        setVisible(true);
    }

}
