package squidpony.epigon.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.RNG;
import squidpony.squidmath.StatefulRNG;

/**
 * An object in the world
 */
public class Physical {

    public String name;
    public char glyph;
    public SColor color;
    public Rating rarity = Rating.SLIGHT;
    public boolean small;
    public boolean alive;
    public int quantity;

    public List<Physical> inventory = new ArrayList<>();
    public Physical armor;
    public Physical weapon;
    public Physical jewelry;

    public Map<Stat, Integer> stats;
    public Map<Stat, Integer> baseStats;

    public Material sourceMaterial;

    private RNG rng = new StatefulRNG();

    public Physical(String name, char glyph, SColor color) {
        this.name = name;
        this.glyph = glyph;
        this.color = color;

        for (Stat s : Stat.values()){
            int n = rng.between(5, 20);
            baseStats.put(s, n);
            stats.put(s, rng.between(n - 3, n + 2));
        }
    }

    public Physical(Material mat) {
        this(mat.toString(), mat.glyph(), mat.color());
        sourceMaterial = mat;
    }
}
