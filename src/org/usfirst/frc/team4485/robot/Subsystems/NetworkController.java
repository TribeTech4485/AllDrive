package org.usfirst.frc.team4485.robot.Subsystems;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class NetworkController implements Runnable {

	private DatagramSocket socket;
	private byte[] buffer = new byte[256];
	private boolean running = true;
	
	private boolean enabled = true;
	private boolean error = false;

	private int port = 5800;
	
	private String dataReceived = "";
	private int numRcvd = 0;
	
	private boolean ranInit = false;
	
	public NetworkController() {
	}
	
	private void init() {
		if (ranInit) return;
		ranInit = true;
		if (!enabled) return;
		System.out.println("Warning: Network controller started on port " + port);
		try {
			socket = new DatagramSocket(port);
		} catch (Exception ex) {
			error = true;
		}
	}
	
	public void run() {
		if (!ranInit) init();
		running = true;
		if (!enabled || error) return;
		while (running && enabled && !error) {
			//System.out.println("Warning: Network controller waiting for data. Received " + numRcvd + " messages");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			// Receive the packet
			try {
				socket.receive(packet);
			} catch(Exception ex) {
				error = true;
				System.out.println(ex.getMessage());
			}
			
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			
			packet = new DatagramPacket(buffer, buffer.length, address, port);
			dataReceived = new String(packet.getData(), 0, packet.getLength());
			
			if (dataReceived == "end") {
				running = false;
			} else if (dataReceived == "") {
				dataReceived = "[none]";
			}
			
			// Send the packet
			try {
				socket.send(packet);
			} catch (IOException ex) {
				error = true;
				System.out.println(ex.getMessage());
			}
			numRcvd ++;
			
		}
		socket.close();
	}
	
	public void setPort(int _port) {
		port = _port;
	}
	
	public String getRcvd() { 
		return dataReceived; 		
	}
	public int getNumRcvd() {
		return numRcvd;
	}
	
}
