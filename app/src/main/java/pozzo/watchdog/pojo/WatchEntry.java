package pozzo.watchdog.pojo;

import java.io.Serializable;

/**
 * Pojo to represent a entry to be watched by our Watchdog System.
 * Every field is documented by its field.
 *
 * @author Luiz Gustavo Pozzo
 * @since 17/04/15.
 */
public class WatchEntry implements Serializable {
	/** Database _id. */
	private long id;
	/** Full address path to be checked. */
	private String address;
	/** Type of request, like Ping, GET. */
	private String requestType;
	/** Port to be checked on server. */
	private int port;
	/** If we should alarm the user that server went down. */
	private boolean alarm;
	/** Email to send a notification if server goes down. */
	private String email;
	/** Frequency in which server will be checked for responsiveness. */
	private long frequency;
	/** If an entry is able to suffer delay on watch to save battery on wakes. */
	private boolean canBeDelayed;
	/** When next watch should happen. */
	private long nextWatch;

	{
		id = -1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAlarm() {
		return alarm;
	}

	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public boolean isCanBeDelayed() {
		return canBeDelayed;
	}

	public void setCanBeDelayed(boolean canBeDelayed) {
		this.canBeDelayed = canBeDelayed;
	}

	public long getNextWatch() {
		return nextWatch;
	}

	public void setNextWatch(long nextWatch) {
		this.nextWatch = nextWatch;
	}
}
