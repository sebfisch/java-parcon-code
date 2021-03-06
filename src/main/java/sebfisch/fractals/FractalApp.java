package sebfisch.fractals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import sebfisch.graphics.ImageParams;
import sebfisch.graphics.Pixel;
import sebfisch.graphics.Point;
import sebfisch.graphics.rendering.ImageCanvas;
import sebfisch.graphics.rendering.Renderer;
import sebfisch.graphics.rendering.StreamRenderer;
import sebfisch.graphics.rendering.TimedRenderer;

public class FractalApp {
    public static final int THREADS = //
        Runtime.getRuntime().availableProcessors();

    private final Frame frame;
    private final ImageCanvas canvas;

    private FractalImage image;
    private ImageParams params;

    public static void main(String[] args) {
        new FractalApp();
    }

    public FractalApp() {
        params = new ImageParams(new Point(0, 0), 0.007);
        // params = new ImageParams( //
        //     new Point(-1.4793453674316406,0.0021713104248046885), //
        //     7.62939453125E-9 //
        // );

        image = new MandelbrotSet();
        // image = new JuliaSet(new Point(-1, 0.1));
        // image = new JuliaSet(new Point(-1.476, 0.0));
        adjustMaxIter();

        // TODO [Task 1, Threads] try different renderers
        final Renderer renderer = new TimedRenderer<>( //
            new StreamRenderer() // 25s
            // new MultiThreadedRenderer() // 11s
            // new ThreadPoolRenderer() // 10s
            // new ForkJoinRenderer() // 8.0s
        );

        canvas = new ImageCanvas(renderer);
        canvas.setPreferredSize(new Dimension(675, 450));
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent event) {
                handleClick( //
                    new Pixel(event.getX(), event.getY()), //
                    event.getButton() == MouseEvent.BUTTON1 //
                );
            }
        });

        frame = new Frame("FractalApp");
        frame.addWindowListener(EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        updateImage();
    }

    private void updateImage() {
        canvas.setImage( //
            params.pixelImage(image, canvas.getSize()) //
                );//.mapC(FractalApp::threadColor));
    }

    private void adjustMaxIter() {
        final long maxIter = //
            (long) Math.min(1e5, Math.sqrt(1 / params.pixelDist) + 10);
        image.setMaxIter(maxIter);
    }

    private void handleClick(final Pixel mouse, final boolean isLeft) {
        final Point mousePoint = //
            params.pointAt(mouse, canvas.getSize());
        params = params.zoomedAround(mousePoint, isLeft ? 0.5 : 2);
        adjustMaxIter();
        updateImage();
        canvas.repaint();
    }

    private static Color threadColor(final Color c) {
        final float[] hsb = //
            Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        final float index = Thread.currentThread().getId() % THREADS;
        return Color.getHSBColor(index/THREADS, hsb[1], hsb[2]);
    }

    private static final WindowAdapter EXIT_ON_CLOSE = new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent event) {
            System.exit(0);
        }
    };
}
