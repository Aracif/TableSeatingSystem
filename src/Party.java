
public class Party {
	private String name;
	private int size;
	private Location section;
	private Table table;
	
	public Party(String name, int size, Location section, Table t){
		this.name=name;
		this.size=size;
		this.section=section;
		table=t;
	}
	
	public Party(String name, int size, Location section){
		this.name=name;
		this.size=size;
		this.section=section;
		table=null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Location getSection() {
		return section;
	}

	public void setSection(Location section) {
		this.section = section;
	}
	
	public String toString(){
		return name + "'s party of " + size + " waiting to be seated in the " + section.toString();
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

}
