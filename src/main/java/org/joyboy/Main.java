package org.joyboy;

import org.joyboy.exceptions.JoyBoyException;
import org.joyboy.joyboydatastructures.JoyboyTriple;
import org.joyboy.permutationgenerator.components.criteria.ColumnCriteriaBuilder;
import org.joyboy.permutationgenerator.templates.CSVQueryablePermutationGeneratorTemplate;
import org.joyboy.utils.JoyboyUtils;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
	public static void main(String[] args) throws Exception
	{
		String confToml = "src/main/resources/CsvTemplateInput.toml";
		String criteriaString = "(((true | (true & false & true))|(false & true))| false)";
		String processedCriteriaString = criteriaString.replace(" or ", " | ").replace(" and ", " & ");
		Boolean value = JoyboyUtils.parseParenthesesCriteriaString(processedCriteriaString, (parsedCriteria) -> parsedColumnHandler(parsedCriteria));
		System.out.println(value);

		CSVQueryablePermutationGeneratorTemplate csvQueryablePermutationGeneratorTemplate = new CSVQueryablePermutationGeneratorTemplate(confToml);
		csvQueryablePermutationGeneratorTemplate.generate();

	}

	public static Boolean parsedColumnHandler(JoyboyTriple<Object, ColumnCriteriaBuilder.LogicalOperator, Object> parsedCriteria)
	{
		Boolean leftOperand = parsedCriteria.getLeft() instanceof Boolean ? (Boolean) parsedCriteria.getLeft() : Boolean.valueOf(String.valueOf(parsedCriteria.getLeft()).trim());
		Boolean rightOperand = parsedCriteria.getRight() instanceof Boolean ? (Boolean) parsedCriteria.getRight() : Boolean.valueOf(String.valueOf(parsedCriteria.getRight()).trim());
		return parsedCriteria.getMiddle().equals(ColumnCriteriaBuilder.LogicalOperator.AND) ? leftOperand && rightOperand : leftOperand || rightOperand;
	}
}
