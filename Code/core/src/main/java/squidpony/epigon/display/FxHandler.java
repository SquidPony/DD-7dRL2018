package squidpony.epigon.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import squidpony.ArrayTools;
import squidpony.Maker;
import squidpony.epigon.Utilities;
import squidpony.epigon.universe.Element;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.gui.gdx.PanelEffect;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidColorCenter;
import squidpony.squidmath.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controls what happens on the full map overlay panel.
 *
 * @author Eben Howard
 */
public class FxHandler {

    private static char[] explosionChars = new char[]{'%', '$', '&', '!'};
    private static char[] zapChars = new char[]{};

    private SparseLayers fx;
    private int layer;
    private int width;
    private int height;
    private SquidColorCenter colorCenter;
    private GreasedRegion viable;
    double[][] seen;
    private StatefulRNG rng = new StatefulRNG();

    public FxHandler(SparseLayers layers, int layerNumber, SquidColorCenter colorCenter, double[][] visible) {
        fx = layers;
        width = layers.gridWidth();
        height = layers.gridHeight();
        layer = layerNumber;
        this.colorCenter = colorCenter;
        viable = new GreasedRegion(visible.length, visible[0].length);
        seen = visible;
    }

    public void sectorBlast(Coord origin, Element element, int size, Radius radius) {
        fx.addAction(new ConeEffect(0.85f, viable.refill(seen, 0.001, 999.0),
            origin, size,
            rng.nextDouble(360.0),
            radius,
            Maker.makeList(colorCenter.saturate(element.color, 0.3),
                colorCenter.light(colorCenter.saturate(element.color, 0.15)),
                colorCenter.lightest(element.color),
                colorCenter.lighter(colorCenter.desaturate(element.color, 0.15)),
                colorCenter.desaturate(element.color, 0.3),
                colorCenter.dim(colorCenter.desaturate(element.color, 0.45)).sub(0, 0, 0, 0.35f),
                colorCenter.dimmer(colorCenter.desaturate(element.color, 0.6)).sub(0, 0, 0, 0.85f))));
    }

    public void staticStorm(Coord origin, Element element, int size, Radius radius) {
        fx.addAction(new DustEffect(1f, viable.refill(seen, 0.001, 999.0), origin, size, radius,
            Maker.makeList(colorCenter.saturate(element.color, 0.3),
                colorCenter.light(colorCenter.saturate(element.color, 0.15)),
                colorCenter.lightest(element.color),
                colorCenter.lighter(colorCenter.desaturate(element.color, 0.15)),
                colorCenter.desaturate(element.color, 0.3),
                colorCenter.dim(colorCenter.desaturate(element.color, 0.45), 0.1),
                colorCenter.dim(colorCenter.desaturate(element.color, 0.6), 0.2).sub(0, 0, 0, 0.3f)
            )));
    }

    public void twinkle(Coord origin, Element element) {
        fx.addAction(new TwinkleEffect((float) rng.between(1.2, 3.1), rng.between(2, 4), origin,
            Maker.makeList(
                colorCenter.dim(colorCenter.desaturate(element.color, 0.6), 0.2).sub(0, 0, 0, 0.3f),
                colorCenter.desaturate(element.color, 0.3),
                colorCenter.saturate(element.color, 0.3),
                colorCenter.light(colorCenter.saturate(element.color, 0.15)),
                colorCenter.lightest(element.color),
                colorCenter.lighter(colorCenter.desaturate(element.color, 0.15)),
                colorCenter.desaturate(element.color, 0.3),
                colorCenter.dim(colorCenter.desaturate(element.color, 0.45), 0.1),
                colorCenter.dim(colorCenter.desaturate(element.color, 0.6), 0.2).sub(0, 0, 0, 0.3f)
            )));
    }

    public void line(Coord origin, Coord end, Element element) {
        Coord[] path = Bresenham.line2D_(origin, end);
        fx.addAction(new LineEffect(path.length * 0.2f, path,
            colorCenter.zigzagGradient(element.color, colorCenter.lightest(element.color), 32)));
    }

    public void zapBoom(Coord origin, Coord end, Element element) {
        Coord[] path = Bresenham.line2D_(origin, end);
        List<Color> colors = colorCenter.zigzagGradient(element.color, colorCenter.lightest(element.color), 6);
        fx.addAction(
            Actions.sequence(
                new LineEffect(path.length * 0.06f, path, colors),
                Actions.parallel(
                    new TwinkleEffect(0.4f, rng.between(2, 4), end, colors.stream().map(c -> colorCenter.lighter(c)).collect(Collectors.toList())),
                    new DustEffect(0.3f, viable.refill(seen, 0.001, 999.0), end, 3, Radius.DIAMOND, colors))));
    }

    public void layeredSparkle(Coord origin, int size, Radius radius) {
        fx.addAction(new ColorSparkleEffect(1f, viable.refill(seen, 0.001, 999.0), origin, size, radius, rng));
    }

    public class TwinkleEffect extends PanelEffect {

        public int cycles;
        public float[] colors;
        public Coord c;

        public TwinkleEffect(float duration, int cycles, Coord center, List<? extends Color> coloring) {
            super(fx, duration);
            this.cycles = cycles;
            c = center;
            colors = new float[coloring.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = coloring.get(i).toFloatBits();
            }
        }

        @Override
        protected void end() {
            super.end();
            fx.clear(c.x, c.y, layer);
        }

        @Override
        protected void update(float percent) {
            float f, color;
            int idx, seed = System.identityHashCode(this);
            f = (float) SeededNoise.noise(c.x * 1.5, c.y * 1.5, percent * 0.015, seed) * 0.125f + percent;
            idx = (int) (f * colors.length);
            if (idx >= colors.length - 1) {
                color = SColor.lerpFloatColors(colors[colors.length - 1], NumberTools.setSelectedByte(colors[colors.length - 1], 3, (byte) 0), (Math.min(0.99f, f) * colors.length) % 1f);
            } else {
                color = SColor.lerpFloatColors(colors[idx], colors[idx + 1], (f * colors.length) % 1f);
            }
            fx.put(c.x, c.y, Utilities.twinkles.charAt((int) Math.floor(percent * (Utilities.twinkles.length() * cycles + 1)) % cycles), color, 0f, layer);
        }
    }

    public class LineEffect extends PanelEffect {

        public float[] colors;
        public Coord[] path;

        public LineEffect(float duration, Coord[] path, List<? extends Color> coloring) {
            super(fx, duration);
            this.path = path;

            colors = new float[coloring.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = coloring.get(i).toFloatBits();
            }
        }

        @Override
        protected void end() {
            super.end();
            fx.clear(path[path.length - 1].x, path[path.length - 1].y, layer);
        }

        @Override
        protected void update(float percent) {
            float pathPercent = path.length * percent;
            int pathIndex = Math.min(path.length - 1, Math.round(pathPercent));
            pathPercent %= 1; // get just the fractional part
            Coord c = path[pathIndex];
            String lines;

            if (pathIndex == 0) {
                lines = Utilities.linesFor(Direction.toGoTo(c, path[pathIndex + 1]));
            } else {
                lines = Utilities.linesFor(Direction.toGoTo(path[pathIndex - 1], c));
            }

            float color = colors[Math.min(colors.length - 1, Math.round(pathPercent))];

            // clear rest of line
            for (Coord clearing : path) {
                fx.clear(clearing.x, clearing.y, layer);
            }

            // put new line segment
            fx.put(c.x, c.y, lines.charAt(0), color, 0f, layer);
        }
    }

    public class DustEffect extends PanelEffect {

        public float[] colors;

        public double[][] resMap,
            lightMap;

        public List<Coord> affected;

        public DustEffect(float duration, GreasedRegion valid, Coord center, int distance, Radius radius, List<? extends Color> coloring) {
            super(fx, duration, valid);
            resMap = ArrayTools.fill(1.0, validCells.width, validCells.height);
            validCells.writeDoublesInto(resMap, 0.0);
            lightMap = new double[validCells.width][validCells.height];
            FOV.reuseFOV(resMap, lightMap, center.x, center.y, distance, radius);
            validCells.not().writeDoublesInto(lightMap, 0.0);
            validCells.not();
            affected = new GreasedRegion(lightMap, 0.01, 999.0).getAll();
            colors = new float[coloring.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = coloring.get(i).toFloatBits();
            }
        }

        @Override
        protected void update(float percent) {
            int len = affected.size();
            Coord c;
            float f, color;
            int idx, seed = System.identityHashCode(this), seed2 = seed;
            for (int i = 0; i < len; i++) {
                c = affected.get(i);
                if (lightMap[c.x][c.y] <= 0.0) {// || 0.6 * (lightMap[c.x][c.y] + percent) < 0.25)
                    continue;
                }
                f = (float) SeededNoise.noise(c.x * 1.5, c.y * 1.5, percent * 0.015, seed)
                    * 0.125f + percent;
                if (f < 0f || 0.5 * lightMap[c.x][c.y] + f < 0.4) {
                    continue;
                }
                idx = (int) (f * colors.length);
                if (idx >= colors.length - 1) {
                    color = SColor.lerpFloatColors(colors[colors.length - 1], NumberTools.setSelectedByte(colors[colors.length - 1], 3, (byte) 0), (Math.min(0.99f, f) * colors.length) % 1f);
                } else {
                    color = SColor.lerpFloatColors(colors[idx], colors[idx + 1], (f * colors.length) % 1f);
                }
                fx.put(c.x, c.y, Utilities.randomBraille(++seed2, percent < 0.375 ? (int) (percent * 8) + 1 : (int) (7.625 - percent * 7)), color, 0f, layer);
            }
        }
    }

    public class ConeEffect extends PanelEffect {

        /**
         * The default explosion colors are normal for (non-chemical, non-electrical) fire and smoke, going from orange
         * at the start to yellow, very light yellow, and then back to a different orange before going to smoke and
         * fading out to translucent and then transparent by the end.
         * <br>
         * If you want to change the colors the explosion uses, you can either pass a List of Color (or SColor or other
         * subclasses) to the constructor or change this array directly. The float items assigned to this should be the
         * result of calling {@link Color#toFloatBits()} or possibly the result of mixing multiple existing floats with
         * {@link SColor#lerpFloatColors(float, float, float)}; other floats that can be directly used by libGDX, that
         * is, packed as ABGR floats (usually the docs will mention this), can also be used.
         */
        public float[] colors = {
            SColor.INTERNATIONAL_ORANGE.toFloatBits(),
            SColor.FLORAL_LEAF.toFloatBits(),
            SColor.LEMON.toFloatBits(),
            SColor.LEMON_CHIFFON.toFloatBits(),
            SColor.floatGet(0xFF6600EE), // SColor.SAFETY_ORANGE
            SColor.floatGet(0x595652DD), // SColor.DB_SOOT
            SColor.floatGet(0x59565299)}; // SColor.DB_SOOT
        /**
         * Used internally to determine how the explosion should spread; derived from {@link #validCells}.
         */
        public double[][] resMap,
            /**
             * The internal representation of how affected each cell is by the explosion, based on proximity to center.
             */
            lightMap;

        /**
         * The raw list of Coords that might be affected by the explosion; may include some cells that aren't going to
         * show as exploding (it usually has some false positives), but shouldn't exclude any cells that should show as
         * such (no false negatives). You can edit this if you need to, but it isn't recommended.
         */
        public List<Coord> affected;

        public ConeEffect(float duration, GreasedRegion valid, Coord center, int distance, double angle, Radius radius) {
            super(fx, duration, valid);
            resMap = ArrayTools.fill(1.0, validCells.width, validCells.height);
            validCells.writeDoublesInto(resMap, 0.0);
            lightMap = new double[validCells.width][validCells.height];
            FOV.reuseFOV(resMap, lightMap, center.x, center.y, distance, radius, angle, 75.0);
            validCells.not().writeDoublesInto(lightMap, 0.0);
            validCells.not();
            affected = new GreasedRegion(lightMap, 0.01, 999.0).getAll();
        }

        public ConeEffect(float duration, GreasedRegion valid, Coord center, int distance, double angle, Radius radius, List<? extends Color> coloring) {
            this(duration, valid, center, distance, angle, radius);
            if (colors.length != coloring.size()) {
                colors = new float[coloring.size()];
            }
            for (int i = 0; i < colors.length; i++) {
                colors[i] = coloring.get(i).toFloatBits();
            }
        }

        /**
         * Called each frame.
         *
         * @param percent The percentage of completion for this action, growing from 0 to 1 over the duration. If
         * {@link #setReverse(boolean) reversed}, this will shrink from 1 to 0.
         */
        @Override
        protected void update(float percent) {
            int len = affected.size();
            Coord c;
            float f, color;
            int idx, seed = System.identityHashCode(this);
            for (int i = 0; i < len; i++) {
                c = affected.get(i);
                if (lightMap[c.x][c.y] <= 0.0)// || 0.6 * (lightMap[c.x][c.y] + percent) < 0.25)
                {
                    continue;
                }
                f = (float) SeededNoise.noise(c.x * 1.5, c.y * 1.5, percent * 5, seed)
                    * 0.17f + percent * 1.2f;
                if (f < 0f || 0.5 * lightMap[c.x][c.y] + f < 0.4) {
                    continue;
                }
                idx = (int) (f * colors.length);
                if (idx >= colors.length - 1) {
                    color = SColor.lerpFloatColors(colors[colors.length - 1], NumberTools.setSelectedByte(colors[colors.length - 1], 3, (byte) 0), (Math.min(0.99f, f) * colors.length) % 1f);
                } else {
                    color = SColor.lerpFloatColors(colors[idx], colors[idx + 1], (f * colors.length) % 1f);
                }
                fx.put(c.x, c.y, '\u0000', color, 0f, layer);
            }
        }
    }

    public class ColorSparkleEffect extends PanelEffect {

        public float[][] colors;
        public double[][] resMap,
            lightMap;
        public List<Coord> affected;
        private final char[] dots = "⠁⠂⠄⠈⠐⠠⡀⢀".toCharArray();

        public ColorSparkleEffect(float duration, GreasedRegion valid, Coord center, int distance, Radius radius, RNG rng) {
            this(duration, valid, center, distance, radius, Utilities.randomColors(8, rng));
        }

        public ColorSparkleEffect(float duration, GreasedRegion valid, Coord center, int distance, Radius radius, Color[][] coloring) {
            super(fx, duration, valid);
            resMap = ArrayTools.fill(1.0, validCells.width, validCells.height);
            validCells.writeDoublesInto(resMap, 0.0);
            lightMap = new double[validCells.width][validCells.height];
            FOV.reuseFOV(resMap, lightMap, center.x, center.y, distance, radius);
            validCells.not().writeDoublesInto(lightMap, 0.0);
            validCells.not();
            affected = new GreasedRegion(lightMap, 0.01, 999.0).getAll();
            colors = new float[8][];
            for (int i = 0; i < 8; i++) {
                colors[i] = new float[coloring[i % coloring.length].length];
                for (int j = 0; j < coloring[i % coloring.length].length; j++) {
                    colors[i][j] = coloring[i % coloring.length][j].toFloatBits();
                }
            }
        }

        @Override
        protected void end() {
            super.end();
            for (int i = 1; i < 9; i++) {
                fx.clear(layer + i);
            }
        }

        @Override
        protected void update(float percent) {
            int len = affected.size();
            Coord c;
            float f, color;
            int idx, seed = System.identityHashCode(this), seed2 = seed;
            for (int i = 0; i < len; i++) {
                c = affected.get(i);
                if (lightMap[c.x][c.y] <= 0.0) {// || 0.6 * (lightMap[c.x][c.y] + percent) < 0.25)
                    continue;
                }
                f = (float) SeededNoise.noise(c.x * 1.5, c.y * 1.5, percent * 0.015, seed)
                    * 0.125f + percent;
                if (f < 0f || 0.5 * lightMap[c.x][c.y] + f < 0.4) {
                    continue;
                }
                for (int j = 0; j < 8; j++) {
                    idx = (int) (f * colors[j].length);
                    if (idx >= colors[j].length - 1) {
                        color = SColor.lerpFloatColors(colors[j][colors[j].length - 1], NumberTools.setSelectedByte(colors[j][colors[j].length - 1], 3, (byte) 0), (Math.min(0.99f, f) * colors.length) % 1f);
                    } else {
                        color = SColor.lerpFloatColors(colors[j][idx], colors[j][idx + 1], (f * colors[j].length) % 1f);
                    }
                    fx.put(c.x, c.y, dots[j], color, 0f, layer + j + 1);
                }
            }
        }

    }

}
