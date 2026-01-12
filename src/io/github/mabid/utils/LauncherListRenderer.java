package io.github.mabid.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LauncherListRenderer extends JPanel implements ListCellRenderer<GameItem> {
    
	private static final long serialVersionUID = 1L;
	
	private JLabel label = new JLabel();
    
    public LauncherListRenderer() {
        setLayout(new BorderLayout());
        setOpaque(true);
        
        setPreferredSize(new Dimension(180, 26)); 
        
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        label.setFont(UIManager.getFont("Label.font").deriveFont(13f)); 

        add(label, BorderLayout.CENTER);
        
        setBorder(new EmptyBorder(1, 1, 1, 1));
    }

    public Component getListCellRendererComponent(JList<? extends GameItem> list, GameItem value, int index, boolean isSelected, boolean cellHasFocus) {
        label.setText(value.getName());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }

        if (cellHasFocus) {
            setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
        } else {
            setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        return this;
    }
    
}