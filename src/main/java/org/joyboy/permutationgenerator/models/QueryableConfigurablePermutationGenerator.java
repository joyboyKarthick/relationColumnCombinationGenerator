package org.joyboy.permutationgenerator.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.joyboy.utils.JoyboyUtils;

public class QueryableConfigurablePermutationGenerator<CS extends ColumnSource> implements PermutationGenerator
{
	protected List<CS> sourceList;
	protected Optional<Map<ColumnSource, List<Predicate<Map<ColumnSource, Object>>>>> skipPatternMap = Optional.ofNullable(null);
	protected Map<String, Predicate<Map<ColumnSource, Object>>> skippedColumnValuePredicate = new HashMap<>();
	protected Map<ColumnSource, Predicate<Map<ColumnSource, Object>>> skipColumnCriteria = new HashMap<>();

	public QueryableConfigurablePermutationGenerator()
	{
	}

	public void addSourceColumn(CS columnSource)
	{
		sourceList = sourceList == null ? JoyboyUtils.consume(new ArrayList<CS>(), al -> al.add(columnSource)) : JoyboyUtils.consume(sourceList, sl -> sl.add(columnSource));
	}

	public void addColumnSkipCriteria(ColumnCriteriaBuilder.ColumnCriteria<CS> columnCriteria)
	{
		for(CS columSource : columnCriteria.getCriteriaColumn())
		{
			Predicate<Map<ColumnSource, Object>> columnCriteriaPredicate = columnCriteria.getPredicate();
			for(ColumnSource columnSource : columnCriteria.getSkippedColumn())
			{
				columnCriteriaPredicate = columnCriteriaPredicate.and(paths -> paths.get(columnSource) != null);
			}
			Predicate<Map<ColumnSource, Object>> predicate = skipColumnCriteria.get(columSource);
			skipColumnCriteria.put(columSource, predicate == null ? columnCriteriaPredicate : predicate.or(columnCriteriaPredicate));
			setSkippedColumnValuePredicate(columnCriteria);
		}
	}

	protected void setSkippedColumnValuePredicate(ColumnCriteriaBuilder.ColumnCriteria<CS> columnCriteria)
	{
		String combinationKey = "";
		int index = 0;
		for(ColumnSource columnSource : sourceList)
		{
			if(!columnCriteria.getSkippedColumn().contains(columnSource))
			{
				combinationKey += index;
			}
			index += 1;
		}
		Predicate<Map<ColumnSource, Object>> predicate = skippedColumnValuePredicate.get(combinationKey);
		skippedColumnValuePredicate.put(combinationKey, predicate == null ? columnCriteria.getPredicate() : predicate.or(columnCriteria.getPredicate()));
	}

	public void generate(int index)
	{
		_generate(index, new LinkedHashMap<>(), "");
	}

	@Override public void destinationHook(Map<ColumnSource, Object> paths)
	{
		System.out.println(paths.values());
	}

	@Override public void _generate(int index, Map<ColumnSource, Object> paths, String combinationKey)
	{
		if(index < sourceList.size())
		{
			_generate(index + 1, paths, combinationKey);
			for(Object columnValue : sourceList.get(index).getColumnValues())
			{
				LinkedHashMap<ColumnSource, Object> currentPath = JoyboyUtils.consume(new LinkedHashMap<>(), c -> c.putAll(paths), c -> c.put(sourceList.get(index), columnValue));
				if(skipColumnCriteria != null && skipColumnCriteria.get(sourceList.get(index)) != null && skipColumnCriteria.get(sourceList.get(index)).test(currentPath))
				{
					continue;
				}
				_generate(index + 1, currentPath, combinationKey + index);
			}
		}
		if(index >= sourceList.size())
		{
			if(paths.size() == sourceList.size())
			{
				destinationHook(paths);
			}
			else if(skippedColumnValuePredicate.containsKey(combinationKey) && skippedColumnValuePredicate.get(combinationKey).test(paths))
			{
				destinationHook(paths);
			}
		}
	}
}
