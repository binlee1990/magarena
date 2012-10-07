package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.stack.MagicItemOnStack;

public class MagicCounterUnlessEvent extends MagicEvent {

    public MagicCounterUnlessEvent(final MagicSource source,final MagicItemOnStack itemOnStack,final MagicManaCost cost) {
        super(
            source,
            itemOnStack.getController(),
            new MagicMayChoice(
                "You may pay "+cost.getText()+'.',
                new MagicPayManaCostChoice(cost)
            ),
            itemOnStack,
            EVENT_ACTION,
            "You may$ pay "+cost.getText()+"$. " + 
            "If you don't, counter "+itemOnStack.getName()+"."
        );
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicItemOnStack itemOnStack = event.getRefItemOnStack();
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.payManaCost(game,itemOnStack.getController(),choiceResults);
            } else {
                game.doAction(new MagicCounterItemOnStackAction(itemOnStack));
            }
        }
    };
}
