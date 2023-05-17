import Vue.MenuGraphique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Exemple");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(300, 300));

		MenuGraphique menu = new MenuGraphique();

		menu.setSize(layeredPane.getSize());

		JTextField textField = new JTextField("");
		textField.setBounds(layeredPane.getSize().width /2, layeredPane.getSize().height /2, 100, 100); // Définir les coordonnées et les dimensions du deuxième bouton

//		layeredPane.add(button1, JLayeredPane.DEFAULT_LAYER); // Ajouter le premier bouton avec le calque par défaut
		layeredPane.add(menu, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(textField, JLayeredPane.PALETTE_LAYER); // Ajouter le deuxième bouton avec un calque supérieur

		layeredPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				menu.setSize(layeredPane.getSize());
				textField.setBounds(layeredPane.getSize().width /2, layeredPane.getSize().height /2, 100, 100); // Définir les coordonnées et les dimensions du deuxième bouton
			}
		});

		frame.add(layeredPane);
		frame.pack();
		frame.setVisible(true);
	}
}