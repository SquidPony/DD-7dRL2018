package squidpony.epigon.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import java.util.ListIterator;
import squidpony.ArrayTools;
import squidpony.epigon.data.specific.Physical;
import squidpony.epigon.dm.RecipeMixer;
import squidpony.epigon.mapping.EpiMap;
import squidpony.epigon.mapping.EpiTile;
import squidpony.epigon.universe.Element;
import squidpony.epigon.universe.LiveValueModification;
import squidpony.epigon.universe.Stat;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidColorCenter;
import squidpony.squidgrid.gui.gdx.TextCellFactory.Glyph;
import squidpony.squidmath.Coord;
import squidpony.squidmath.DeckRNG;
import squidpony.squidmath.RNG;

/**
 * Oh no, you're falling!
 */
public class FallingHandler {

    private SColor headingColor = SColor.CW_BLUE;
    private SColor keyColor = SColor.FLORAL_LEAF;

    private SquidColorCenter colorCenter;

    private SparseLayers layers;
    private int width;
    private int halfWidth;
    private int quarterWidth;
    private int midLeft;
    private int midRight;
    private int height;
    private Physical player;

    private EpiMap map;
    private Physical trail;
    private RNG rng = new DeckRNG();
    private FxHandler fx;

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

        trail = new Physical();
        trail.symbol = 0x2801; // center braille

        colorCenter = new SquidColorCenter();

        layers.addLayer();//first added panel adds at level 1, used for cases when we need "extra background"
        layers.addLayer();//next adds at level 2, used for the cursor line
        layers.addLayer();//next adds at level 3, used for effects
        double[][] fov = new double[width][height];
        ArrayTools.fill(fov, 1);
        fx = new FxHandler(layers, 3, colorCenter, fov);

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

        if (map.contents[player.location.x][player.location.y].blockage != null) {
            damagePlayer();
        }

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
        layers.burst(player.location.x + 1, player.location.y - scrollOffsetY, 2, Radius.SPHERE, "*^!!*", SColor.CW_PALE_YELLOW.toFloatBits(), SColor.CW_FLUSH_RED.toFloatBits(), 0.3f);
    }

    private void wigglePlayer() {
        Glyph g = layers.glyphFromGrid(player.location.x + 1, player.location.y - scrollOffsetY);
        layers.wiggle(0f, g, 0.2f, () -> layers.removeGlyph(g));
    }

    public void move(Direction dir) {
        doMovement(dir.deltaX, dir.deltaY);
    }

    private void doMovement(int x, int y) {
        if (y < 0) {
            pressedUp = true;
            y = 0;
        }

        Coord target = player.location.translate(x, y);

        if (target.isWithinRectangle(0, scrollOffsetY, map.width, Math.min(map.height, scrollOffsetY + height - 2))) {
            
            EpiTile tile = map.contents[player.location.x][player.location.y];
            tile.blockage = null;
            Physical floor = tile.floor;
            Physical t = RecipeMixer.buildPhysical(trail);
            t.color = floor.color == SColor.TRANSPARENT.toFloatBits() ? rng.getRandomElement(SColor.RAINBOW).toFloatBits() : floor.color;
            tile.floor = t;
            
            tile = map.contents[target.x][target.y];
            player.location = target;
            if (tile.blockage != null) {
                damagePlayer();
            }
            ListIterator<Physical> li = tile.contents.listIterator();
            while (li.hasNext()) {
                Physical p = li.next();
                player.addToInventory(p);
                fx.twinkle(target, Element.FIRE);// have to have it lower due to border offset
                li.remove();
            }
            update();
        } else {
            wigglePlayer();
        }
    }

    public void processInput() {
        int offX = 0, offY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
            --offY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            ++offY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
            --offX;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
            ++offX;
        }
        doMovement(offX, offY);
    }

    public void fall() {
        if (!pressedUp){
            player.location = player.location.translate(Direction.DOWN);
        }

        if (player.location.y <= scrollOffsetY) {
            player.location = player.location.translate(Direction.DOWN);
            damagePlayer();
        } 

        pressedUp = false;
        update(scrollOffsetY + 1);
    }

}
