package squidpony.dd.data;

import squidpony.squidgrid.gui.gdx.SColor;

/**
 * Created by Tommy Ettinger on 11/26/2017.
 */
public enum Hide implements Material {
    BEAR_HIDE(SColor.CW_DARK_BROWN, SColor.DB_SEAL_BROWN, 130, 40),
    TIGER_HIDE(SColor.BRIGHT_GOLD_BROWN, SColor.CW_ALMOST_BLACK, 220, 32),
    WOLF_HIDE(SColor.CW_DARK_GRAY, SColor.CW_LIGHT_GRAY, 120, 35),
    RABBIT_HIDE(SColor.WHITE_MOUSE, SColor.SILVER_GREY, 20, 9),
    RHINOCEROS_HIDE(SColor.DB_ELEPHANT, SColor.DB_ELEPHANT, 200, 80),
    CROCODILE_HIDE(SColor.SISKIN_SPROUT_YELLOW, SColor.WILLOW_DYE, 140, 65);

    public SColor front, back;
    public int value; //base material is 100
    public int hardness; //average hardness

    Hide(SColor front) {
        this(front, front, 100, 30);
    }

    Hide(SColor front, SColor back) {
        this(front, back, 100, 30);
    }

    Hide(SColor front, SColor back, int value) {
        this(front, back, value, 30);
    }

    Hide(SColor front, SColor back, int value, int hardness) {
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
        return 'ᴥ';
    }

}
