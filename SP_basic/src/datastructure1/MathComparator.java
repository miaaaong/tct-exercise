package datastructure1;

import java.util.Comparator;

public class MathComparator implements Comparator<Score> {

	@Override
	public int compare(Score o1, Score o2) {
		if(o2.getMath().compareTo(o1.getMath()) == 0) {
			return o1.getName().compareTo(o2.getName());
		}
		return o2.getMath().compareTo(o1.getMath());
	}

}
