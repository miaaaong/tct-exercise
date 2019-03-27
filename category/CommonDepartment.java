package com.lgcns.tct.commondept;

public class CommonDepartment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(get("재무담당", "재무1팀"));
	}
	
	private static String get(String thisDept, String thatDept) {
		String common = null;
		String name = thisDept;
		while(common == null) {
			common = getCommonDept(name, thatDept);
			if(common == null) {
				name = getDeptInfo(name)[1];
			}
		}
		return common;
	}
	
	private static String getCommonDept(String thisDept, String thatDept) {
		System.out.println(thisDept + ", " + thatDept);
		if(thisDept.equals(thatDept)) {
			return thisDept;
		} else {
			String[] info = getDeptInfo(thatDept);
			if(thisDept.equals(info[1])) {
				return thisDept;
			} else {
				if(info[1].equals("-")) {
					return null;
				}
				return getCommonDept(thisDept, info[1]);
			}
		}
	}
	
	private static String getCommonDept_bak(String thisDept, String thatDept) {
		if(thisDept.equals(thatDept)) {
			return thisDept;
		} else {
			String[] info1 = getDeptInfo(thisDept);
			String[] info2 = getDeptInfo(thatDept);
			if(thisDept.equals(info2[1])) {
				return thisDept;
			} else if(thatDept.equals(info1[1])) {
				return thatDept;
			} else {
				return getCommonDept_bak(info1[1], info2[1]);
			}
		}
	}
	
	private static String[] getDeptInfo(String dept) {
		for(String[] info : deptInfo) {
			if(info[0].equals(dept)) {
				return info;
			}
		}
		return null;
	}

	private static String[][] deptInfo = {
		{"CEO", "-"},
		{"CTO", "CEO"},
		{"CFO", "CEO"},
		{"CHO", "CEO"},
		{"기술담당", "CTO"},
		{"품질담당", "CTO"},
		{"재무담당", "CFO"},
		{"회계담당", "CFO"},
		{"인사담당", "CHO"},
		{"교육담당", "CHO"},
		{"기술1팀", "기술담당"},
		{"기술2팀", "기술담당"},
		{"재무1팀", "재무담당"},
		{"재무2팀", "재무담당"},
	};
}
