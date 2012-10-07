package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class To_Arms_ {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Untap all creatures you control. Draw a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            final Collection<MagicTarget> targets=
                game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicUntapAction((MagicPermanent)target));
            }
            game.doAction(new MagicDrawAction(player,1));
        }
    };
}
