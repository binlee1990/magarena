package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicType;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Chain_Reaction {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "SN deals X damage to each creature, where X is the number of creatures on the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amount=game.getNrOfPermanents(MagicType.Creature);
            final Collection<MagicTarget> targets=
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget target : targets) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,amount,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
}
