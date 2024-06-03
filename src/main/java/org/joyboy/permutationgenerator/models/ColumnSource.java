package org.joyboy.permutationgenerator.models;

import java.util.function.Function;

public interface ColumnSource<K,T extends Iterable<K>>
{
	Iterable<K> getColumnValues();
	String setColumnID(String id);
	String getColumnID();
	Boolean skipValue(K currentCellValue);
	void setInvalidColumnValueSkipper(Function<K,Boolean> invalidColumnValueSkipper);
}
