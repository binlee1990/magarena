package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Visions_of_Beyond {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Draw a card. If a graveyard has twenty or more cards in it, " +
                    "draw three cards instead.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amount = (game.getPlayer(0).getGraveyard().size() >= 20 ||
                                game.getPlayer(1).getGraveyard().size() >= 20) ? 3 : 1;
            game.doAction(new MagicDrawAction(event.getPlayer(),amount));
        }
    };
}
