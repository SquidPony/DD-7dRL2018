package squidpony.epigon.display;

import com.badlogic.gdx.graphics.Color;
import squidpony.ArrayTools;
import squidpony.epigon.data.specific.Physical;
import squidpony.epigon.mapping.EpiMap;
import squidpony.epigon.mapping.EpiTile;
import squidpony.epigon.universe.LiveValueModification;
import squidpony.epigon.universe.Stat;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.TextCellFactory.Glyph;
import squidpony.squidmath.Coord;

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
    private int midLeft;
    private int midRight;
    private int height;
    private Physical player;

    private EpiMap map;

    private int scrollOffsetY;
    private boolean pressedUp; // attempting to hover

    public FallingHandler(SparseLayers layers) {
        width = layers.gridWidth;
        halfWidth = width / 2;
        quarterWidth = halfWidth / 2;
        midLeft = halfWidth - quarterWidth;
        midRight = halfWidth + quarterWidth;
        height = layers.gridHeight;
        this.layers = layers;

        ArrayTools.fill(this.layers.backgrounds, layers.defaultPackedBackground);
        hide();
    }

    public void setPlayer(Physical player){
        this.player = player;
    }

    public void hide() {
        layers.setVisible(false);
    }

    public void show(EpiMap map) {
        this.map = map;
        update(0);
        layers.setVisible(true);
    }

    public void update(){
        update(scrollOffsetY);
    }

    public void update(int yOffset) {
        clear();
        scrollOffsetY = yOffset;
        int dx = -1; // TODO - have screen shift left or right when player gets outside of side mid range
        int dy = -1 + yOffset;
        for (int x = 1; x < width - 2; x++){
            for (int y = 1; y < height - 1; y++){
                int x2 = x + dx;
                int y2 = y + dy;
                if (map.inBounds(x2,y2)){
                    put(x, y, map.contents[x2][y2]);
                } else {
                    put(x, y, ' ');
                }
            }
        }

        put(player.location.x + 1, player.location.y  + 1 - yOffset, player.symbol, player.color);
    }

    private void clear() {
        layers.clear(0);

        doBorder(-scrollOffsetY, map.height);
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

    private void put(int x, int y, EpiTile tile){
        put(x, y, tile.getSymbol(), tile.getForegroundColor());
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

    private void damagePlayer() {
        player.stats.get(Stat.VIGOR).modify(LiveValueModification.add(-1));

        wigglePlayer();
        layers.burst(player.location.x + 1, player.location.y - scrollOffsetY, 2, Radius.SPHERE, "*^&%*", SColor.CW_PALE_YELLOW.toFloatBits(), SColor.CW_FLUSH_RED.toFloatBits(), 0.3f);
    }

    private void wigglePlayer() {
        Glyph g = layers.glyphFromGrid(player.location.x + 1, player.location.y - scrollOffsetY);
        layers.wiggle(0f, g, 0.2f, () -> layers.removeGlyph(g));
    }

    public void move(Direction dir) {
        if (dir == Direction.UP) {
            pressedUp = true;
            return;
        }

        Coord target = player.location.translate(dir);
        // check against both backing map and current visible space vertically
        if (target.isWithinRectangle(0, scrollOffsetY, map.width, Math.min(map.height, scrollOffsetY + height - 1))) {
            player.location = target;
            update();
        } else {
            wigglePlayer();
        }
    }

    public void fall() {
        if (!pressedUp) {
            player.location = player.location.translate(Direction.DOWN);
        }

        if (player.location.y <= scrollOffsetY) {
            player.location = player.location.translate(Direction.DOWN);
            damagePlayer();
        } else if (map.contents[player.location.x][player.location.y].blockage != null) {
            damagePlayer();
        }

        pressedUp = false;
        update(scrollOffsetY + 1);
    }

}
