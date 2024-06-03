package org.joyboy.permutationgenerator.models;

import java.util.Map;

public interface PermutationGenerator
{
	void destinationHook(Map<ColumnSource, Object> paths);

	void _generate(int index, Map<ColumnSource, Object> paths, String combinationKey);
}
