package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.ui.IMagActionBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MagActionBar extends TexturedPanel {

    private IMagActionBar actionProvider;

    public MagActionBar(final IMagActionBar provider0) {
        actionProvider = provider0;
        setMinimumSize(new Dimension(getPreferredSize().width, 50));
        setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
        setMagActionBarLayout();
    }

    /**
     *
     */
    private void setMagActionBarLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0, gap 10, flowx, aligny 50%", "[][center,grow][]"));
        addLeftAction();
        addMiddleActions();
        addRightAction();

    }

    private void addRightAction() {
        MenuButton action = actionProvider.getRightAction();
        if (action != null) {
            action.setEnabled(action.isRunnable());
            add(action);
        }
    }

    private void addMiddleActions() {
        final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, flowx"));
        panel.setOpaque(false);
        final List<MenuButton> buttons = actionProvider.getMiddleActions();
        if (buttons != null) {
            boolean isFirstButton = true;
            for (MenuButton btn : buttons) {
                panel.add(btn);
                btn.setEnabled(btn.isRunnable());
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, isFirstButton ? 1 : 0, 0, 1, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                btn.setBorderPainted(true);
                isFirstButton = false;
            }
        }
        add(panel);
    }

    private void addLeftAction() {
        MenuButton action = actionProvider.getLeftAction();
        if (action != null) {
            action.setEnabled(action.isRunnable());
            add(action);
        } else {
            JLabel lbl = new JLabel();
            lbl.setMinimumSize(new Dimension(120, 50));
            add(lbl);
        }
    }

}
