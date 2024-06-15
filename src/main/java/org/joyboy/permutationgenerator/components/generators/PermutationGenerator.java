package org.joyboy.permutationgenerator.components.generators;

import java.util.Map;

import org.joyboy.permutationgenerator.components.columnsources.ColumnSource;

public interface PermutationGenerator<CS extends ColumnSource>
{
	void destinationHook(Map<CS, Object> paths) throws Exception;

	void _generate(int index, Map<CS, Object> paths, String permutationKey) throws Exception;
}
