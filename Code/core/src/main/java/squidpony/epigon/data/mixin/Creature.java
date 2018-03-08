package squidpony.epigon.data.mixin;

import squidpony.epigon.data.generic.Ability;
import squidpony.epigon.data.generic.Skill;
import squidpony.epigon.data.specific.Physical;
import squidpony.epigon.data.specific.Recipe;
import squidpony.epigon.data.specific.Weapon;
import squidpony.epigon.universe.*;
import squidpony.squidmath.EnumOrderedMap;
import squidpony.squidmath.OrderedMap;
import squidpony.squidmath.ProbabilityTable;

import java.util.HashSet;
import java.util.Set;

/**
 * A specific creature in the world.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class Creature {

    public Creature parent;
    public OrderedMap<Skill, Rating> skills = new OrderedMap<>();
    public OrderedMap<Skill, Rating> skillProgression = new OrderedMap<>();
    public Set<Ability> abilities = new HashSet<>();

    public Set<Recipe> knownRecipes = new HashSet<>();
    public OrderedMap<Profession, Rating> professions = new OrderedMap<>();

    // TODO - add validity list for slots on a per-creature type (Humanoid, Quadruped) basis
    // can be done by switching these to OrderedMap of ImmutableKey values, then unifying slots.
    // validation could be done by calling OrderedMap.keySet().retainAll(validSlots) , I think
    public EnumOrderedMap<JewelrySlot, Physical> jewelry = new EnumOrderedMap<>(JewelrySlot.class);
    public EnumOrderedMap<ClothingSlot, Physical> clothing = new EnumOrderedMap<>(ClothingSlot.class);
    public EnumOrderedMap<ClothingSlot, Physical> armor = new EnumOrderedMap<>(ClothingSlot.class);
    public EnumOrderedMap<OverArmorSlot, Physical> overArmor = new EnumOrderedMap<>(OverArmorSlot.class);
    public EnumOrderedMap<WieldSlot, Physical> wielded = new EnumOrderedMap<>(WieldSlot.class);

    public ProbabilityTable<Weapon> weaponChoices;

    // Runtime values
    public boolean aware;//has noticed the player

    /*
     * Properties Creatures Need to model real life
     * 
     * taxonomy
     * habitation areas (with frequency / likelihood)
     * behavior
     * size of territory
     * breeding method
     * gestation time
     * number of children
     * years to maturity
     * lifespan
     * food type (and specific animals / plants preferred)
     * needed food intake per day
     * quality as livestock (dairy, egg-laying, meat, wool/fur)
     */
}
