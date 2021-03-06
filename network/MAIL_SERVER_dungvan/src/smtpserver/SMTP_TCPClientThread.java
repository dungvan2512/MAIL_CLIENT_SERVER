package smtpserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.connection.*;
import client.smtp.SendMailSMTP;
import dataserver.MailServerDataOption;
import mainserver.MainServer_GUI;

public class SMTP_TCPClientThread extends Thread {

	public static final byte EHLO_STATE = 0;
	public static final byte MAIL_FROM_STATE = 1;
	public static final byte RCPT_TO_STATE = 2;
	public static final byte DATA_STATE = 3;
	public static final byte END_STATE = 4;

	private boolean toThisServer = false;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int state = 0;
	private String allCommand = "\n===================================\n";

	public SMTP_TCPClientThread(Socket socket) {
		this.socket = socket;
		try {
			oos = new ObjectOutputStream(this.socket.getOutputStream());
			ois = new ObjectInputStream(this.socket.getInputStream());
		} catch (Exception ex) {
			Logger.getLogger(SMTP_TCPClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void sendMessage(String message) {
		try {
			oos.writeUTF(message);
			oos.flush();
		} catch (IOException ex) {
			Logger.getLogger(SMTP_TCPClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void run() {
		String response = "", data = "", senderName = "", receiverName = "", servername= "";
		try {
			String line = null;
			if (socket.isConnected()) {
				allCommand += "connected to " + socket.getInetAddress().getHostAddress() + "\n"
						+ "--------------------===============--------------------\n";
				oos.writeUTF("220 Server access OK");
				oos.flush();
			}
			while (!socket.isClosed()) {
				line = ois.readUTF();
				if (line != null) {
					allCommand += "C: " + line + "\n";
					line = line.toLowerCase().trim();
					/*
					 * if received quit command so close connection
					 */
					if (line.equals("quit")) {
						if (state == END_STATE)
							if (toThisServer) {
								MailServerDataOption.saveEmail(receiverName, senderName, data);
							}else{
								final String sdname = senderName;
								final String rcvname = receiverName;
								final String dt = data;
								final String serverTo = servername;
								Thread t = new Thread(){
									String servTo = serverTo;
									String senderName = sdname, receiverName = rcvname, subject = "", data = dt;
									@Override
									public void run() {
										ConnectionServerOption.SERVER_NAME = servTo;
											SendMailSMTP send = new SendMailSMTP();
											try {
												send.sendMail(senderName, receiverName, subject, data);
											} catch (Exception e) {
												// TODO Auto-generated catch block
											MainServer_GUI.ta_showconnection.append("\n can't connect to server "+ serverTo+"\n");
											}
									}
								};
							}
						
						sendMessage("251, Bye");
						allCommand += "S: " + "251, Bye\n";
						allCommand += "--------------------===============--------------------\nclose connection with "
								+ socket.getInetAddress().getHostAddress() + "\n"
								+ "===================================";
						MainServer_GUI.append(MainServer_GUI.ta_showsmtpcomunication, allCommand);
						this.oos.close();
						this.ois.close();
						this.socket.close();
						return;
					}

					/*
					 * get request and response to client
					 */
					switch (state) {
					case EHLO_STATE:
						if (line.equals("helo") || line.startsWith("ehlo ")) {
							response = "250 hello " + InetAddress.getLocalHost().getHostName() + " ,OK";
							state++;
						} else
							response = "ERROR HELO/HELO mail.example.com ";
						sendMessage(response);
						allCommand += "S: " + response + "\n";
						break;
					case MAIL_FROM_STATE:
						if (line.startsWith("mail from: <") && line.endsWith(">") && !line.split("<")[1].equals(">")) {
							/*
							 * check sender name is null?
							 */
							senderName = line.split("<")[1].split(">")[0];
							if (senderName.trim().equals("")) {
								response = "ERROR need command : MAIL FROM: <example@example.com>";
							} else {
								response = "250 sender <" + senderName + "> ,OK";
							}
							state++;
						} else
							response = "ERROR need command : MAIL FROM: <example@example.com>";
						sendMessage(response);
						allCommand += "S: " + response + "\n";
						break;
					case RCPT_TO_STATE:
						if (line.startsWith("rcpt to: <") && line.trim().endsWith(">")
								&& !line.split("<")[1].equals(">")) {
							/*
							 * check receiver name is null?
							 */
							receiverName = line.split("<")[1].split(">")[0];
							if (receiverName.trim().equals("")) {
								response = "ERROR need command : RCPT TO: <example@example.com>";
							} else {
								if ((servername = receiverName.split("@")[1]) != null) {
									if (servername.equalsIgnoreCase(MainServer_GUI.SERVER_NAME))
										toThisServer = true;
									else
										toThisServer = false;
									response = "250 receiver <" + receiverName + "> ,OK";
								}
							}
							state++;
						} else
							response = "ERROR need command : RCPT TO: <example@example.com>";
						sendMessage(response);
						allCommand += "S: " + response + "\n";
						break;
					case DATA_STATE:
						if (line.equals("data")) {
							response = "354 Send message, end with a \".\" on a line by itself";
							sendMessage(response);
							allCommand += "S: " + response + "\n";
							/*
							 * start to get DATA here
							 */
							data = "";
							line = ois.readUTF();
							while (!line.equals(".")) {
								data += line + "\n";
								line = ois.readUTF();

							}
							/*
							 * got DATA
							 */
							allCommand += "C: " + data + ".\n";
							response = "250 DATA OK";
							sendMessage(response);
							allCommand += "S: " + response + "\n";
							state++;
						} else {
							response = "ERROR need command DATA";
							sendMessage(response);
							allCommand += "S: " + response + "\n";
						}
						break;
					case END_STATE:
						sendMessage("ERROR send QUIT to disconnect this communication");
						allCommand += "S: ERROR send QUIT to disconnect this communication" + "\n";
						break;
					}
				}
			}
			MainServer_GUI.ta_showsmtpcomunication.setText(allCommand);
		} catch (IOException ex) {
			Logger.getLogger(SMTP_TCPClientThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
