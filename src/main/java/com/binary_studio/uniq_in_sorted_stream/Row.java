package com.binary_studio.uniq_in_sorted_stream;

public final class Row<RowData> {

	private final Long id;

	public Row(Long id) {
		this.id = id;
	}

	public Long getPrimaryId() {
		return this.id;
	}
}


// With distinct and equals() and heshCode() overriding:
//
//public final class Row<RowData> {
//
//	private final Long id;
//
//	public Row(Long id) {
//		this.id = id;
//	}
//
//	public Long getPrimaryId() {
//		return this.id;
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (o == this) {
//			return true;
//		}
//		if (o == null || o.getClass() != this.getClass()) {
//			return false;
//		}
//		Row<RowData> row = (Row<RowData>) o;
//		return row.getPrimaryId() == this.getPrimaryId();
//	}
//
//	@Override
//	public int hashCode() {
//		int prime = 31;
//		int result = 1;
//		result = prime * result + getPrimaryId().hashCode();
//		return result;
//	}
//}
