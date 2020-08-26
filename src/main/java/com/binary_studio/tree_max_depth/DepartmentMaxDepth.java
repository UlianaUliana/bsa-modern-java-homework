package com.binary_studio.tree_max_depth;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public final class DepartmentMaxDepth {

	private DepartmentMaxDepth() {
	}

	public static Integer calculateMaxDepth(Department rootDepartment) {
		if (rootDepartment == null) {
			return 0;
		}
		int depth = 0;
		Queue<Department> departments = new LinkedList<>();
		departments.add(rootDepartment);
		int counter;
		for (;;) {
			counter = departments.size();
			if (counter == 0) {
				return depth;
			}
			depth++;
			while (counter > 0) {
				List<Department> notNullSubDepartments = departments.remove()
						.getSubDepartments()
						.stream()
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				departments.addAll(notNullSubDepartments);
				counter--;
			}
		}
	}
}

//Second way - with recursion

//public static Integer calculateMaxDepth(Department rootDepartment) {
//		if (rootDepartment == null) return 0;
//		int depth = 0;
//		if (rootDepartment.subDepartments == null) return 0;
//		for (Department subDepartment : rootDepartment.subDepartments) {
//			depth = Math.max(depth, calculateMaxDepth(subDepartment));
//		}
//		return ++depth;
//	}
