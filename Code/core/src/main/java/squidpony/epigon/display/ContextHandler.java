package squidpony.epigon.display;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.scenes.scene2d.Actor;
import squidpony.ArrayTools;
import squidpony.epigon.Dive;
import squidpony.epigon.data.specific.Physical;
import squidpony.epigon.mapping.EpiTile;
import squidpony.epigon.universe.LiveValue;
import squidpony.epigon.universe.Stat;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidmath.Coord;
import squidpony.squidmath.EnumOrderedMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the contents relevant to the current context mode, switching modes as needed.
 *
 * @author Eben Howard
 */
public class ContextHandler {

    public enum ContextMode {
        TILE_CONTENTS, INVENTORY, STAT_DETAILS, MINI_MAP, MESSAGE;

        private final String name;

        private ContextMode() {
            name = Arrays.stream(name().split("_"))
                .map(s -> s.substring(0, 1) + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        }

        private int position() {
            for (int i = 0; i < values().length; i++) {
                if (values()[i] == this) {
                    return i;
                }
            }
            return -1;
        }

        public ContextMode next() {
            return values()[(position() + 1) % values().length];
        }

        public ContextMode prior() {
            return values()[(position() + (values().length - 1)) % values().length];
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private SquidPanel back;
    private SquidPanel front;
    private Actor miniMap;
    private int width;
    private int height;
    private TextCellFactory miniMapFont;
    private ContextMode contextMode = ContextMode.TILE_CONTENTS;
    private EnumOrderedMap<ContextMode, char[][]> cachedTexts = new EnumOrderedMap<>(ContextMode.class);
    private EnumOrderedMap<ContextMode, float[][]> cachedColors = new EnumOrderedMap<>(ContextMode.class);
    private EnumOrderedMap<ContextMode, Boolean> cacheIsValid = new EnumOrderedMap<>(ContextMode.class);
    private float defaultFrontColor;

    public Coord arrowLeft;
    public Coord arrowRight;

    public ContextHandler(SquidLayers layers, SparseLayers mainMap) {
        width = layers.getGridWidth();
        height = layers.getGridHeight();
        back = layers.getBackgroundLayer();
        front = layers.getForegroundLayer();
        miniMap = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                Frustum frustum = layers.getStage().getCamera().frustum;
                float xo = getX() + Dive.contextSize.cellWidth, yo = getY(), yOff = yo + 1f + mainMap.gridHeight * 3f;
                //mainMap.font.configureShader(batch);
                mainMap.getLayer(0).draw(batch, miniMapFont, frustum, xo, yOff, '\u0000');
                int x, y;
                ArrayList<TextCellFactory.Glyph> glyphs = mainMap.glyphs;
                for (int i = 0; i < glyphs.size(); i++) {
                    TextCellFactory.Glyph glyph = glyphs.get(i);
                    if(glyph == null)
                        continue;
                    //glyph.act(Gdx.graphics.getDeltaTime());
                    if((x = Math.round((glyph.getX() - xo) / 3)) < 0 || x >= mainMap.gridWidth ||
                            (y = Math.round((glyph.getY() - yOff) / -3 + mainMap.gridHeight)) < 0 || y >= mainMap.gridHeight ||
                            mainMap.backgrounds[x][y] == 0f)
                        continue;
                    miniMapFont.draw(batch, '\u0000', glyph.getColor(), glyph.getX(), glyph.getY());
                }
            }
        };
        miniMapFont = mainMap.font.copy().width(3f).height(3f).initBySize();
        layers.addActor(miniMap);
        miniMap.setVisible(false);
        arrowLeft = Coord.get(1, 0);
        arrowRight = Coord.get(layers.getGridWidth() - 2, 0);

        defaultFrontColor = front.getDefaultForegroundColor().toFloatBits();
        for (ContextMode mode : ContextMode.values()) {
            cachedTexts.put(mode, ArrayTools.fill(' ', width, height));
            cachedColors.put(mode, ArrayTools.fill(defaultFrontColor, width, height));
            cacheIsValid.put(mode, Boolean.FALSE);
        }

        ArrayTools.fill(back.colors, back.getDefaultForegroundColor().toFloatBits());
        ArrayTools.fill(back.contents, '\0');
        ArrayTools.fill(front.colors, defaultFrontColor);
        ArrayTools.fill(front.contents, ' ');
    }

    private void clear() {
        ArrayTools.fill(front.contents, ' ');

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

        String title = contextMode.toString();
        int x = width / 2 - title.length() / 2;
        put(x, 0, title);

        put(arrowLeft.x, arrowLeft.y, '◀');
        put(arrowRight.x, arrowRight.y, '▶');
    }

    private void put(CharSequence[] text) {
        for (int y = 0; y < text.length && y < height - 2; y++) {
            put(1, y + 1, text[y]);
        }
    }

    private void put(int x, int y, CharSequence s) {
        for (int sx = 0; sx < s.length() && sx + x < width; sx++) {
            put(sx + x, y, s.charAt(sx));
        }
    }

    private void put(int x, int y, char c){
        put(x, y, c, defaultFrontColor);
    }

    private void put(int x, int y, char c, float color) {
        front.put(x, y, c);
        if (x >= 0 && x < width && y >= 0 && y < height) {
            cachedTexts.get(contextMode)[x][y] = c;
            cachedColors.get(contextMode)[x][y] = color;
        }
    }

    private void putFromCache(){
        for (int x = 0; x< width;x++){
            for(int y =0; y<height;y++){
                front.put(x, y, cachedTexts.get(contextMode)[x][y], cachedColors.get(contextMode)[x][y]);
            }
        }
    }

    public void next() {
        front.summon(arrowRight.x, arrowRight.y, arrowRight.x + 1, arrowRight.y - 2, '✔', SColor.CW_HONEYDEW,
            SColor.CW_RICH_HONEYDEW.cpy().sub(0f, 0f, 0f, 0.8f), 0f, 0.6f);
        switchTo(contextMode.next());
    }

    public void prior() {
        front.summon(arrowLeft.x, arrowLeft.y, arrowLeft.x + 1, arrowLeft.y - 2, '✔', SColor.CW_HONEYDEW,
            SColor.CW_RICH_HONEYDEW.cpy().sub(0f, 0f, 0f, 0.8f), 0f, 0.6f);
        switchTo(contextMode.prior());
    }

    public void invalidateCache(ContextMode mode){
        cacheIsValid.put(mode, Boolean.FALSE);
    }

    private void switchTo(ContextMode mode) {
        if (contextMode == ContextMode.MINI_MAP) {
            miniMap.setVisible(false);
        }
        contextMode = mode;
        if (cacheIsValid.get(mode)) { // map cache is never valid
            putFromCache();
        } else {
            switch (mode) {
                case INVENTORY:
                    contextInventory(Collections.emptyList());
                    break;
                case MESSAGE:
                    message("");
                    break;
                case MINI_MAP:
                    contextMiniMap();
                    break;
                case STAT_DETAILS:
                    contextStatDetails(null, null);
                    break;
                case TILE_CONTENTS:
                    tileContents(null, null);
            }
        }
    }

    public void contextStatDetails(Stat stat, LiveValue lv) {
        contextMode = ContextMode.STAT_DETAILS;
        miniMap.setVisible(false);
        clear();
        if (stat != null && lv != null) {
            put(1, 1, stat.toString() + " (" + stat.nick() + ")");
            put(1, 2, "Base:  " + lv.base());
            put(1, 3, "Max:   " + lv.max());
            put(1, 4, "Delta: " + lv.delta());
        }
        cacheIsValid.put(contextMode, Boolean.TRUE);
    }

    public void contextMiniMap() {
        contextMode = ContextMode.MINI_MAP;
        clear();
        miniMap.setVisible(true);
    }

    public void contextInventory(List<Physical> inventory) {
        contextMode = ContextMode.INVENTORY;
        miniMap.setVisible(false);
        clear();
        put(inventory.stream()
            .map(i -> i.name)
            .collect(Collectors.toList())
            .toArray(new String[]{}));
        cacheIsValid.put(contextMode, Boolean.TRUE);
    }

    public void tileContents(Coord location, EpiTile tile) {
        contextMode = ContextMode.TILE_CONTENTS;
        miniMap.setVisible(false);
        clear();
        if (location != null && tile != null) {
            String tileDescription = "[" + location.x + ", " + location.y + "] ";
            if (tile.floor != null) {
                tileDescription += tile.floor.name + " floor";
            } else {
                tileDescription += "empty space";
            }
            if (!tile.contents.isEmpty()) {
                tileDescription = tile.contents.stream()
                    .map(p -> p.name)
                    .collect(Collectors.joining("\n", tileDescription + "\n", ""));
            }
            put(tileDescription.split("\n"));
        }
        cacheIsValid.put(contextMode, Boolean.TRUE);
    }

    public void message(CharSequence... text) {
        contextMode = ContextMode.MESSAGE;
        miniMap.setVisible(false);
        clear();
        put(text);
        cacheIsValid.put(contextMode, Boolean.TRUE);
    }

}
