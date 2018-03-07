package squidpony.dd.data;

import squidpony.squidgrid.gui.gdx.SColor;

/**
 * Created by Tommy Ettinger on 11/26/2017.
 */
public enum Wood implements Material {
    BIRCH(SColor.BIRCH_BROWN, SColor.ALOEWOOD),
    ASPEN(SColor.CW_ALMOST_WHITE, SColor.WILLOW_GREY),
    CHERRY(SColor.RED_BEAN, SColor.RED_BIRCH, 120, 170),
    MAPLE(SColor.CLOVE_BROWN, SColor.BAIKO_BROWN, 110),
    WALNUT(SColor.WALNUT, SColor.WILLOW_GREY),
    KOA(SColor.FLATTERY_BROWN, SColor.DB_SEAL_BROWN, 150, 200),
    WILLOW(SColor.WILLOW_LEAVES_UNDERSIDE, SColor.WILLOW_DYE, 80, 80),
    MANZANITA(SColor.RUSSET, SColor.GREEN_BAMBOO, 90, 300);

    public SColor front, back;
    public int value; //base material is 100
    public int hardness; //average hardness

    Wood(SColor front, SColor back) {
        this(front, back, 100, 100);
    }

    Wood(SColor front, SColor back, int value) {
        this(front, back, value, 100);
    }

    Wood(SColor front, SColor back, int value, int hardness) {
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
        return '=';
    }

}
