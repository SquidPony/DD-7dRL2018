package squidpony.dd.mapping;

import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;
import squidpony.squidmath.StatefulRNG;

import static squidpony.dd.Dive.rootChaos;

/**
 * This represents a single explorable map level.
 *
 * Each cell is considered to be 1 meter by 1 meter square.
 *
 * A null tile represents open space with no special properties or opacities to things passing
 * through. They should not be considered a vacuum, but rather normal air.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class EpiMap {

    public int width, height;
    public EpiTile[][] contents;
    public RememberedTile[][] remembered;
    public double[][] resistances;
    public StatefulRNG chaos;
    public EpiMap(int width, int height) {
        this.width = width;
        this.height = height;
        chaos = new StatefulRNG(rootChaos.nextLong());
        contents = new EpiTile[width][height];
        remembered = new RememberedTile[width][height];
        resistances = new double[width][height];
    }

    public EpiMap() {
        this(3, 3);
    }

    public boolean inBounds(Coord p) {
        return inBounds(p.x, p.y);
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public double[][] opacities() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                resistances[x][y] = contents[x][y].opacity();
            }
        }
        return resistances;
    }

    public char[][] simpleChars() {
        char[][] ret = new char[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ret[x][y] = contents[x][y].getSymbol();
            }
        }
        return ret;
    }

    public char altSymbolOf(char symbol) {

        switch (symbol) {
            case '¸'://grass
            case '"':
                return '¸';
            case '~':
                return '≈';
            case 'ø':
                return ' ';
            default://opaque items
                return symbol;
        }
    }

    public SColor colorOf(char symbol) {
        SColor color;
        switch (symbol) {
            case '.'://stone ground
                color = SColor.SLATE_GRAY;
                break;
            case '"':
            case '¸'://grass
                color = SColor.GREEN;
                break;
            case ','://pathway
                color = SColor.STOREROOM_BROWN;
                break;
            case 'c':
                color = SColor.SEPIA;
                break;
            case '/':
                color = SColor.BROWNER;
                break;
            case '~':
                color = SColor.AZUL;
                break;
            case '≈':
                color = SColor.CW_FLUSH_BLUE;
                break;
            case '<':
            case '>':
                color = SColor.SLATE_GRAY;
                break;
            case 't':
                color = SColor.BROWNER;
                break;
            case 'm':
                color = SColor.BAIKO_BROWN;
                break;
            case 'u':
                color = SColor.TAN;
                break;
            case 'T':
            case '₤':
                color = SColor.FOREST_GREEN;
                break;
            case 'E':
                color = SColor.SILVER;
                break;
            case 'S':
                color = SColor.BREWED_MUSTARD_BROWN;
                break;
            case '#':
                color = SColor.SLATE_GRAY;
                break;
            case '+':
                color = SColor.BROWNER;
                break;
            case 'A':
                color = SColor.ALICE_BLUE;
                break;
            case 'ø':
            case ' ':
                color = SColor.DB_INK;
                break;
            default://opaque items
                color = SColor.DEEP_PINK;
        }
        return color;
    }
}
