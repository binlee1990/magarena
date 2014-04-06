package magic.model.target;

import java.util.Map;
import java.util.TreeMap;

import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicAbility;
import magic.model.MagicPlayer;
import magic.model.MagicColor;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.stack.MagicItemOnStack;

import static magic.model.target.MagicTargetFilter.*;

public class MagicTargetFilterFactory {
    
    private static final Map<String, MagicTargetFilter<MagicPermanent>> multiple =
        new TreeMap<String, MagicTargetFilter<MagicPermanent>>(String.CASE_INSENSITIVE_ORDER);
    
    private static final Map<String, MagicTargetFilter<?>> single =
        new TreeMap<String, MagicTargetFilter<?>>(String.CASE_INSENSITIVE_ORDER);

    static {
        // used by lord ability/target <group>
        // <color|type|subtype> creatures you controls
        multiple.put("creatures you control", TARGET_CREATURE_YOU_CONTROL);
        multiple.put("red creatures and white creatures you control", TARGET_RED_OR_WHITE_CREATURE_YOU_CONTROL);
        multiple.put("creatures you control with flying", TARGET_CREATURE_WITH_FLYING_YOU_CONTROL);
        multiple.put("enchanted creatures you control", TARGET_ENCHANTED_CREATURE_YOU_CONTROL);
        multiple.put("non-human creatures you control", TARGET_NONHUMAN_CREATURE_YOU_CONTROL);
        multiple.put("attacking creatures you control", TARGET_ATTACKING_CREATURE_YOU_CONTROL);
        multiple.put("untapped creatures you control", TARGET_UNTAPPED_CREATURE_YOU_CONTROL);

        // <color|type|subtype> creatures your opponents control
       
        // <color|type|subtype> creatures
        multiple.put("creatures", TARGET_CREATURE);
        multiple.put("nonblack creatures", TARGET_NONBLACK_CREATURE);
        multiple.put("nonwhite creatures", TARGET_NONWHITE_CREATURE);
        multiple.put("creatures without flying", TARGET_CREATURE_WITHOUT_FLYING);
        multiple.put("creatures with flying", TARGET_CREATURE_WITH_FLYING);
        multiple.put("all sliver creatures", TARGET_SLIVER_CREATURE);
        multiple.put("all creatures", TARGET_CREATURE);
        multiple.put("attacking creatures", TARGET_ATTACKING_CREATURE);
        multiple.put("blocking creatures", TARGET_BLOCKING_CREATURE);

        // <color|type|subtype> you control
        multiple.put("lands you control", TARGET_LAND_YOU_CONTROL);
        multiple.put("permanents you control", TARGET_PERMANENT_YOU_CONTROL);
        multiple.put("artifacts you control", TARGET_ARTIFACT_YOU_CONTROL);
        multiple.put("enchantments you control", TARGET_ENCHANTMENT_YOU_CONTROL);
        multiple.put("creature tokens you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        multiple.put("faeries you control", TARGET_FAERIE_YOU_CONTROL);
        multiple.put("each wolf you control", TARGET_WOLF_YOU_CONTROL);
        
        // <color|type|subtype> your opponents control
        multiple.put("creatures your opponents control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        multiple.put("creatures with flying your opponents control", TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        
        // <color|type|subtype> 
        multiple.put("lands",TARGET_LAND);
        multiple.put("nonbasic lands", TARGET_NONBASIC_LAND);
        multiple.put("islands", TARGET_ISLAND);
        multiple.put("nonland permanents", TARGET_NONLAND_PERMANENT);
        multiple.put("all slivers", TARGET_SLIVER_PERMANENT);
        multiple.put("all goblins", TARGET_GOBLIN_PERMANENT);
        multiple.put("artifacts", TARGET_ARTIFACT);



        // used by MagicTargetChoice
        // <color|type|subtype> card from your graveyard
        single.put("card from your graveyard", TARGET_CARD_FROM_GRAVEYARD);
        single.put("instant or sorcery card from your graveyard", TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD);
        single.put("artifact or enchantment card from your graveyard", cardOr(MagicTargetType.Graveyard, MagicType.Artifact, MagicType.Enchantment));
        
        // <color|type|subtype> permanent card from your graveyard
        single.put("permanent card from your graveyard", TARGET_PERMANENT_CARD_FROM_GRAVEYARD); 
        single.put("permanent card with converted mana cost 3 or less from your graveyard", TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        
        // <color|type|subtype> creature card from your graveyard
        single.put("creature card with converted mana cost 3 or less from your graveyard", TARGET_CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        single.put("creature card with converted mana cost 2 or less from your graveyard", TARGET_CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD); 
        single.put("creature card with infect from your graveyard", TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD);
        single.put("creature card with scavenge from your graveyard", PAYABLE_CREATURE_CARD_FROM_GRAVEYARD);
        
        // <color|type|subtype> card from an opponent's graveyard
        single.put("instant or sorcery card from an opponent's graveyard", TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD);
        
        // <color|type|subtype> card from your hand
        single.put("card from your hand", TARGET_CARD_FROM_HAND);
        single.put("basic land card from your hand", TARGET_BASIC_LAND_CARD_FROM_HAND);
        
        // <color|type|subtype> permanent card from your hand
        
        // <color|type|subtype> creature card from your hand
        single.put("blue or red creature card from your hand", TARGET_BLUE_OR_RED_CREATURE_CARD_FROM_HAND);
        single.put("multicolored creature card from your hand", TARGET_MULTICOLORED_CREATURE_CARD_FROM_HAND);

        // <color|type|subtype> card from your library
        single.put("basic land card from your library", TARGET_BASIC_LAND_CARD_FROM_LIBRARY);
        single.put("Plains, Island, Swamp, Mountain or Forest card from your library", TARGET_LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("Plains or Island card from your library", cardOr(MagicTargetType.Library, MagicSubType.Plains, MagicSubType.Island));
        single.put("Plains or Swamp card from your library", cardOr(MagicTargetType.Library, MagicSubType.Plains, MagicSubType.Swamp));
        single.put("Island or Swamp card from your library", cardOr(MagicTargetType.Library, MagicSubType.Island, MagicSubType.Swamp));
        single.put("Island or Mountain card from your library", cardOr(MagicTargetType.Library, MagicSubType.Island, MagicSubType.Mountain));
        single.put("Swamp or Mountain card from your library", cardOr(MagicTargetType.Library, MagicSubType.Swamp, MagicSubType.Mountain));
        single.put("Swamp or Forest card from your library", cardOr(MagicTargetType.Library, MagicSubType.Swamp, MagicSubType.Forest));
        single.put("Mountain or Forest card from your library", cardOr(MagicTargetType.Library, MagicSubType.Mountain, MagicSubType.Forest));
        single.put("Mountain or Plains card from your library", cardOr(MagicTargetType.Library, MagicSubType.Mountain, MagicSubType.Plains));
        single.put("Forest or Plains card from your library", cardOr(MagicTargetType.Library, MagicSubType.Forest, MagicSubType.Plains));
        single.put("Forest or Island card from your library", cardOr(MagicTargetType.Library, MagicSubType.Forest, MagicSubType.Island));
        single.put("Plains, Island, Swamp, or Mountain card from your library", TARGET_PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY);
        single.put("land card with a basic land type from your library", TARGET_LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("artifact or enchantment card from your library", cardOr(MagicTargetType.Library, MagicType.Artifact, MagicType.Enchantment));
        single.put("instant or sorcery card from your library", cardOr(MagicTargetType.Library, MagicType.Instant, MagicType.Sorcery));
        single.put("Treefolk or Forest card from your library", cardOr(MagicTargetType.Library, MagicSubType.Treefolk, MagicSubType.Forest));
    
        
        // <color|type|subtype> permanent card from your library
        
        // <color|type|subtype> creature card from your library
        
        // <color|type|subtype> creature you control
        single.put("non-Angel creature you control", TARGET_NON_ANGEL_CREATURE_YOU_CONTROL);
        single.put("blue or black creature you control", TARGET_BLUE_OR_BLACK_CREATURE_YOU_CONTROL);
        single.put("red or green creature you control", TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL);
        single.put("untapped creature you control", TARGET_UNTAPPED_CREATURE_YOU_CONTROL);
        single.put("artifact or creature you control", TARGET_ARTIFACT_OR_CREATURE_YOU_CONTROL);
        single.put("attacking or blocking creature you control", TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL);
        single.put("nonlegendary creature you control", TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL);
        single.put("non-Zombie creature you control", TARGET_NON_ZOMBIE_YOU_CONTROL);
        single.put("non-Vampire creature you control", TARGET_NON_VAMPIRE_YOU_CONTROL);
        single.put("unblocked attacking creature you control", UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL);
        single.put("attacking creature you control", TARGET_ATTACKING_CREATURE_YOU_CONTROL);
        
        // <color|type|subtype> creature an opponent controls
        single.put("creature with flying an opponent controls", TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("red or green creature an opponent controls", TARGET_RED_OR_GREEN_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("creature an opponent controls", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);

        // <color|type|subtype> creature
        single.put("1/1 creature", TARGET_1_1_CREATURE);
        single.put("nonblack creature", TARGET_NONBLACK_CREATURE);
        single.put("nonartifact creature", TARGET_NONARTIFACT_CREATURE);
        single.put("non-Demon creature", TARGET_NON_DEMON_CREATURE);
        single.put("non-Zombie creature", TARGET_NONZOMBIE_CREATURE);
        single.put("non-Human creature", TARGET_NONHUMAN_CREATURE);
        single.put("non-Vampire, non-Werewolf, non-Zombie creature", TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE);
        single.put("noncreature", TARGET_NONCREATURE);
        single.put("nonartifact, nonblack creature", TARGET_NONARTIFACT_NONBLACK_CREATURE);
        single.put("land or nonblack creature", TARGET_LAND_OR_NONBLACK_CREATURE);
        single.put("red or green creature",TARGET_RED_OR_GREEN_CREATURE);
        single.put("tapped creature", TARGET_TAPPED_CREATURE);
        single.put("untapped creature", TARGET_UNTAPPED_CREATURE);
        single.put("artifact or creature", TARGET_ARTIFACT_OR_CREATURE);
        single.put("unpaired Soulbond creature", TARGET_UNPAIRED_SOULBOND_CREATURE);
        single.put("monocolored creature", TARGET_MONOCOLORED_CREATURE);
        single.put("attacking creature", TARGET_ATTACKING_CREATURE);
        single.put("attacking or blocking creature", TARGET_ATTACKING_OR_BLOCKING_CREATURE);
        single.put("blocked creature", TARGET_BLOCKED_CREATURE);
        single.put("blocking creature", TARGET_BLOCKING_CREATURE);
        single.put("green or white creature", TARGET_GREEN_OR_WHITE_CREATURE);
        single.put("white or blue creature", TARGET_WHITE_OR_BLUE_CREATURE);
        single.put("creature with converted mana cost 3 or less", TARGET_CREATURE_CONVERTED_3_OR_LESS);
        single.put("creature with converted mana cost 2 or less", TARGET_CREATURE_CONVERTED_2_OR_LESS);
        single.put("creature with flying", TARGET_CREATURE_WITH_FLYING);
        single.put("creature without flying", TARGET_CREATURE_WITHOUT_FLYING);
        single.put("creature with power 2 or less", TARGET_CREATURE_POWER_2_OR_LESS); 
        single.put("creature with power 4 or greater", TARGET_CREATURE_POWER_4_OR_MORE);
        single.put("creature with shadow", TARGET_CREATURE_WITH_SHADOW);
        single.put("creature with +1/+1 counter", TARGET_CREATURE_PLUSONE_COUNTER);
        single.put("attacking creature with flying", TARGET_ATTACKING_CREATURE_WITH_FLYING);
        single.put("attacking creature without flying", TARGET_ATTACKING_CREATURE_WITHOUT_FLYING);
        single.put("Djinn or Efreet", TARGET_DJINN_OR_EFREET);

        // <color|type|subtype> you control
        single.put("basic land you control", TARGET_BASIC_LAND_YOU_CONTROL);
        single.put("nonbasic land you control", TARGET_NONBASIC_LAND_YOU_CONTROL);
        single.put("Forest or Plains you control", TARGET_FOREST_OR_PLAINS_YOU_CONTROL);
        single.put("creature or enchantment you control", TARGET_CREATURE_OR_ENCHANTMENT_YOU_CONTROL);
        single.put("creature token you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        single.put("permanent you control", TARGET_PERMANENT_YOU_CONTROL);
        single.put("multicolored permanent you control", TARGET_MULTICOLORED_PERMANENT_YOU_CONTROL);
        single.put("blue permanent you control", TARGET_BLUE_PERMANENT_YOU_CONTROL);
        single.put("nonland permanent you control", TARGET_NONLAND_PERMANENT_YOU_CONTROL);
        single.put("nontoken permanent you control", TARGET_NONTOKEN_PERMANENT_YOU_CONTROL);
        
        // <color|type|subtype> an opponent controls
        single.put("artifact or enchantment an opponent controls", TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent an opponent controls", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("permanent an opponent controls", TARGET_PERMANENT_AN_OPPONENT_CONTROLS);
        
        // <color|type|subtype> you don't control
        single.put("spell you don't control", TARGET_SPELL_YOU_DONT_CONTROL);
        single.put("creature without flying you don't control", MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent you don't control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        
        // <color|type|subtype> permanent
        single.put("permanent", TARGET_PERMANENT);
        single.put("noncreature permanent", TARGET_NONCREATURE);
        single.put("spell or permanent", TARGET_SPELL_OR_PERMANENT);
        single.put("nonland permanent", TARGET_NONLAND_PERMANENT);
        single.put("nonland permanent with converted mana cost 3 or less", TARGET_NONLAND_PERMANENT_CMC_LEQ_3);
        single.put("black or red permanent", TARGET_BLACK_RED_PERMANENT);
        
        // <color|type|subtype>
        single.put("permanent you own", TARGET_PERMANENT_YOU_OWN);
        single.put("Insect, Rat, Spider, or Squirrel", TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL);
        single.put("Vampire, Werewolf, or Zombie", TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE);
        single.put("attacking or blocking Spirit",  TARGET_ATTACKING_OR_BLOCKING_SPIRIT);
        single.put("basic land", TARGET_BASIC_LAND);
        single.put("nonbasic land", TARGET_NONBASIC_LAND);
        single.put("artifact or land", TARGET_ARTIFACT_OR_LAND);
        single.put("artifact or enchantment", TARGET_ARTIFACT_OR_ENCHANTMENT);
        single.put("artifact, enchantment, or land", TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND);
        single.put("artifact, creature, or land", TARGET_ARTIFACT_OR_CREATURE_OR_LAND);
        single.put("artifact, creature, or enchantment",TARGET_ARTIFACT_OR_CREATURE_OR_ENCHANTMENT);
        single.put("noncreature artifact", TARGET_NONCREATURE_ARTIFACT);
        single.put("Spirit or enchantment", TARGET_SPIRIT_OR_ENCHANTMENT);
        single.put("creature or enchantment", TARGET_CREATURE_OR_ENCHANTMENT);
        single.put("creature or land", TARGET_CREATURE_OR_LAND);
        single.put("creature or planeswalker", TARGET_CREATURE_OR_PLANESWALKER);
        single.put("creature or player", TARGET_CREATURE_OR_PLAYER); 
        single.put("Sliver creature or player", TARGET_SLIVER_CREATURE_OR_PLAYER); 
       
        // <color|type> spell
        single.put("spell", TARGET_SPELL);
        single.put("spell or ability", TARGET_SPELL_OR_PERMANENT);
        single.put("spell with converted mana cost 1", TARGET_SPELL_WITH_CMC_EQ_1);
        single.put("spell with converted mana cost 2", TARGET_SPELL_WITH_CMC_EQ_2);
        single.put("spell that targets a player", TARGET_SPELL_THAT_TARGETS_PLAYER);
        single.put("spell with {X} in its mana cost", TARGET_SPELL_WITH_X_COST);
        single.put("noncreature spell", TARGET_NONCREATURE_SPELL);
        single.put("artifact or enchantment spell", TARGET_ARTIFACT_OR_ENCHANTMENT_SPELL);
        single.put("red or green spell", TARGET_RED_OR_GREEN_SPELL);
        single.put("nonblue spell", TARGET_NONBLUE_SPELL);
        single.put("instant or sorcery spell", TARGET_INSTANT_OR_SORCERY_SPELL);
        single.put("creature or Aura spell", TARGET_CREATURE_OR_AURA_SPELL);
        single.put("creature or sorcery spell", TARGET_CREATURE_OR_SORCERY_SPELL);
        single.put("Spirit or Arcane spell", TARGET_SPIRIT_OR_ARCANE_SPELL);

        // player
        single.put("opponent", TARGET_OPPONENT);
        single.put("player", TARGET_PLAYER);

        // from a graveyard
        single.put("card from a graveyard", TARGET_CARD_FROM_ALL_GRAVEYARDS);
        single.put("artifact or creature card from a graveyard", TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("creature card from a graveyard", TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("land card from a graveyard", TARGET_LAND_CARD_FROM_ALL_GRAVEYARDS);
    }

    public static MagicTargetFilter<MagicPermanent> multiple(final String arg) {
        if (multiple.containsKey(arg)) {
            return multiple.get(arg);
        } else if (arg.endsWith(" creatures you control")) {
            return matchCreaturePrefix(arg, " creatures you control", Control.You);
        } else if (arg.endsWith(" creatures your opponents control")) {
            return matchCreaturePrefix(arg, " creatures your opponents control", Control.Opp);
        } else if (arg.endsWith(" creatures")) {
            return matchCreaturePrefix(arg, " creatures", Control.Any);
        } else if (arg.endsWith(" you control")) {
            return matchPermanentPrefix(arg, " you control", Control.You);
        } else if (arg.endsWith(" your opponents control")) {
            return matchPermanentPrefix(arg, " your opponents control", Control.Opp);
        } else {
            return matchPermanentPrefix(arg, "", Control.Any);
        } 
    }
    
    public static MagicTargetFilter<?> single(final String arg) {
        final String filter = arg.replaceFirst(" to sacrifice$", " you control");
        if (single.containsKey(filter)) {
            return single.get(filter);
        } else if (filter.endsWith(" permanent card from your graveyard")) {
            return matchPermanentCardPrefix(filter, " permanent card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" creature card from your graveyard")) {
            return matchCreatureCardPrefix(filter, " creature card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" card from your graveyard")) {
            return matchCardPrefix(filter, " card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" permanent card from an opponent's graveyard")) {
            return matchPermanentCardPrefix(filter, " permanent card from an opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" creature card from an opponent's graveyard")) {
            return matchCreatureCardPrefix(filter, " creature card from an opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" card from an opponent's graveyard")) {
            return matchCardPrefix(filter, " card from an opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" permanent card from your hand")) {
            return matchPermanentCardPrefix(filter, " permanent card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" creature card from your hand")) {
            return matchCreatureCardPrefix(filter, " creature card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" card from your hand")) {
            return matchCardPrefix(filter, " card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" permanent card from your library")) {
            return matchPermanentCardPrefix(filter, " permanent card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" creature card from your library")) {
            return matchCreatureCardPrefix(filter, " creature card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" card from your library")) {
            return matchCardPrefix(filter, " card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" creature you control")) {
            return matchCreaturePrefix(filter, " creature you control", Control.You);
        } else if (filter.endsWith(" creature an opponent controls")) {
            return matchCreaturePrefix(filter, " creature an opponent controls", Control.Opp);
        } else if (filter.endsWith(" creature")) {
            return matchCreaturePrefix(filter, " creature", Control.Any);
        } else if (filter.endsWith(" spell")) {
            return matchSpellPrefix(filter, " spell");
        } else if (filter.endsWith(" you control")) {
            return matchPermanentPrefix(filter, " you control", Control.You);
        } else if (filter.endsWith(" an opponent controls")) {
            return matchPermanentPrefix(filter, " an opponent controls", Control.Opp);
        } else if (filter.endsWith(" you don't control")) {
            return matchPermanentPrefix(filter, " you don't control", Control.Opp);
        } else if (filter.endsWith(" permanent")) {
            return matchPermanentPrefix(filter, " permanent", Control.Any);
        } else {
            return matchPermanentPrefix(filter, "", Control.Any);
        }
    }
    
    private static MagicTargetFilter<MagicCard> matchCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return card(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return card(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return card(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicCard> matchPermanentCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return permanentCard(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return permanentCard(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return permanentCard(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicCard> matchCreatureCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return creatureCard(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return creatureCard(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return creatureCard(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }    
    private static MagicTargetFilter<MagicItemOnStack> matchSpellPrefix(final String arg, final String suffix) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return spell(c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return spell(t);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicPermanent> matchPermanentPrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return permanent(c, control);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return permanent(t, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return permanent(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    private static MagicTargetFilter<MagicPermanent> matchCreaturePrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return creature(c, control);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return creature(t, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return creature(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    enum Control {
        Any,
        You,
        Opp
    }
        
    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Control control) {
        return permanentOr(type, type, control);
    }

    public static final MagicPermanentFilterImpl permanentAnd(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasType(type1) || target.hasType(type2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicSubType subType1, final MagicSubType subType2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasSubType(subType1) || target.hasSubType(subType2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasType(type) || target.hasSubType(subType)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasColor(color1) || target.hasColor(color2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanent(final MagicColor color, final Control control) {
        return permanentOr(color, color, control);
    }
    public static final MagicPermanentFilterImpl permanent(final MagicSubType subType, final Control control) {
        return permanentOr(subType, subType, control);
    }
    public static final MagicPermanentFilterImpl creature(final MagicColor color, final Control control) {
        return creatureOr(color, color, control);
    }
    public static final MagicPermanentFilterImpl creature(final MagicType type, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasType(type) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creature(final MagicSubType subtype, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasSubType(subtype) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasColor(color1) || target.hasColor(color2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creature(final MagicAbility ability, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasAbility(ability) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creature(final MagicPermanentState state, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasState(state) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicTargetType aTargetType, final MagicSubType subType) {
        return cardOr(aTargetType, subType, subType);
    }
    public static final MagicCardFilterImpl cardOr(final MagicTargetType aTargetType, final MagicSubType type1, final MagicSubType type2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(type1) || target.hasSubType(type2);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicTargetType aTargetType, final MagicType type) {
        return cardOr(aTargetType, type, type);
    }
    public static final MagicCardFilterImpl cardOr(final MagicTargetType aTargetType, final MagicType type1, final MagicType type2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type1) || target.hasType(type2);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl cardAnd(final MagicTargetType aTargetType, final MagicType type1, final MagicType type2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type1) && target.hasType(type2);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicTargetType aTargetType, final MagicColor color) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasColor(color);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicColor color) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() && itemOnStack.hasColor(color);
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicType type) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type1, final MagicType type2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type1) || itemOnStack.isSpell(type2);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type, final MagicSubType subType) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type) || itemOnStack.isSpell(subType);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicSubType subType1, final MagicSubType subType2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType1) || itemOnStack.isSpell(subType2);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicColor color1, final MagicColor color2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() &&
                       (itemOnStack.hasColor(color1) || itemOnStack.hasColor(color2));
            }
        };
    }
    public static final MagicCardFilterImpl creatureCard(final MagicTargetType aTargetType, final MagicType type) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type) && target.hasType(MagicType.Creature);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl creatureCard(final MagicTargetType aTargetType, final MagicSubType subType) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(subType) && target.hasType(MagicType.Creature);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl creatureCard(final MagicTargetType aTargetType, final MagicColor color) {
        return creatureCardOr(aTargetType, color, color);
    }
    public static final MagicCardFilterImpl creatureCardOr(final MagicTargetType aTargetType, final MagicColor color1, final MagicColor color2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return (target.hasColor(color1) || target.hasColor(color2)) && target.hasType(MagicType.Creature);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl permanentCard(final MagicTargetType aTargetType, final MagicType type) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type) && target.getCardDefinition().isPermanent();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl permanentCard(final MagicTargetType aTargetType, final MagicColor color) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasColor(color) && target.getCardDefinition().isPermanent();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl permanentCard(final MagicTargetType aTargetType, final MagicSubType subType) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(subType) && target.getCardDefinition().isPermanent();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
}
