package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Harrowing_Journey {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.TARGET_PLAYER,
                    this,
                    "Target player$ draws three cards and loses 3 life.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicDrawAction(player,3));
                    game.doAction(new MagicChangeLifeAction(player,-3));
                }
            });
        }
    };
}
