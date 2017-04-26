/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.sun.glass.events.KeyEvent;

import client.connetion.ConnectionServerOption;

/**
 *
 * @author DungVan
 */
public class MainOption_GUI extends JFrame {

	/**
	 * Creates new form Option
	 */
	public MainOption_GUI() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		clientOption = new ButtonGroup();
		submit = new JButton();
		pop3Client = new JRadioButton();
		imap4Client = new JRadioButton();
		serverName = new JTextField();

		serverName.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				// TODO Auto-generated method stub
				submitMouseClicked();
			}
		});

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Client Option");
		setResizable(false);

		clientOption.add(pop3Client);
		clientOption.add(imap4Client);

		submit.setText("Done");
		submit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				submitMouseClicked();
			}
		});

		pop3Client.setText("POP3 Client");

		imap4Client.setSelected(true);
		imap4Client.setText("IMAP4 Client");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(84, 84, 84).addComponent(submit)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addContainerGap(20, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addComponent(imap4Client)
												.addGap(18, 18, 18).addComponent(pop3Client))
										.addComponent(serverName, GroupLayout.Alignment.TRAILING,
												GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
								.addGap(19, 19, 19)));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addGap(16, 16, 16)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(pop3Client).addComponent(imap4Client))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(serverName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18).addComponent(submit)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>

	private void submitMouseClicked() {
		// TODO add your handling code here:
		if (pop3Client.isSelected())
			Client_GUI.setCLIENT_TYPE("POP3");
		else
			Client_GUI.setCLIENT_TYPE("IMAP4");

		ConnectionServerOption.setSERVER_NAME(this.serverName.getText().trim());

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Login_GUI().setVisible(true);
			}
		});
		this.dispose();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainOption_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainOption_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainOption_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainOption_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainOption_GUI().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify
	private ButtonGroup clientOption;
	private JRadioButton imap4Client;
	private JRadioButton pop3Client;
	private JTextField serverName;
	private JButton submit;
	// End of variables declaration
}
