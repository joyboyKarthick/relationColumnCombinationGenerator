package org.joyboy.joyboydatastructures;

public class JoyboyTriple<left, middle, right>
{
	left left;
	middle middle;
	right right;

	public JoyboyTriple(left left, middle middle, right right)
	{
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	public left getLeft()
	{
		return left;
	}

	public middle getMiddle()
	{
		return middle;
	}

	public right getRight()
	{
		return right;
	}
}