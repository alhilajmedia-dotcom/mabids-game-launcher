package io.github.mabid;

import io.github.mabid.dialogs.NewGameDialog;
import io.github.mabid.utils.GameItem;
import io.github.mabid.utils.GameProperties;
import io.github.mabid.utils.LauncherListRenderer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class Main {
	
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	
	private final String TITLE = "MGL 1.0.0a";
	
	private JFrame frame;
	
	private JList<GameItem> list;
	
	private CardLayout cardLayout;
	private JPanel mainContent;

	private JPanel emptyCard;
	private JPanel detailsCard;

	private JLabel titleLabel;
	private JLabel statusLabel;

	
	private Main() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		frame = new JFrame();
		frame.setPreferredSize(size);
	}
	
	private void start() {
		frame.setLayout(new BorderLayout());
		
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
		
		DefaultListModel<GameItem> model = new DefaultListModel<>();
		List<GameProperties.GameConfig> configs = GameProperties.listSavedGameConfigs();
		for (GameProperties.GameConfig cfg : configs) {
		    model.addElement(new GameItem(cfg));
		}

	    list = new JList<>(model);
		
		JMenuItem newAdd = new JMenuItem("New");
		newAdd.setAccelerator(KeyStroke.getKeyStroke("alt shift N"));
		newAdd.addActionListener(e -> {
			new NewGameDialog(frame, this::reloadList).setVisible(true);
		});
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> {
			new NewGameDialog(frame, this::reloadList).setVisible(true);
		});
		
		file.add(newAdd);
		file.add(exit);
		menu.add(file);
		frame.setJMenuBar(menu);
	    
	    JPopupMenu popupMenu = new JPopupMenu();

	    JMenuItem launchItem = new JMenuItem("Launch", 
	    		new ImageIcon(new ImageIcon(Main.class.getResource("/icons/launch.png")).getImage()
	            .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	    
	    JMenuItem settingsItem = new JMenuItem("Settings", 
	    		new ImageIcon(new ImageIcon(Main.class.getResource("/icons/cogwheel.png")).getImage()
	    	            .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	    
	    JMenuItem deleteItem = new JMenuItem("Delete", 
	    		new ImageIcon(new ImageIcon(Main.class.getResource("/icons/delete.png")).getImage()
	            .getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
	    
	    launchItem.addActionListener(e -> {
	    	launchGame();
	    });
	    
	    settingsItem.addActionListener(e -> {
	    	openGameSettings();
	    });
	    
	    deleteItem.addActionListener(e -> {
	    	deleteGame();
	    });
	    
	    popupMenu.add(launchItem);
	    popupMenu.add(settingsItem);
	    popupMenu.add(deleteItem);
	    
	    list.addMouseListener(new MouseAdapter() {
	        private void showPopup(MouseEvent e) {
	            if (!e.isPopupTrigger()) return;

	            int index = list.locationToIndex(e.getPoint());
	            if (index == -1) return;

	            list.setSelectedIndex(index);

	            GameItem selected = list.getSelectedValue();
	            if (selected == null) return;

	            popupMenu.show(list, e.getX(), e.getY());
	        }

	        public void mousePressed(MouseEvent e) {
	            showPopup(e);
	        }

	        public void mouseReleased(MouseEvent e) {
	            showPopup(e);
	        }
	    });
	    
	    list.setCellRenderer(new LauncherListRenderer());

	    JScrollPane scrollPane = new JScrollPane(list);
	    
	    scrollPane.setPreferredSize(new Dimension(180, HEIGHT));
	    
	    scrollPane.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, 
	            UIManager.getColor("ControlShadow"))); 
	    
	    scrollPane.getViewport().setOpaque(false);
	    scrollPane.setOpaque(false);

	    frame.add(scrollPane, BorderLayout.WEST);

	    cardLayout = new CardLayout();
	    mainContent = new JPanel(cardLayout);
	    
	    emptyCard = new JPanel();
	    emptyCard.setLayout(new BorderLayout());

	    JLabel hint = new JLabel("Select a game...", JLabel.CENTER);
	    hint.setFont(new Font("SansSerif", Font.PLAIN, 24));

	    emptyCard.add(hint, BorderLayout.CENTER);
	    
	    detailsCard = new JPanel();
	    
	    mainContent.add(emptyCard, "EMPTY");
	    mainContent.add(detailsCard, "DETAILS");
	    
	    detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));

	    titleLabel = new JLabel("", JLabel.CENTER);
	    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
	    titleLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

	    JButton play = new JButton("Play");
	    JButton settings = new JButton("Settings");
	    JButton delete = new JButton("Delete");
	    
	    play.addActionListener(e -> {
	    	launchGame();
	    });
	    
	    settings.addActionListener(e -> {
	    	openGameSettings();
	    });
	    
	    delete.addActionListener(e -> {
	    	deleteGame();
	    });
	    
	    JPanel actionBar = new JPanel();
	    actionBar.add(play);
	    actionBar.add(settings);
	    actionBar.add(delete);

	    statusLabel = new JLabel("No info available");
	    statusLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

	    detailsCard.add(Box.createVerticalGlue());
	    detailsCard.add(titleLabel);
	    detailsCard.add(Box.createRigidArea(new Dimension(0, 20)));
	    detailsCard.add(actionBar);
	    detailsCard.add(Box.createRigidArea(new Dimension(0, 10)));
	    detailsCard.add(statusLabel);
	    detailsCard.add(Box.createVerticalGlue());
	    
	    list.addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) {
	            GameItem item = list.getSelectedValue();
	            if (item == null) {
	            	cardLayout.show(mainContent, "EMPTY");
	                return;
	            }
	            
	            titleLabel.setText(item.getName());
	            statusLabel.setText("No info available");

	            cardLayout.show(mainContent, "DETAILS");
	        }
	    });
        	
	    frame.add(mainContent, BorderLayout.CENTER);
	}
	
	private void reloadList() {
		DefaultListModel<GameItem> newModel = new DefaultListModel<>();
		List<GameProperties.GameConfig> newConfigs = GameProperties.listSavedGameConfigs();
		for (GameProperties.GameConfig cfg : newConfigs) {
			newModel.addElement(new GameItem(cfg));
		}

		list.setModel(newModel);
	}
	
	private void launchGame() {
		GameItem item = list.getSelectedValue();
        if (item == null) return;
        
        GameProperties.GameConfig config = item.getConfig();

        String os = System.getProperty("os.name").toLowerCase();

        try {
            ProcessBuilder pb;

            if (os.contains("win")) {
                pb = new ProcessBuilder(
                    "cmd.exe",
                    "/c",
                    "cd /d \"" + config.location + "\" && " + config.launchCommands
                );
            } else {
                pb = new ProcessBuilder(
                    "bash",
                    "-c",
                    "cd \"" + config.location + "\" && " + config.launchCommands
                );
            }

            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void openGameSettings() {
		GameItem item = list.getSelectedValue();
	    if (item == null) return;

	    GameProperties.GameConfig config = item.getConfig();

	    JDialog dialog = new JDialog(frame, "Game Settings", true);
	    dialog.setLayout(new BorderLayout());
	    dialog.setSize(450, 300);
	    dialog.setLocationRelativeTo(frame);

	    JPanel form = new JPanel();
	    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
	    form.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    JTextField nameField = new JTextField(config.name);
	    JTextField locationField = new JTextField(config.location);
	    JTextArea launchArea = new JTextArea(config.launchCommands, 4, 20);
	    launchArea.setLineWrap(true);
	    launchArea.setWrapStyleWord(true);

	    form.add(labeled("Name", nameField));
	    form.add(Box.createVerticalStrut(8));
	    form.add(labeled("Location", locationField));
	    form.add(Box.createVerticalStrut(8));
	    form.add(labeled("Launch Commands", new JScrollPane(launchArea)));

	    JButton save = new JButton("Save");
	    JButton cancel = new JButton("Cancel");

	    save.addActionListener(e -> {
	        config.name = nameField.getText().trim();
	        config.location = locationField.getText().trim();
	        config.launchCommands = launchArea.getText().trim();

	        GameProperties.saveConfig(config.name, config.location, config.launchCommands);
	        reloadList();
	        dialog.dispose();
	    });

	    cancel.addActionListener(e -> dialog.dispose());

	    JPanel actions = new JPanel();
	    actions.add(save);
	    actions.add(cancel);

	    dialog.add(form, BorderLayout.CENTER);
	    dialog.add(actions, BorderLayout.SOUTH);
	    dialog.setVisible(true);
	}
	
	private void deleteGame() {
        if (list.getSelectedIndex() == -1) return;
        int yesornah = JOptionPane.showConfirmDialog(null, "Do you want to delete " +
        		list.getSelectedValue().getName() + "?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (yesornah == JOptionPane.YES_OPTION) {
        	File file = new File("saved_games/" + list.getSelectedValue().getName() + ".gc");

            if (file.delete()) {
                JOptionPane.showMessageDialog(null, "Successfully deleted file", "Noice", 
                		JOptionPane.INFORMATION_MESSAGE);
            } else {
            	JOptionPane.showMessageDialog(null, "Failed deleted file", "Error", 
                		JOptionPane.ERROR_MESSAGE);
            }
            
        	reloadList();
        }
	}
	
	private JPanel labeled(String label, java.awt.Component field) {
	    JPanel panel = new JPanel(new BorderLayout(5, 5));
	    panel.add(new JLabel(label), BorderLayout.NORTH);
	    panel.add(field, BorderLayout.CENTER);
	    return panel;
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Main main = new Main();
		main.frame.setResizable(false);
		main.frame.setTitle(main.TITLE);
		main.frame.pack();
		main.frame.setLocationRelativeTo(null);
		main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		main.start();

		main.frame.setVisible(true);
	}

}
