package org.joyboy.utils;

import org.joyboy.joyboydatastructures.JoyboyTriple;
import org.joyboy.permutationgenerator.components.criteria.ColumnCriteriaBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JoyboyUtilsTest {

    @Test
    void consume() {
    }

    @Test
    void consumeNotEmpty() {

    }

    @Test
    void parseParenthesesCriteriaString() {
        HashMap<String, Boolean> criteriaAssertionMap = new HashMap<>();
        criteriaAssertionMap.put("(false |  false)", false);
        criteriaAssertionMap.put("(true |  false)", true);
        criteriaAssertionMap.put("(((false | (true & false & true))|(false & true))| false)", false);
        criteriaAssertionMap.put("(((true | (true & false & true))|(false & true))| false)", true);
        for (Map.Entry<String, Boolean> criteriaEntry : criteriaAssertionMap.entrySet()) {
            String processedCriteriaString = criteriaEntry.getKey().replace(" or ", " | ").replace(" and ", " & ");
            Boolean value = JoyboyUtils.parseParenthesesCriteriaString(processedCriteriaString, (parsedCriteria) -> parsedColumnHandler(parsedCriteria));
            assert value == criteriaEntry.getValue();
        }
    }

    public static Boolean parsedColumnHandler(JoyboyTriple<Object, ColumnCriteriaBuilder.LogicalOperator, Object> parsedCriteria) {
        Boolean leftOperand = parsedCriteria.getLeft() instanceof Boolean ? (Boolean) parsedCriteria.getLeft() : Boolean.valueOf(String.valueOf(parsedCriteria.getLeft()).trim());
        Boolean rightOperand = parsedCriteria.getRight() instanceof Boolean ? (Boolean) parsedCriteria.getRight() : Boolean.valueOf(String.valueOf(parsedCriteria.getRight()).trim());
        return parsedCriteria.getMiddle().equals(ColumnCriteriaBuilder.LogicalOperator.AND) ? leftOperand && rightOperand : leftOperand || rightOperand;
    }
}