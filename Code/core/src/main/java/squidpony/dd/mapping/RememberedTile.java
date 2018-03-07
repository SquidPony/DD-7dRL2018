package squidpony.dd.mapping;

import com.badlogic.gdx.graphics.Color;
import squidpony.dd.Dive;
import squidpony.dd.data.Physical;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SColorFactory;

/**
 * Contains the player's memory of the tile. May eventually have more detail for "look" commands, but right now is
 * focused purely on drawing maps.
 *
 * @author Eben Howard
 */
public class RememberedTile {

    public static final SColor memoryColor = SColor.DB_INK;
    public static final float memoryColorFloat = memoryColor.toFloatBits();
    public static final float frontFade = 0.2f;
    public static final float backFade = 0.89f;

    public char symbol = ' ';
    public SColor front = SColor.TRANSPARENT;
    public SColor miniMapColor = SColor.TRANSPARENT;

    public RememberedTile(EpiTile tile) {
        remake(tile);
    }

    public void remake(EpiTile tile) {
        symbol = tile.getSymbolUninhabited();
        if (tile.getForegroundColor().equals(SColor.TRANSPARENT)) {
            front = SColor.TRANSPARENT;
        } else {
            front = new SColor(Dive.colorCenter.lerp(tile.getForegroundColor(), memoryColor, frontFade));// :( I miss SColorCenter
        }
        if (tile.getCreature() != null) {
            miniMapColor = SColor.SCARLET;
        } else {
            Physical p = tile.getLargeNonCreature();
            if (p == null) {
                miniMapColor = SColor.DARK_INDIGO;
            } else {
                switch (p.glyph) {
                    case '#':
                        miniMapColor = SColor.SILVER_GREY;
                        break;
                    case '+':
                        miniMapColor = SColor.TAWNY;
                        break;
                    default:
                        miniMapColor = SColor.FLAX;
                        break;
                }
            }
        }
    }
}
