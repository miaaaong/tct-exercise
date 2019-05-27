package datastructure1;

import java.util.Comparator;

public class KoreanComparator implements Comparator<Score> {

	@Override
	public int compare(Score o1, Score o2) {
		if(o2.getKorean().compareTo(o1.getKorean()) == 0) {
			return o1.getName().compareTo(o2.getName());
		}
		return o2.getKorean().compareTo(o1.getKorean());
	}

}
