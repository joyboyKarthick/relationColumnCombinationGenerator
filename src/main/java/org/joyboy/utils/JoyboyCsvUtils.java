package org.joyboy.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.joyboy.exceptions.JoyBoyException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class JoyboyCsvUtils
{
	public static List<String> getColumnValues(Path filePath, String headerName)
	{
		try(CSVReader reader = new CSVReader(new FileReader(filePath.toString())))
		{
			return getColumnValues(reader, headerName);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static List<String> getColumnValues(CSVReader csvReader, String headerName)
	{
		try
		{
			List<String[]> allData = csvReader.readAll();
			if(allData.isEmpty())
			{
				throw new JoyBoyException(JoyBoyException.INVALID_RESOURCE," loaded sheet is empty ");
			}
			String[] columnArray = allData.get(0);
			int headerPosition = IntStream.range(0, columnArray.length).filter(i -> columnArray[i].equals(headerName))
				.findFirst().orElseThrow(() -> new JoyBoyException(JoyBoyException.INVALID_RESOURCE, "header column " + headerName + " not found in the sheet"));
			allData.remove(0);
			return allData.stream().map(row -> row[headerPosition]).filter(x -> !x.isEmpty()).collect(Collectors.toList());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public static CSVWriter getCsvWriter(String filePath) throws IOException
	{
		FileWriter outputfile = new FileWriter(filePath);
		CSVWriter csvWriter = new CSVWriter(outputfile);
		return csvWriter;
	}
}
