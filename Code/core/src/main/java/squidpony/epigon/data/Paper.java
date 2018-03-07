package squidpony.epigon.data;

import squidpony.squidgrid.gui.gdx.SColor;

/**
 * Created by Tommy Ettinger on 11/26/2017.
 */
public enum Paper implements Material {
    PAPYRUS(SColor.DB_DAFFODIL),
    VELLUM(SColor.LEMON_CHIFFON),
    PARCHMENT(SColor.COSMIC_LATTE),
    SKIN(SColor.DB_NUDE),
    EGGSHELL_PAPER(SColor.EGGSHELL_PAPER),
    ASPEN_BARK(SColor.CW_ALMOST_WHITE);

    public SColor front, back;
    public int value; //base material is 100
    public int hardness; //average hardness

    Paper(SColor front) {
        this(front, front, 100, 10);
    }

    Paper(SColor front, SColor back) {
        this(front, back, 100, 10);
    }

    Paper(SColor front, SColor back, int value) {
        this(front, back, value, 10);
    }

    Paper(SColor front, SColor back, int value, int hardness) {
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
        return '⌷';
    }

}
