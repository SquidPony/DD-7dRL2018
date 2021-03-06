package squidpony.epigon.playground;

import squidpony.epigon.GauntRNG;
import squidpony.epigon.ImmutableKey;
import squidpony.epigon.Utilities;
import squidpony.epigon.data.blueprint.*;
import squidpony.epigon.data.generic.Formula;
import squidpony.epigon.data.specific.Physical;
import squidpony.epigon.universe.LiveValue;
import squidpony.epigon.universe.Rating;
import squidpony.epigon.universe.Stat;
import squidpony.squidmath.OrderedSet;

/**
 * A class for doing various tests and utilities work.
 */
public class Kickball {

    public static void main(String... args) {
        new Kickball().go();
    }

    private void go() {
        testImmutableKeys();
//        testFormulas();
//        testJSON();
//        printStones();
    }

    private void testImmutableKeys() {
        OrderedSet<Material> materials = new OrderedSet<>(ImmutableKey.ImmutableKeyHasher.instance);
        materials.addAll(Cloth.values());
        materials.addAll(Hide.values());
        materials.addAll(Inclusion.values());
        materials.addAll(Metal.values());
        materials.addAll(Paper.values());
        materials.addAll(Stone.values());
        materials.addAll(Wood.values());
        for (Material m : materials) {
            System.out.println(Utilities.capitalizeFirst(m.toString()) + "? I can pay $" + m.getValue() + ", not a nan more.");
        }
    }

    private void testFormulas() {
        HandBuilt handBuilt = new HandBuilt();
        Physical source = handBuilt.mixer.buildPhysical(handBuilt.playerBlueprint);
        Physical target = handBuilt.mixer.buildPhysical(Inclusion.ANDALUSITE);
        source.stats.put(Stat.AIM, new LiveValue(52.5));
        target.stats.put(Stat.DODGE, new LiveValue(52));
        source.stats.put(Stat.IMPACT, new LiveValue(64));
        target.stats.put(Stat.TOUGHNESS, new LiveValue(57));

        testBaseHitChance(source, target);
        testBaseDamageDealt(source, target);

        int[] tests = new int[]{2, 4, 30};
        for (int n : tests) {
            System.out.println("Primary for " + n + " is " + Formula.healthForLevel(n, Rating.GOOD));
        }
    }

    private void testBaseHitChance(Physical source, Physical target) {
        System.out.println("Hit chance of " + source.stats.get(Stat.AIM).actual()
                + " vs " + target.stats.get(Stat.DODGE).actual()
                + " is " + Formula.baseHitChance(source, target));

    }

    private void testBaseDamageDealt(Physical source, Physical target) {
        System.out.println("Damage dealt of " + source.stats.get(Stat.IMPACT).actual()
                + " vs " + target.stats.get(Stat.TOUGHNESS).actual()
                + " is " + Formula.baseDamageDealt(source, target));
    }

    private Physical makePhysicalFromStone(Stone stone) {
        Physical pb = new Physical();
        pb.name = stone.toString();
        pb.color = GauntRNG.toRandomizedFloat(stone.front, pb.chaos += 3, 0.05f, 0f, 0.15f);

        return pb;
    }

}