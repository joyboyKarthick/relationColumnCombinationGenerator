package org.joyboy.combinationgenerator.components.columnsources;

import java.util.function.Function;

public interface ColumnSource<K, T extends Iterable<K>>
{
	T getColumnValues();

	String setColumnID(String id);

	String getColumnID();

	Boolean skipValue(K currentCellValue);

	void setInvalidColumnValueSkipper(Function<K, Boolean> invalidColumnValueSkipper);
}
