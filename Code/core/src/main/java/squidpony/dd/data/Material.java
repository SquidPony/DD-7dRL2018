package squidpony.dd.data;

import squidpony.squidgrid.gui.gdx.SColor;

/**
 * Base for anything that can be used as a base to create physical objects
 */
public interface Material {

    SColor color();

    int value();

    int hardness();

    char glyph();
}
