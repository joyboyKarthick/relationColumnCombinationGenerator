package org.joyboy.combinationgenerator.templates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.joyboy.exceptions.JoyBoyException;
import org.joyboy.joyboydatastructures.JoyboyTriple;
import org.joyboy.combinationgenerator.components.Handler.CSVPermutationGenerationHandler;
import org.joyboy.combinationgenerator.components.columnsources.CSVColumnSource;
import org.joyboy.combinationgenerator.components.criteria.ColumnCriteriaBuilder;
import org.joyboy.utils.JoyboyCsvUtils;
import org.joyboy.utils.JoyboyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.opencsv.CSVWriter;

public class CSVQueryablePermutationGeneratorTemplate
{
	protected CSVTemplateSourcePojo csvTemplateSourcePojo;
	protected List<CSVColumnSource> csvSourceColumns;
	protected CSVPermutationGenerationHandler csvPermutationGenerationHandler;
	protected CSVWriter csvWriter;

	public CSVQueryablePermutationGeneratorTemplate(String confTomlPath) throws IOException
	{
		//configuration handling
		ObjectMapper objectMapper = new ObjectMapper(new TomlFactory());
		this.csvTemplateSourcePojo = objectMapper.readValue(new File(confTomlPath), CSVTemplateSourcePojo.class);
		this.csvWriter = JoyboyCsvUtils.getCsvWriter(csvTemplateSourcePojo.csvConfigurations.destinationSource.toString());
		csvSourceColumns = csvTemplateSourcePojo.csvConfigurations.csvColumnSources;
		this.csvPermutationGenerationHandler = new CSVPermutationGenerationHandler(csvSourceColumns, this::destinationHookHandler);
		//criteria handling
		csvTemplateSourcePojo.criteria.ifPresent(criteria -> criteria.skipColumnCriteria.ifPresent(skipColumnCriteria -> handleSkipColumnCriteria(csvSourceColumns, skipColumnCriteria)));
		csvTemplateSourcePojo.criteria.ifPresent(criteria -> criteria.skipValueCriteria.ifPresent(skipValueCriteria -> handleSkipValueCriteria(csvSourceColumns, skipValueCriteria)));
		csvWriter.writeNext(csvTemplateSourcePojo.csvConfigurations.csvColumnSources.stream().map(CSVColumnSource::getColumnID).toArray(String[]::new));
	}

	protected void destinationHookHandler(Map<CSVColumnSource, Object> paths)
	{
		List<Object> outputRow = this.csvTemplateSourcePojo.csvConfigurations.csvColumnSources.stream().map(sc -> paths.getOrDefault(sc, null)).collect(Collectors.toList());
		csvWriter.writeNext(outputRow.toArray(new String[] {}));
	}

	protected void handleSkipValueCriteria(List<CSVColumnSource> csvColumnSources, List<CSVTemplateSourcePojo.SkipCriteria> skipValueCriteria)
	{
		if(skipValueCriteria == null)
		{
			return;
		}

		for(CSVTemplateSourcePojo.SkipCriteria skc : skipValueCriteria)
		{
			List<CSVColumnSource> criteriaColumns = new ArrayList<>();
			Predicate<Map<CSVColumnSource, Object>> parsedSkipCriteria = getColumnCriteriaFromSkipCriteria(csvColumnSources, skc, criteriaColumns);
			ColumnCriteriaBuilder.ColumnCriteria<CSVColumnSource> valueSkipCriteria = new ColumnCriteriaBuilder().setPredicate(parsedSkipCriteria).setCriteriaColumn(criteriaColumns).build();
			this.csvPermutationGenerationHandler.addSkipValueCriteria(valueSkipCriteria);
		}

	}

	protected void handleSkipColumnCriteria(List<CSVColumnSource> csvColumnSources, List<CSVTemplateSourcePojo.SkipCriteria> skipColumnCriteria)
	{
		if(skipColumnCriteria == null)
		{
			return;
		}
		for(CSVTemplateSourcePojo.SkipCriteria skc : skipColumnCriteria)
		{
			List<CSVColumnSource> criteriaColumns = new ArrayList<>();
			List<CSVColumnSource> skippedColumns = skc.skippedColumns.stream().map(skipColumn -> csvColumnSources.stream().filter(cs -> cs.getColumnID().equals(skipColumn))
				.findFirst().orElseThrow(() -> new JoyBoyException(JoyBoyException.INVALID_INPUT, "invalid skip column found in : " + skc.skippedColumns))).collect(Collectors.toList());
			Predicate<Map<CSVColumnSource, Object>> parsedSkipCriteria = getColumnCriteriaFromSkipCriteria(csvColumnSources, skc, criteriaColumns);
			ColumnCriteriaBuilder.ColumnCriteria<CSVColumnSource> columnSkipCriteria = new ColumnCriteriaBuilder().setSkippedColumn(skippedColumns).setPredicate(parsedSkipCriteria)
				.setCriteriaColumn(criteriaColumns).build();
			this.csvPermutationGenerationHandler.addColumnSkipCriteria(columnSkipCriteria);
		}
	}

	protected Predicate<Map<CSVColumnSource, Object>> getColumnCriteriaFromSkipCriteria(List<CSVColumnSource> csvColumnSources, CSVTemplateSourcePojo.SkipCriteria skc, List<CSVColumnSource> criteriaColumns) throws JoyBoyException
	{
		Predicate<Map<CSVColumnSource, Object>> skipCriteria;

		if(skc.criteriaString.contains(" and ") || skc.criteriaString.contains(" or "))
		{
			skipCriteria = JoyboyUtils.parseParenthesesCriteriaString(skc.criteriaString.replace(" or ", " | ").replace(" and ", " & "),
				(joyboyTriple) -> csvParsedCriteriaColumnHandler(joyboyTriple, csvColumnSources, criteriaColumns));
		}
		else
		{
			skipCriteria = getPredicate(skc.criteriaString.replace("(", "").replace(")", ""), csvColumnSources, criteriaColumns);
		}
		return skipCriteria;
	}

	protected Predicate<Map<CSVColumnSource, Object>> csvParsedCriteriaColumnHandler(JoyboyTriple<Object, ColumnCriteriaBuilder.LogicalOperator, Object> logicalCriteria, List<CSVColumnSource> sourceList, List<CSVColumnSource> criteriaColumns)
		throws JoyBoyException
	{
		Predicate<Map<CSVColumnSource, Object>> leftPredicate = logicalCriteria.getLeft() instanceof Predicate ?
			(Predicate) logicalCriteria.getLeft() : getPredicate(String.valueOf(logicalCriteria.getLeft()), sourceList, criteriaColumns);
		Predicate<Map<CSVColumnSource, Object>> rightPredicate = logicalCriteria.getRight() instanceof Predicate ?
			(Predicate) logicalCriteria.getRight() : getPredicate(String.valueOf(logicalCriteria.getRight()), sourceList, criteriaColumns);
		return logicalCriteria.getMiddle().equals(ColumnCriteriaBuilder.LogicalOperator.OR) ? leftPredicate.or(rightPredicate) : leftPredicate.and(rightPredicate);
	}

	protected Predicate<Map<CSVColumnSource, Object>> getPredicate(String columnCriteriaStr, List<CSVColumnSource> sourceList, List<CSVColumnSource> criteriaColumns) throws JoyBoyException
	{
		CSVQueryablePermutationGeneratorTemplate.CriteriaPredicate operatorPredicate = Arrays.stream(CriteriaPredicate.values()).filter(operator -> columnCriteriaStr.contains(operator.operatorName))
			.findFirst().orElseThrow(() -> new JoyBoyException(JoyBoyException.INVALID_INPUT,"invalid criteria operator used in criteria : ".concat(columnCriteriaStr)));
		String[] tokenizedCriteria = columnCriteriaStr.split(operatorPredicate.operatorName);
		if(tokenizedCriteria.length == 2)
		{
			CSVColumnSource criteriaColumn = sourceList.stream().filter(csvColumnSource -> csvColumnSource.getColumnID().equals(tokenizedCriteria[0].trim())).findFirst()
				.orElseThrow(() -> new JoyBoyException(JoyBoyException.INVALID_INPUT,"invalid criteria column : ".concat(tokenizedCriteria[0])));
			criteriaColumns.add(criteriaColumn);
			return operatorPredicate.getPredicate(criteriaColumn, tokenizedCriteria[1].trim());
		}
		throw new JoyBoyException(JoyBoyException.INVALID_INPUT_FORMAT,"invalid criteria string ::: " + columnCriteriaStr);
	}

	public void generate() throws Exception
	{
		this.csvPermutationGenerationHandler.generate();
		csvWriter.flush();
		csvWriter.close();
	}

	protected enum CriteriaPredicate
	{
		Equal(" equal ")
			{
				@Override Predicate<Map<CSVColumnSource, Object>> getPredicate(CSVColumnSource column, String value)
				{
					return (paths) -> paths.get(column).equals(value);
				}
			},
		NotEqual(" not_equal ")
			{
				@Override Predicate<Map<CSVColumnSource, Object>> getPredicate(CSVColumnSource column, String value)
				{
					return (paths) -> !paths.get(column).equals(value);
				}
			};

		String operatorName;

		CriteriaPredicate(String operatorName)
		{
			this.operatorName = operatorName;
		}

		abstract Predicate<Map<CSVColumnSource, Object>> getPredicate(CSVColumnSource column, String value);
	}

}
