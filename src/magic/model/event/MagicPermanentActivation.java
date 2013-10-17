package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyable;
import magic.model.MagicCopyMap;
import magic.model.MagicCounterType;
import magic.model.MagicLocationType;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicTargetAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicUntapAction;
import magic.model.action.MagicPreventDamageAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.target.MagicPreventTargetPicker;
import magic.model.target.MagicTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public abstract class MagicPermanentActivation extends MagicActivation<MagicPermanent> implements MagicChangeCardDefinition, MagicCopyable {

    public MagicPermanentActivation(final MagicActivationHints hints, final String txt) {
        super(MagicActivation.NO_COND,hints,txt);
    }

    public MagicPermanentActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        super(conditions,hints,txt);
    }

    @Override
    public final boolean usesStack() {
        return true;
    }

    @Override
    public final MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            EVENT_ACTION,
            "Play activated ability of SN."
        );
    }

    @Override
    public MagicCopyable copy(final MagicCopyMap copyMap) {
        return this;
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public final void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentActivation permanentActivation = event.getRefPermanentActivation();
            final MagicPermanent permanent = event.getPermanent();
            final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                permanentActivation,
                permanent,
                game.getPayedCost()
            );
            game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
        }
    };

    @Override
    public final MagicChoice getChoice(final MagicPermanent source) {
        return getPermanentEvent(source,MagicPayedCost.NO_COST).getChoice();
    }

    public abstract Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source);

    public abstract MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost);

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addAct(this);
    }
    
    private static final String COLON = "\\s*:\\s*";
    private static final String COMMA = "\\s*,\\s*";

    public static final MagicPermanentActivation create(final String act) {
        final String[] token = act.split(COLON);
        final String[] costs = token[0].split(COMMA);
        final String text = token[1];

        final String effect = text.toLowerCase();
        final MagicRuleEventAction ruleAction = MagicRuleEventAction.build(effect);
        final MagicEventAction action  = ruleAction.getAction(effect);
        final MagicTargetPicker<?> picker = ruleAction.getPicker(effect);
        final MagicChoice choice = ruleAction.getChoice(effect);

        return new MagicPermanentActivation(
            new MagicActivationHints(ruleAction.timing),
            ruleAction.description
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
                List<MagicEvent> events = new LinkedList<MagicEvent>();
                for (String cost : costs) {
                    if (cost.equals("{S}")) {
                        events.add(new MagicSacrificeEvent(source));
                    } else if (cost.equals("Sacrifice an artifact")) {
                        events.add(new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_ARTIFACT));
                    } else if (cost.equals("Sacrifice a creature")) {
                        events.add(new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_CREATURE));
                    } else if (cost.equals("{SG}")) {
                        events.add(new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_GOBLIN));
                    } else if (cost.equals("{SS}")) {
                        events.add(new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_SAPROLING));
                    } else if (cost.equals("{SB}")) {
                        events.add(new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_BEAST));
                    } else if (cost.equals("{SL}")) {
                        events.add(new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_LAND));
                    } else if (cost.equals("{E}")) {
                        events.add(new MagicExileEvent(source));
                    } else if (cost.equals("{D}")) {
                        events.add(new MagicDiscardEvent(source));
                    } else if (cost.equals("{T}")) {
                        events.add(new MagicTapEvent(source));
                    } else if (cost.equals("{Q}")) {
                        events.add(new MagicUntapEvent(source));
                    } else if (cost.equals("{L1}")) {
                        events.add(new MagicPayLifeEvent(source, 1));
                    } else if (cost.equals("{L2}")) {
                        events.add(new MagicPayLifeEvent(source, 2));
                    } else if (cost.equals("{L7}")) {
                        events.add(new MagicPayLifeEvent(source, 7));
                    } else if (cost.equals("{+1/+1}")) {
                        events.add(new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1));
                    } else if (cost.equals("{C}")) {
                        events.add(new MagicRemoveCounterEvent(source,MagicCounterType.Charge,1));
                    } else if (cost.equals("{C3}")) {
                        events.add(new MagicRemoveCounterEvent(source,MagicCounterType.Charge,3));
                    } else {
                        events.add(new MagicPayManaCostEvent(source, MagicManaCost.create(cost)));
                    }
                }
                return events;
            }
       
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
                return new MagicEvent(
                    source,
                    choice,
                    picker,
                    action,
                    text + "$"
                );
            }
        };
    }

    public static final MagicPermanentActivation TapAddCharge = new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Charge"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return Arrays.asList(new MagicTapEvent(source));
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a charge counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Charge,
                1,
                true
            ));
        }
    };

    public static final MagicPermanentActivation Untap(final MagicManaCost cost) {
        return new MagicPermanentActivation(
            new MagicActivationHints(MagicTiming.Tapping),
            "Untap"
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
                return Arrays.asList(
                    new MagicPayManaCostEvent(source,cost),
                    new MagicUntapConditionsEvent(source,this)
                );
            }
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    source,
                    this,
                    "Untap SN."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicUntapAction(event.getPermanent()));
            }
        };
    }

    public static final MagicPermanentActivation PreventDamage(final int n) {
        return new MagicPermanentActivation(
            new MagicActivationHints(MagicTiming.Pump),
            "Prevent " + n
        ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return Arrays.asList(new MagicTapEvent(source));
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,
                MagicPreventTargetPicker.getInstance(),
                this,
                "Prevent the next " + n + " damage that would be dealt to target creature or player$ this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    game.doAction(new MagicPreventDamageAction(target,n));
                }
            });
        }
    };}

    public static final MagicPermanentActivation ReturnToOwnersHand(final MagicManaCost cost) {
        return new MagicPermanentActivation(
            new MagicActivationHints(MagicTiming.Removal),
            "Return"
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
                return Arrays.asList(new MagicPayManaCostEvent(source,cost));
            }
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    source,
                    this,
                    "Return SN to its owner's hand."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersHand));
            }
        };
    }

    public static final MagicPermanentActivation SwitchPT(final MagicManaCost cost) {
        return new MagicPermanentActivation(
            new MagicActivationHints(MagicTiming.Pump),
            "Switch"
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
                return Arrays.asList(new MagicPayManaCostEvent(source,cost));
            }
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    source,
                    this,
                    "Switch SN's power and toughness until end of turn."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicAddStaticAction(event.getPermanent(), MagicStatic.SwitchPT));
            }
        };
    }
}
