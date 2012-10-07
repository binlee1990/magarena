package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicLandfallPumpTrigger extends MagicLandfallTrigger {
    private final int power;
    private final int toughness;
    
    public MagicLandfallPumpTrigger(final int power, final int toughness) {
        this.power = power;
        this.toughness = toughness;
    }

    @Override
    protected MagicEvent getEvent(final MagicPermanent permanent) {
        return new MagicEvent(
            permanent,
            this,
            "SN gets " + 
            getString(power) + "/" + getString(toughness) + 
            " until end of turn."
        );
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        game.doAction(new MagicChangeTurnPTAction(
            event.getPermanent(),
            power,
            toughness
        ));
    }
    
    private String getString(final int pt) {
        return pt >= 0 ? "+" + pt : Integer.toString(pt);
    }
}
