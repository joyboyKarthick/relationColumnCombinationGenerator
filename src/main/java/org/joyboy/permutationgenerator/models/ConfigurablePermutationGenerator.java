package org.joyboy.permutationgenerator.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.joyboy.utils.JoyboyUtils;

public abstract class ConfigurablePermutationGenerator<CS extends ColumnSource> implements PermutationGenerator
{
	protected List<CS> sourceList;
	protected Optional<Map<ColumnSource, List<Predicate<Map<ColumnSource, Object>>>>> skipPatternMap = Optional.empty();

	public ConfigurablePermutationGenerator(List<CS> sourceList)
	{
		this.sourceList = Collections.unmodifiableList(sourceList);
	}

	public void generate(int index)
	{
		_generate(index, new LinkedHashMap<>(), "");
	}

	@Override public void _generate(int index, Map<ColumnSource, Object> paths, String combinationKey)
	{
		if(index < sourceList.size())
		{
			_generate(index+1,paths,combinationKey);
			for(Object columnValue : sourceList.get(index).getColumnValues())
			{
				if(sourceList.get(index).skipValue(columnValue))
				{
					continue;
				}
				LinkedHashMap<ColumnSource, Object> currentPath = JoyboyUtils.consume(new LinkedHashMap<>(), c -> c.putAll(paths), c -> c.put(sourceList.get(index), columnValue));
				if(skipPatternMap.isPresent() && skipPatternMap.get().getOrDefault(sourceList.get(index), new ArrayList<>()).stream().anyMatch(predicate -> predicate.test(paths)))
				{
					continue;
				}
				_generate(index + 1, currentPath, combinationKey + index);
			}
		}
		if(index >= sourceList.size())
		{
			System.out.println(combinationKey);
			destinationHook(paths);
		}
	}
}
