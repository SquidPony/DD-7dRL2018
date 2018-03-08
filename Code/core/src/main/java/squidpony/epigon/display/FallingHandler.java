package squidpony.epigon.display;

import com.badlogic.gdx.graphics.Color;
import squidpony.ArrayTools;
import squidpony.epigon.data.specific.Physical;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;

/**
 * Oh no, you're falling!
 */
public class FallingHandler {

    private SColor headingColor = SColor.CW_BLUE;
    private SColor keyColor = SColor.FLORAL_LEAF;

    private SparseLayers layers;
    private int width;
    private int halfWidth;
    private int quarterWidth;
    private int height;
    private Physical player;

    private int scrollOffsetY;

    public FallingHandler(SparseLayers layers) {
        width = layers.gridWidth;
        halfWidth = width / 2;
        height = layers.gridHeight;
        this.layers = layers;

        ArrayTools.fill(this.layers.backgrounds, layers.defaultPackedBackground);
        hide();
    }

    public void hide() {
        layers.setVisible(false);
    }

    public void show(){
        layers.setVisible(true);
    }

    private void clear() {
        layers.clear(0);

        doBorder();
    }

    private void doBorder() {
        int w = width;
        int h = height;
        for (int x = 0; x < w; x++) {
            put(x, 0, '─');
            put(x, h - 1, '─');
        }
        for (int y = 0; y < h; y++) {
            put(0, y, '│');
            put(w - 1, y, '│');
        }
        put(0, 0, '┌');
        put(w - 1, 0, '┐');
        put(0, h - 1, '└');
        put(w - 1, h - 1, '┘');

        String title = "Falling (Side View)";
        int x = width / 2 - title.length() / 2;
        put(x, 0, title);
    }

    /**
     * Draws some characters to indicate that the current view has more content above or below.
     *
     * @param y
     * @param contentHeight
     */
    private void doBorder(int y, int contentHeight) {
        doBorder();
        if (y < 0) {
            put(width - 1, 1, '▲');
        }
        if (contentHeight + y > height) {
            put(width - 1, height - 2, '▼');
        }
        put(width - 1, Math.round(((height - 5f) * y) / (height - contentHeight)) + 2, '█');
    }

    private void put(int x, int y, String s) {
        for (int sx = 0; sx < s.length() && sx + x < width; sx++) {
            put(sx + x, y, s.charAt(sx));
        }
    }

    private void put(int x, int y, String s, Color color) {
        for (int sx = 0; sx < s.length() && sx + x < width; sx++) {
            put(sx + x, y, s.charAt(sx), color);
        }
    }

    private void put(int x, int y, char c) {
        layers.put(x, y, c);
    }

    private void put(int x, int y, char c, Color color) {
        layers.put(x, y, c, color);
    }

    private void put(int x, int y, char c, float color) {
        layers.put(x, y, c, color);
    }

    public void move(Direction dir) {
        switch (dir) {
            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
        }
    }

}
