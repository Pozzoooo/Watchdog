package pozzo.watchdog.pojo;

/**
 * Created by ghost on 17/04/15.
 */
public class PingRequest {
	private String address;
	private long latency;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}
}
