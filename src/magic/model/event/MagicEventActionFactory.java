package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.stack.MagicCardOnStack;

import java.util.HashMap;
import java.util.Map;

public class MagicEventActionFactory {
    private static final Map<String, MagicEventAction> factory =
        new HashMap<String, MagicEventAction>();
    
    private static final Map<String, String> hint =
        new HashMap<String, String>();
    
    private static void register(final String key, final MagicEventAction action) {
        factory.put(key, action);
    }
    
    private static void hint(final String key, final String value) {
        hint.put(key, value);
    }
    
    static {
        hint("destroy", "neg ");
        register("destroy", new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] choiceResults) {
                event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicDestroyAction(creature));
                    }
                });
            }
        });
        
        hint("counter", "neg ");
        register("counter", new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] choiceResults) {
                event.processTargetCardOnStack(game,choiceResults,0,new MagicCardOnStackAction() {
                    public void doAction(final MagicCardOnStack targetSpell) {
                        game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                    }
                });
            }
        });
        
        hint("exile", "neg ");
        register("exile", new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] choiceResults) {
                event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
                    }
                });
            }
        });
    }

    public static String hint(final String action) {
        if (hint.containsKey(action)) {
            return hint.get(action);
        } else {
            throw new RuntimeException("unknown action " + action);
        }
    }

    public static MagicEventAction build(final String action) {
        if (factory.containsKey(action)) {
            return factory.get(action);
        } else {
            throw new RuntimeException("unknown action " + action);
        }
    }
}
