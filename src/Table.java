import java.util.Random;

public class Table {
	private Integer availableSeating;
	private Party currentParty;
	private int tableNumber;
	
	
	public Table(int num, int tableNumber){
		availableSeating = num;
		currentParty = null;
		this.tableNumber=tableNumber;
	}

	public Integer getAvailableSeating() {
		return availableSeating;
	}

	public void setAvailableSeating(Integer availableSeating) {
		this.availableSeating = availableSeating;
	}

	public Party getCurrentParty() {
		return currentParty;
	}

	public void setCurrentParty(Party currentParty) {
		this.currentParty = currentParty;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	
}
