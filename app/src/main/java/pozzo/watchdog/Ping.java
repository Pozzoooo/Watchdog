package pozzo.watchdog;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ghost on 17/04/15.
 */
public class Ping {

	/**
	 * Test reachability using InetAddress method.
	 *
	 * @param address where to test.
	 * @return is reachable.
	 */
	public boolean pingRequestInet(String address) {
		try {
			return InetAddress.getByName(address).isReachable(5000);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test reachability using a TCP echo message.
	 *
	 * @return latency in nanoseconds.
	 */
	public long pingRequestTcp(String address, int port) throws IOException {
		long latency = -1;
		//Simple way to measure latency
		latency = System.nanoTime();
		Socket socket = new Socket();
		//TODO put timout on config file
		socket.connect(new InetSocketAddress(address, port), 1000);
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		PrintStream ps = new PrintStream(socket.getOutputStream());
		String msg = "Hi";
		ps.println(msg);
		latency = System.nanoTime() - latency;
		socket.close();
		return latency;
	}

	/**
	 * Test reachability using a UDP echo message.
	 *
	 * @return
	 */
//	public String pingRequestUdp() {
//		try {
//			//https://systembash.com/a-simple-java-udp-server-and-udp-client/
//			DatagramSocket socket = new DatagramSocket(7, InetAddress.getByName("127.0.0.1"));
//			DataInputStream dis = new DataInputStream(socket.getInputStream());
//			PrintStream ps = new PrintStream(socket.getOutputStream());
//			ps.println("Hello");
//			String str = dis.readUTF();
//			socket.close();
//			return str;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return e.getMessage();
//		}
//	}

	/**
	 * Ping request using shell.
	 * Locked request, will fully execute and return entire output.
	 *
	 * @return shell output.
	 */
	public String pingRequestShell() {
		String result;
		try {
			Process proccess = Runtime.getRuntime().exec("ping -c 4 186.235.31.222");
			result = "" + proccess.waitFor();
			BufferedReader in2 = new BufferedReader(new InputStreamReader(proccess.getInputStream()));
			String line = null;
			while ((line = in2.readLine()) != null) {
				result += line + "\n";
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
	}
}
