package io.github.mabid.dialogs;

import io.github.mabid.functions.NewGameFunction;
import io.github.mabid.utils.GameProperties;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGameDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;

	public NewGameDialog(JFrame parent, NewGameFunction func) {
	    super(parent, "New Game", true);

	    setSize(500, 300);
	    setLocationRelativeTo(parent);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    setLayout(new BorderLayout(10, 10));

	    JLabel title = new JLabel("Add a New Game");
	    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

	    JPanel top = new JPanel(new BorderLayout());
	    top.add(title, BorderLayout.NORTH);
	    top.add(new JLabel("Enter the Name of the Game"), BorderLayout.SOUTH);
	    add(top, BorderLayout.NORTH);

	    JPanel center = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.anchor = GridBagConstraints.WEST;

	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    center.add(new JLabel("Game name:"), gbc);

	    gbc.gridx = 1;
	    gbc.gridwidth = 2;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 1.0;

	    JTextField gameName = new JTextField(20);
	    center.add(gameName, gbc);

	    gbc.gridy++;
	    gbc.gridx = 0;
	    gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.weightx = 0;

	    center.add(new JLabel("Location:"), gbc);

	    gbc.gridx = 1;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 1.0;

	    JTextField locationField = new JTextField();
	    locationField.setText("C:\\Games\\SomeGame\\");
	    center.add(locationField, gbc);

	    gbc.gridx = 2;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.weightx = 0;

	    JButton browseButton = new JButton("Browse...");
	    center.add(browseButton, gbc);

	    gbc.gridy++;
	    gbc.gridx = 0;
	    gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.weightx = 0;
	    center.add(new JLabel("Launch commands:"), gbc);

	    gbc.gridx = 1;
	    gbc.gridwidth = 2; 
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 1.0;

	    JTextField launchCommandField = new JTextField();
	    launchCommandField.setText("game.exe"); 
	    center.add(launchCommandField, gbc);
	    
	    add(center, BorderLayout.CENTER);
	    
	    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton create = new JButton("Create");
	    JButton cancel = new JButton("Cancel");

	    create.addActionListener(e -> {
	        String name = gameName.getText().trim();
	        String location = locationField.getText().trim();
	        String launchCommands = launchCommandField.getText().trim();

	        if (name.isEmpty()) {
	            JOptionPane.showMessageDialog(
	                this,
	                "Game name is required",
	                "Validation Error",
	                JOptionPane.WARNING_MESSAGE
	            );
	            return;
	        }

	        GameProperties.saveConfig(name, location, launchCommands);
	        func.reloadList();
	        dispose();
	    });
	    
	    cancel.addActionListener(e -> dispose());

	    buttons.add(create);
	    buttons.add(cancel);
	    add(buttons, BorderLayout.SOUTH);

	    browseButton.addActionListener(e -> {
	        JFileChooser chooser = new JFileChooser();
	        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            locationField.setText(
	                chooser.getSelectedFile().getAbsolutePath()
	            );
	        }
	    });
	}
	
}