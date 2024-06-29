package org.joyboy.combinationgenerator.components.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.joyboy.combinationgenerator.components.columnsources.ColumnSource;
import org.joyboy.utils.JoyboyUtils;

public abstract class ConfigurablePermutationGenerator<CS extends ColumnSource> implements PermutationGenerator<CS>
{
	protected List<CS> sourceList;
	protected Optional<Map<CS, List<Predicate<Map<CS, Object>>>>> skipPatternMap = Optional.empty();

	public ConfigurablePermutationGenerator(List<CS> sourceList)
	{
		this.sourceList = Collections.unmodifiableList(sourceList);
	}

	public void generate(int index) throws Exception
	{
		_generate(index, new LinkedHashMap<>(), "");
	}

	@Override public void _generate(int index, Map<CS, Object> paths, String permutationKey) throws Exception
	{
		if(index < sourceList.size())
		{
			_generate(index + 1, paths, permutationKey);
			for(Object columnValue : sourceList.get(index).getColumnValues())
			{
				if(sourceList.get(index).skipValue(columnValue))
				{
					continue;
				}
				paths.put(sourceList.get(index),columnValue);
				if(skipPatternMap.isPresent() && skipPatternMap.get().getOrDefault(sourceList.get(index), new ArrayList<>()).stream().anyMatch(predicate -> predicate.test(paths)))
				{
					continue;
				}
				_generate(index + 1, paths, permutationKey + index);
				paths.remove(sourceList.get(index));
			}
		}
		if(index >= sourceList.size())
		{
			destinationHook(paths);
		}
	}
}
