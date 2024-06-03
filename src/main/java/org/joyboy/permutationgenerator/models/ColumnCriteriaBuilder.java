package org.joyboy.permutationgenerator.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ColumnCriteriaBuilder
{
	protected List<ColumnSource> skippedColumn = new ArrayList<>();
	protected List<ColumnSource> criteriaColumn = new ArrayList<>();
	protected Predicate<Map<ColumnSource, Object>> predicate;

	public static Predicate getNullableCriteriaCheck(Predicate<Map<ColumnSource, Object>> predicate, List<? extends ColumnSource> usedColumns)
	{
		for(ColumnSource column : usedColumns)
		{
			predicate = predicate.and((map) -> map.containsKey(column));
		}
		return predicate;
	}

	public List<ColumnSource> getCriteriaColumn()
	{
		return criteriaColumn;
	}

	public ColumnCriteriaBuilder setCriteriaColumn(List<ColumnSource> criteriaColumn)
	{
		this.criteriaColumn = criteriaColumn;
		return this;
	}

	public List<ColumnSource> getSkippedColumn()
	{
		return skippedColumn;
	}

	public ColumnCriteriaBuilder setSkippedColumn(List<ColumnSource> skippedColumn)
	{
		this.skippedColumn = skippedColumn;
		return this;
	}

	public Predicate<Map<ColumnSource, Object>> getPredicate()
	{
		return predicate;
	}

	public ColumnCriteriaBuilder setPredicate(Predicate<Map<ColumnSource, Object>> predicate)
	{
		this.predicate = predicate;
		return this;
	}

	public ColumnCriteria build()
	{

		return new ColumnCriteria(skippedColumn, criteriaColumn, getNullableCriteriaCheck(predicate, criteriaColumn));
	}

	public static class ColumnCriteria<CS>
	{
		private final List<CS> skippedColumn ;
		private final List<CS> criteriaColumn ;
		private final Predicate<Map<ColumnSource, Object>> predicate;

		public ColumnCriteria(List<CS> skippedColumn, List<CS> criteriaColumn, Predicate<Map<ColumnSource, Object>> predicate)
		{
			this.skippedColumn = skippedColumn;
			this.criteriaColumn = criteriaColumn;
			this.predicate = predicate;
		}

		public List<CS> getCriteriaColumn()
		{
			return criteriaColumn;
		}

		public List<CS> getSkippedColumn()
		{
			return skippedColumn;
		}

		public Predicate<Map<ColumnSource, Object>> getPredicate()
		{
			return predicate;
		}

	}
}
