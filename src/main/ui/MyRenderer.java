package ui;

import javax.swing.*;
import java.awt.*;

// render the day panel
public class MyRenderer extends JPanel implements ListCellRenderer {
    private String eventName;
    private Color backGround;
    private Color foreGround;

    // EFFECTS: get component
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        this.eventName = value.toString();
        this.backGround = new Color(75, 56, 72, 116);
        this.foreGround = new Color(255, 255, 255);

        return this;
    }

    // EFFECTS: get preferred size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 40);
    }

    // EFFECTS: print the day panel
    @Override
    public void paint(Graphics g) {
        g.setColor(backGround);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(foreGround);
        g.drawRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("SF Pro", Font.PLAIN, 10));

        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(this.eventName);
        int x = (getWidth() - stringWidth) / 2;
        int y = getHeight() / 2 + fm.getAscent() / 2;
        g.drawString(this.eventName, x, y);
    }
}
