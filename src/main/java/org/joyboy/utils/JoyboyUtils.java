package org.joyboy.utils;

import java.util.Arrays;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import org.joyboy.joyboydatastructures.JoyboyTriple;
import org.joyboy.permutationgenerator.components.criteria.ColumnCriteriaBuilder;

public class JoyboyUtils
{
	public static <C> C consume(C collection, Consumer<C>... consumers)
	{
		Arrays.stream(consumers).forEach(consumer -> consumer.accept(collection));
		return collection;
	}

	public static void consumeNotEmpty(String value, Consumer<String> stringConsumer)
	{
		if(!value.isEmpty() && !value.trim().isEmpty())
		{
			stringConsumer.accept(value);
		}
	}

	public static <T> T parseParenthesesCriteriaString(String criteriaString, Function<JoyboyTriple<Object, ColumnCriteriaBuilder.LogicalOperator, Object>, T> parsedCriteriaHandleFunction)
	{
		Stack<Object> operandStack = new Stack<>();
		Stack<String> operatorStack = new Stack<>();
		StringBuilder tokenBuilder = new StringBuilder();
		for(char o : criteriaString.toCharArray())
		{
			if(o == '(')
			{
				consumeNotEmpty(tokenBuilder.toString(), (s) -> operandStack.push(s));
				tokenBuilder = tokenBuilder.toString().isEmpty() ? tokenBuilder : new StringBuilder();
				operandStack.push("(");
			}
			else if(o == ')')
			{
				while(operandStack.peek() != "(")
				{
					consumeNotEmpty(tokenBuilder.toString(), (s) -> operandStack.push(s));
					tokenBuilder = tokenBuilder.toString().isEmpty() ? tokenBuilder : new StringBuilder();
					String operator = operatorStack.pop();
					T previousResult = parsedCriteriaHandleFunction.apply(new JoyboyTriple<>(operandStack.pop(), operator.equals("|") ? ColumnCriteriaBuilder.LogicalOperator.OR : ColumnCriteriaBuilder.LogicalOperator.AND, operandStack.pop()));
					if(operandStack.peek().equals("("))
					{
						operandStack.pop();
						operandStack.push(previousResult);
						break;
					}
					operandStack.push(previousResult);
				}
			}
			else if(o == '|' || o == '&')
			{
				consumeNotEmpty(tokenBuilder.toString(), (s) -> operandStack.push(s));
				tokenBuilder = tokenBuilder.toString().isEmpty() ? tokenBuilder : new StringBuilder();
				operatorStack.push(String.valueOf(o));

			}
			else
			{
				tokenBuilder.append(o);
			}
		}
		return (T) operandStack.pop();
	}

}
