package org.joyboy.combinationgenerator.components.columnsources;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import org.joyboy.utils.JoyboyCsvUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CSVColumnSource implements ColumnSource<String, List<String>>
{
	final Path csvFilePath;
	final String columnName;
	final List<String> sourceList;
	String id;

	@JsonCreator
	public CSVColumnSource(@JsonProperty("identifier") String identifier, @JsonProperty("file_path") String filePath, @JsonProperty("column_name") String columnName)
	{
		this.id = identifier;
		this.csvFilePath = Paths.get(filePath);
		this.columnName = columnName;
		this.sourceList = JoyboyCsvUtils.getColumnValues(this.csvFilePath, columnName);
	}

	@Override public List<String> getColumnValues()
	{
		return this.sourceList;
	}

	@Override public String setColumnID(String id)
	{
		return this.id = id;
	}

	@Override public String getColumnID()
	{
		return this.id;
	}

	@Override public Boolean skipValue(String currentCellValue)
	{
		return Boolean.FALSE;
	}

	@Override public void setInvalidColumnValueSkipper(Function invalidColumnValueSkipper)
	{

	}
}
