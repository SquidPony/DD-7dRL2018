package squidpony.dd.mapping;

import squidpony.squidgrid.gui.gdx.SColor;

import java.util.ArrayList;
import java.util.List;

import squidpony.dd.data.Physical;

/**
 * This class holds the objects in a single grid square.
 *
 * Through this class, one can get how the tile should be displayed, a compiled description of
 * what's in it, and the resistance factor to light, movement, etc.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class EpiTile {

    public Physical floor;
    public Physical blockage;
    public List<Physical> contents = new ArrayList<>();

    /**
     * Returns the total combined opacity of this cell, with 1.0 being fully opaque and 0.0 being
     * fully transparent.
     */
    public double opacity() {
        return blockage == null ? 0 : 1;
    }

    /**
     * Returns the character representation of this tile.
     *
     * @return a char representing the contents of the tile, with creatures not rendered (they render themselves)
     */
    public char getSymbol() {
        //check in order of preference
        if (blockage != null) {
            return blockage.glyph;
        } else if (!contents.isEmpty()){
            return contents.get(0).glyph; // arbitrarily get first thing in list
        } else if (floor != null) {
            return floor.glyph;
        }

        return ' '; //default to no representation;
    }

    /**
     * Returns the character representation of this tile with no consideration for any Creature that may be present.
     *
     * @return
     */
    public char getSymbolUninhabited() { // TODO - check for aliveness
        if (blockage != null) {
            return blockage.glyph;
        } else if (!contents.isEmpty()) {
            return contents.get(0).glyph; // arbitrarily get first thing in list
        } else if (floor != null) {
            return floor.glyph;
        }
        return ' '; //default to no representation
    }

    public SColor getForegroundColor() {
        //check in order of preference
        if (blockage != null) {
            return blockage.color;
        } else if (!contents.isEmpty()){
            return contents.get(0).color; // arbitrarily get first thing in list
        } else if (floor != null) {
            return floor.color;
        }

        return SColor.TRANSPARENT;
    }

    public void remove(Physical phys) {
        if (phys != null) {
            if (!phys.small) {
                if (phys.equals(blockage)) {
                    blockage = null;
                }
            } else {
                contents.remove(phys);
            }
        }
    }

    public void add(Physical phys) {
        if (phys != null) {
            if (!phys.small) {
                if (blockage == null) {
                    blockage = phys;
                }
            } else {
                contents.add(phys);
            }
        }
    }

    public void add(Iterable<Physical> adding) {
        for (Physical p : adding) {
            add(p);
        }
    }

    public Physical getCreature() {
        return blockage != null && blockage.alive ? blockage : null;
    }

    public Physical getLargeNonCreature() {
        return blockage != null && !blockage.alive ? blockage : null;
    }
}
