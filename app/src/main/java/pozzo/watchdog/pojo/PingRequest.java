package pozzo.watchdog.pojo;

/**
 * Will be used like a log, so we can get know of all that happened on our watchdog.
 *
 * @author Luiz Gustavo Pozzo
 * @since 17/04/15.
 */
public class PingRequest {
	private long id;
	private long fkWatchEntry;
	private long date;
	private long latency;
	private String fromNetwork;

	{
		id = -1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getFromNetwork() {
		return fromNetwork;
	}

	public void setFromNetwork(String fromNetwork) {
		this.fromNetwork = fromNetwork;
	}

	public long getFkWatchEntry() {
		return fkWatchEntry;
	}

	public void setFkWatchEntry(long fkWatchEntry) {
		this.fkWatchEntry = fkWatchEntry;
	}
}
