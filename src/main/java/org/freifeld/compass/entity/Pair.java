package org.freifeld.compass.entity;

/**
 * @author royif
 * @since 03/09/17.
 */
public class Pair<F, S>
{
	final public F first;
	final public S second;

	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}

	public static <F, S> Pair<F, S> of(F first, S second)
	{
		return new Pair<>(first, second);
	}
}
