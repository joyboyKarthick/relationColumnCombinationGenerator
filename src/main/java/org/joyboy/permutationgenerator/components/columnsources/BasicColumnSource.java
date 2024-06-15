package org.joyboy.permutationgenerator.components.columnsources;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicColumnSource<K, T extends Iterable<K>> implements ColumnSource<K, T>
{

	T columnValues;
	String id;
	Optional<Function<K, Boolean>> valueValidator = Optional.empty();

	public BasicColumnSource(T columnValuesSource, String id)
	{
		this.columnValues = columnValuesSource;
		this.id = id;
	}

	BasicColumnSource<K, T> builderPattern(Consumer<BasicColumnSource> consumerFunction)
	{
		consumerFunction.accept(this);
		return this;
	}

	@Override public T getColumnValues()
	{
		return this.columnValues;
	}

	@Override public String setColumnID(String id)
	{
		this.id = id;
		return id;
	}

	@Override public String getColumnID()
	{
		return this.id;
	}

	@Override public Boolean skipValue(Object currentCellValue)
	{
		return valueValidator.isPresent() && valueValidator.get().apply((K) currentCellValue);
	}

	@Override public void setInvalidColumnValueSkipper(Function invalidColumnValueSkipper)
	{
		this.valueValidator = Optional.ofNullable(invalidColumnValueSkipper);
	}
}
