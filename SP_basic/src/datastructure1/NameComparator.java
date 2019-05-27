package datastructure1;

import java.util.Comparator;

public class NameComparator implements Comparator<Score> {

	@Override
	public int compare(Score o1, Score o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
