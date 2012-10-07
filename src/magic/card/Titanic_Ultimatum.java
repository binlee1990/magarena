package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Titanic_Ultimatum {
    private static final long TITANIC_ULTIMATUM_FLAGS=
        MagicAbility.FirstStrike.getMask()|
        MagicAbility.LifeLink.getMask()|
        MagicAbility.Trample.getMask();

    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Until end of turn, creatures you control get +5/+5 and gain first strike, lifelink and trample.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets=game.filterTargets(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature=(MagicPermanent)target;
                game.doAction(new MagicChangeTurnPTAction(creature,5,5));
                game.doAction(new MagicSetAbilityAction(creature,TITANIC_ULTIMATUM_FLAGS));
            }
        }
    };
}
