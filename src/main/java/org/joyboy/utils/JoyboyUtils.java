package org.joyboy.utils;

import java.util.Arrays;
import java.util.function.Consumer;

public class JoyboyUtils
{
	public static <C> C consume(C collection, Consumer<C>... consumers)
	{
		Arrays.stream(consumers).forEach(consumer -> consumer.accept(collection));
		return collection;
	}

}
