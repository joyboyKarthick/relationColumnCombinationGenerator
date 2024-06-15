package org.joyboy.permutationgenerator.components.Handler;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.joyboy.permutationgenerator.components.columnsources.CSVColumnSource;
import org.joyboy.permutationgenerator.components.criteria.ColumnCriteriaBuilder;
import org.joyboy.permutationgenerator.components.generators.QueryableConfigurablePermutationGenerator;

public class CSVPermutationGenerationHandler
{
	public final List<CSVColumnSource> sourceList;
	protected final Consumer<Map<CSVColumnSource, Object>> destinationHookConsumer;
	protected final QueryableConfigurablePermutationGenerator<CSVColumnSource> generator;

	public CSVPermutationGenerationHandler(List<CSVColumnSource> sourceList, Consumer<Map<CSVColumnSource, Object>> destinationHook)
	{
		this.sourceList = sourceList;
		this.destinationHookConsumer = destinationHook;
		this.generator = new QueryableConfigurablePermutationGenerator<CSVColumnSource>()
		{
			@Override public void destinationHook(Map<CSVColumnSource, Object> paths) throws Exception
			{
				destinationHookConsumer.accept(paths);
			}
		};
		generator.addSourceColumns(this.sourceList);
	}

	public void addColumnSkipCriteria(ColumnCriteriaBuilder.ColumnCriteria<CSVColumnSource> columnCriteria)
	{
		generator.addColumnSkipCriteria(columnCriteria);
	}

	public void addSkipValueCriteria(ColumnCriteriaBuilder.ColumnCriteria<CSVColumnSource> columnCriteria)
	{
		generator.addSkipValueCriteria(columnCriteria);
	}

	public void generate() throws Exception
	{
		generator.generate();
	}
}
