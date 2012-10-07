package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;

public class MagicRemoveCounterEvent extends MagicEvent {

    public MagicRemoveCounterEvent(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        super(
            permanent,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choices) {
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        counterType,
                        -amount,
                        true
                    ));
                }
            },
            genDescription(permanent,counterType,amount)
        );
    }    
    
    private static String genDescription(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        final StringBuilder description=new StringBuilder("Remove ");
        if (amount==1) {
            description.append("a ").append(counterType.getName()).append(" counter");
        } else {
            description.append(amount).append(' ').append(counterType.getName()).append(" counters");
        }
        description.append(" from ").append(permanent.getName()).append('.');
        return description.toString();
    }
}
