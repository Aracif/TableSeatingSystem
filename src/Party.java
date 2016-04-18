
public class Party {
	private String name;
	private int size;
	private Location section;
	
	public Party(String name, int size, Location section){
		this.name=name;
		this.size=size;
		this.section=section;
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

}
