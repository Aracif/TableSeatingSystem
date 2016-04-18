public class ListArrayBasedPlus extends ListArrayBased {

	public ListArrayBasedPlus() {
		super();
	}

	public void resize(int index, Object item) {
		Object temp[] = new Object[items.length * 2];
		for (int i = 0; i < items.length; i++) {
			temp[i] = items[i];
		}
		items = temp;
		super.add(index, item);
	}

	public void reverse() {
		Object temp[] = new Object[items.length];
		int validValues = -1;
		int index = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				validValues++;
			}
		}
		for (int k = validValues; k >= 0; k--) {
			temp[index] = items[k];
			index++;
		}
		items = temp;
	}

	public String toString() {
		String info = "";
		for (int i = 0; i < items.length; i++) {
			Object val = items[i];
			if (val != null) {
				info += val + " ";
			}
		}
		return info;
	}
}