package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicPreventDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPreventTargetPicker;
import magic.model.target.MagicTarget;

public class Withstand {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,
                    MagicPreventTargetPicker.getInstance(),
                    this,
                    "Prevent the next 3 damage that would be dealt to target creature or player$ this turn. Draw a card.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    game.doAction(new MagicPreventDamageAction(target,3));
                    game.doAction(new MagicDrawAction(event.getPlayer(),1));
                }
            });
        }
    };
}
