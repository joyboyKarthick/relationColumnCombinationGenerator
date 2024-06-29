package org.joyboy.combinationgenerator.components.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.joyboy.combinationgenerator.components.columnsources.ColumnSource;

public class ColumnCriteriaBuilder<CS extends ColumnSource>
{
	protected List<CS> skippedColumn = new ArrayList<>();
	protected List<CS> criteriaColumn = new ArrayList<>();
	protected Predicate<Map<CS, Object>> predicate;

	public static <CS extends ColumnSource> Predicate getNullableCriteriaCheck(Predicate<Map<CS, Object>> predicate, List<CS> usedColumns)
	{
		Predicate<Map<CS, Object>> nullCheckpredicate = null;
		for(ColumnSource column : usedColumns)
		{
			if(nullCheckpredicate == null)
			{
				nullCheckpredicate = (paths) -> paths.containsKey(column);
			}
			else
			{
				nullCheckpredicate = nullCheckpredicate.and((paths) -> paths.containsKey(column));
			}
		}
		predicate = nullCheckpredicate.and(predicate);
		return predicate;
	}

	public List<CS> getCriteriaColumn()
	{
		return criteriaColumn;
	}

	public ColumnCriteriaBuilder setCriteriaColumn(List<CS> criteriaColumn)
	{
		this.criteriaColumn = criteriaColumn;
		return this;
	}

	public List<CS> getSkippedColumn()
	{
		return skippedColumn;
	}

	public ColumnCriteriaBuilder setSkippedColumn(List<CS> skippedColumn)
	{
		this.skippedColumn = skippedColumn;
		return this;
	}

	public Predicate<Map<CS, Object>> getPredicate()
	{
		return predicate;
	}

	public ColumnCriteriaBuilder<CS> setPredicate(Predicate<Map<CS, Object>> predicate)
	{
		this.predicate = predicate;
		return this;
	}

	public ColumnCriteria<CS> build()
	{

		return new ColumnCriteria(skippedColumn, criteriaColumn, getNullableCriteriaCheck(predicate, criteriaColumn));
	}

	public enum LogicalOperator
	{
		AND, OR
	}

	public static class ColumnCriteria<CS>
	{
		private final List<CS> skippedColumn;
		private final List<CS> criteriaColumn;
		private final Predicate<Map<CS, Object>> predicate;

		public ColumnCriteria(List<CS> skippedColumn, List<CS> criteriaColumn, Predicate<Map<CS, Object>> predicate)
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

		public Predicate<Map<CS, Object>> getPredicate()
		{
			return predicate;
		}

	}
}
