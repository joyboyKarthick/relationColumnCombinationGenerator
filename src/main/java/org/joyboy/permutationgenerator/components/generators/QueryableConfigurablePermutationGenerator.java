package org.joyboy.permutationgenerator.components.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.joyboy.exceptions.JoyBoyException;
import org.joyboy.permutationgenerator.components.columnsources.ColumnSource;
import org.joyboy.permutationgenerator.components.criteria.ColumnCriteriaBuilder;
import org.joyboy.utils.JoyboyUtils;

public abstract class QueryableConfigurablePermutationGenerator<CS extends ColumnSource> implements PermutationGenerator<CS>
{
	protected List<CS> sourceList;
	public final Comparator<ColumnSource> descendingComparatorOrder = new Comparator<ColumnSource>()
	{
		@Override public int compare(ColumnSource o1, ColumnSource o2)
		{
			return sourceList.indexOf(o2) < sourceList.indexOf(o1) ? -1 : 1;
		}
	};
	protected Map<CS, Predicate<Map<CS, Object>>> skipValueMap = new HashMap<>();
	protected Map<CS, Predicate<Map<CS, Object>>> skipColumnCriteria = new HashMap<>();
	protected Map<String, Predicate<Map<CS, Object>>> skippedColumnValuePredicate = new HashMap<>();

	public void addSourceColumn(CS columnSource)
	{
		sourceList = sourceList == null ? JoyboyUtils.consume(new ArrayList<>(), al -> al.add(columnSource)) : JoyboyUtils.consume(sourceList, sl -> sl.add(columnSource));
	}

	public void addSourceColumns(List<CS> columnSources)
	{
		sourceList = sourceList == null ? JoyboyUtils.consume(new ArrayList<>(), al -> al.addAll(columnSources)) : JoyboyUtils.consume(sourceList, sl -> sl.addAll(columnSources));
	}

	public void addColumnSkipCriteria(ColumnCriteriaBuilder.ColumnCriteria<CS> columnCriteria)
	{
		columnCriteria.getCriteriaColumn().sort(descendingComparatorOrder);
		columnCriteria.getSkippedColumn().sort(descendingComparatorOrder);
		for(CS criteriaColumSource : columnCriteria.getCriteriaColumn())
		{
			Predicate<Map<CS, Object>> columnCriteriaPredicate = columnCriteria.getPredicate();
			Predicate<Map<CS, Object>> nullCheckPredicate = null;
			for(ColumnSource skippedColumnSource : columnCriteria.getSkippedColumn())
			{
				if(nullCheckPredicate == null)
				{
					nullCheckPredicate = paths -> paths.containsKey(skippedColumnSource);
				}
				else
				{
					nullCheckPredicate = nullCheckPredicate.or((paths) -> paths.containsKey(skippedColumnSource));
				}
			}
			columnCriteriaPredicate = nullCheckPredicate.and(columnCriteriaPredicate);
			criteriaColumSource = sourceList.indexOf(columnCriteria.getSkippedColumn().get(0)) > sourceList.indexOf(criteriaColumSource) ? columnCriteria.getSkippedColumn().get(0) : criteriaColumSource;
			Predicate<Map<CS, Object>> predicate = skipColumnCriteria.get(criteriaColumSource);
			skipColumnCriteria.put(criteriaColumSource, predicate == null ? columnCriteriaPredicate : predicate.or(columnCriteriaPredicate));
			setSkippedColumnValuePredicate(columnCriteria);
		}
	}

	protected void setSkippedColumnValuePredicate(ColumnCriteriaBuilder.ColumnCriteria<CS> columnCriteria)
	{
		StringBuilder combinationKey = new StringBuilder();
		int index = 0;
		for(ColumnSource columnSource : sourceList)
		{
			if(!columnCriteria.getSkippedColumn().contains(columnSource))
			{
				combinationKey.append(index);
			}
			index += 1;
		}
		Predicate<Map<CS, Object>> predicate = skippedColumnValuePredicate.get(combinationKey.toString());
		skippedColumnValuePredicate.put(combinationKey.toString(), predicate == null ? columnCriteria.getPredicate() : predicate.or(columnCriteria.getPredicate()));
	}

	public void addSkipValueCriteria(ColumnCriteriaBuilder.ColumnCriteria<CS> columnCriteria)
	{
		Collections.sort(columnCriteria.getSkippedColumn(), descendingComparatorOrder);
		CS highOrderColumn = columnCriteria.getCriteriaColumn().stream().findFirst().orElseThrow(() -> new JoyBoyException( JoyBoyException.RESOURCE_NOT_FOUND,"empty criteria columns"));
		if(this.skipValueMap.containsKey(highOrderColumn))
		{
			this.skipValueMap.put(highOrderColumn, this.skipValueMap.get(highOrderColumn).or(columnCriteria.getPredicate()));
		}
		else
		{
			this.skipValueMap.put(highOrderColumn, columnCriteria.getPredicate());
		}
	}

	public void generate() throws Exception
	{
		_generate(0, new LinkedHashMap<>(), "");
	}

	@Override public void _generate(int index, Map<CS, Object> paths, String permutationKey) throws Exception
	{

		if(index < sourceList.size())
		{
			_generate(index + 1, paths, permutationKey);
			for(Object columnValue : sourceList.get(index).getColumnValues())
			{
				LinkedHashMap<CS, Object> currentPath = JoyboyUtils.consume(new LinkedHashMap<>(), c -> c.putAll(paths), c -> c.put(sourceList.get(index), columnValue));
				if(skipColumnCriteria.containsKey(sourceList.get(index)) && skipColumnCriteria.get(sourceList.get(index)).test(currentPath))
				{
					continue;
				}
				if(skipValueMap.containsKey(sourceList.get(index)) && skipValueMap.get(sourceList.get(index)).test(currentPath))
				{
					continue;
				}
				_generate(index + 1, currentPath, permutationKey + index);
			}
		}

		if(index >= sourceList.size())
		{
			if(paths.size() == sourceList.size())
			{
				destinationHook(paths);
			}
			else if(skippedColumnValuePredicate.containsKey(permutationKey) && skippedColumnValuePredicate.get(permutationKey).test(paths))
			{
				destinationHook(paths);
			}
		}
	}
}
