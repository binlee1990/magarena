package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Earthquake {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "SN deals "+amount+" damage to each creature without flying and each player.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicSource source=event.getSource();
            final int amount=event.getCardOnStack().getX();
            final Collection<MagicTarget> targets=
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
            for (final MagicTarget target : targets) {
                final MagicDamage damage=new MagicDamage(source,target,amount,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicDamage damage=new MagicDamage(source,player,amount,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
}
