package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Wrath_of_God {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Destroy all creatures. They can't be regenerated.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);            
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeStateAction((MagicPermanent)target,MagicPermanentState.CannotBeRegenerated,true));
            }
            game.doAction(new MagicDestroyAction(targets));
        }
    };
}
