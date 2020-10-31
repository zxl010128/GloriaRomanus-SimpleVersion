package unsw.gloriaromanus.backend;

public interface Observer {
	
	public void update(Subject obj, String FactionName, int OccupiedNum, int treasury, int wealth);
	
}