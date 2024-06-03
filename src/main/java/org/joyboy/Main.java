package org.joyboy;

import java.util.Arrays;
import java.util.List;

import org.joyboy.permutationgenerator.models.BasicColumnSource;
import org.joyboy.permutationgenerator.models.ColumnCriteriaBuilder;
import org.joyboy.permutationgenerator.models.QueryableConfigurablePermutationGenerator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
	public static void main(String[] args)
	{
		BasicColumnSource<Integer, List<Integer>> column1 = new BasicColumnSource<>(Arrays.asList(1, 2, 3), "Column 1 id");
		BasicColumnSource<String, List<String>> column2 = new BasicColumnSource<>(Arrays.asList("@", "$", "&"), "Column 2 id");
		BasicColumnSource<String, List<String>> column3 = new BasicColumnSource<>(Arrays.asList("messi", "Ronaldo", "Neymer"), "Column 3 id");
		BasicColumnSource<String, List<String>> column4 = new BasicColumnSource<>(Arrays.asList("luffy", "goku", "zoro"), "Column 3 id");

		QueryableConfigurablePermutationGenerator<BasicColumnSource> basicQueryableGenerator = new QueryableConfigurablePermutationGenerator<>();

		ColumnCriteriaBuilder.ColumnCriteria<BasicColumnSource> skipColumn1forNeymer = new ColumnCriteriaBuilder()
			.setCriteriaColumn(Arrays.asList(column3))
			.setPredicate((paths)-> paths.get(column3).equals("Neymer"))
			.setSkippedColumn(Arrays.asList(column1)).build();

		ColumnCriteriaBuilder.ColumnCriteria<BasicColumnSource> skipColumn1forMessi = new ColumnCriteriaBuilder()
			.setCriteriaColumn(Arrays.asList(column3))
			.setPredicate((paths)-> paths.get(column3).equals("messi"))
			.setSkippedColumn(Arrays.asList(column1)).build();

		basicQueryableGenerator.addSourceColumn(column1);
		basicQueryableGenerator.addSourceColumn(column2);
		basicQueryableGenerator.addSourceColumn(column3);
		basicQueryableGenerator.addSourceColumn(column4);
		basicQueryableGenerator.addColumnSkipCriteria(skipColumn1forNeymer);
		basicQueryableGenerator.addColumnSkipCriteria(skipColumn1forMessi);
		basicQueryableGenerator.generate(0);

	}
}