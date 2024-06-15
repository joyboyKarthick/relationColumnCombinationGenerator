package org.joyboy.permutationgenerator.templates;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.joyboy.permutationgenerator.components.columnsources.CSVColumnSource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CSVTemplateSourcePojo
{

	public final Configurations csvConfigurations;
	public final Optional<Criteria> criteria;

	public CSVTemplateSourcePojo(@JsonProperty("configurations") Configurations csvConfigurations, @JsonProperty("criteria") Criteria criteria)
	{
		this.csvConfigurations = csvConfigurations;
		this.criteria = Optional.ofNullable(criteria);
	}

	public static class Configurations
	{
		public final Path destinationSource;
		public final List<CSVColumnSource> csvColumnSources;

		public Configurations(@JsonProperty("destination_source") Path destinationSource, @JsonProperty("csv_column_source") List<CSVColumnSource> CSVColumnSources)
		{
			this.destinationSource = destinationSource;
			this.csvColumnSources = CSVColumnSources;
		}
	}

	public static class Criteria
	{
		public final Optional<List<SkipCriteria>> skipColumnCriteria;
		public final Optional<List<SkipCriteria>> skipValueCriteria;

		public Criteria(@JsonProperty("skip_column_criteria") List<SkipCriteria> skipColumnCriteria, @JsonProperty("skip_value_criteria") List<SkipCriteria> skipValueCriteria)
		{
			this.skipColumnCriteria = Optional.ofNullable(skipColumnCriteria);
			this.skipValueCriteria = Optional.ofNullable(skipValueCriteria);
		}
	}

	public static class SkipCriteria
	{
		public final List<String> skippedColumns;
		public final String criteriaString;

		public SkipCriteria(@JsonProperty("skipped_columns_identifier") List<String> skippedColumns, @JsonProperty("criteria_string") String criteriaString)
		{
			this.skippedColumns = skippedColumns;
			this.criteriaString = criteriaString;
		}
	}
}
