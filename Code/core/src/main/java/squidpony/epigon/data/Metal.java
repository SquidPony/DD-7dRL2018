package squidpony.epigon.data;

import squidpony.squidgrid.gui.gdx.SColor;

/**
 * Created by Tommy Ettinger on 11/26/2017.
 */
public enum Metal implements Material {
    BRASS(SColor.BRASS, SColor.CW_GOLD, 170, 1600),
    LEAD(SColor.DB_LEAD, SColor.DB_LEAD, 110, 700),
    COPPER(SColor.COPPER, SColor.COPPER_ROSE, 230, 1100),
    SILVER(SColor.SILVER, SColor.SILVER, 500, 1000),
    GOLD(SColor.CW_BRIGHT_GOLD, SColor.OLD_GOLD, 600, 700),
    TIN(SColor.CW_GRAY_WHITE, SColor.CW_GRAY_WHITE, 140, 1100),
    IRON(SColor.DB_IRON, SColor.DB_IRON, 250, 2500),
    STEEL(SColor.CW_GRAY_WHITE, SColor.STEEL_BLUE, 310, 2900),
    PLATINUM(SColor.PLATINUM, SColor.DB_PLATINUM, 750, 1900);

    public SColor front, back;
    public int value; //base material is 100, metals are 200
    public int hardness; //average hardness

    Metal(SColor front) {
        this(front, front, 200, 2000);
    }

    Metal(SColor front, SColor back) {
        this(front, back, 200, 2000);
    }

    Metal(SColor front, SColor back, int value) {
        this(front, back, value, 2000);
    }

    Metal(SColor front, SColor back, int value, int hardness) {
        this.front = front;
        this.back = back;
        this.value = value;
        this.hardness = hardness;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace('_', ' ');
    }

    @Override
    public SColor color() {
        return front;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public int hardness() {
        return hardness;
    }
    @Override
    public char glyph()
    {
        return '⊕';
    }

}
