package org.codefx.lab.optional;

import java.io.Serializable;
import java.util.Optional;

/**
 * A class which has an attribute of type {@link Optional}. Because it does not treat this correctly, it fails to be
 * executed.
 * <p>
 * Note that a class exposing an optional attribute via accessors smells of bad design. This is just for demo purposes!
 */
public class ClassUsingOptional<T> implements Serializable {

	// ATTRIBUTES

	private static final long serialVersionUID = 5258682920412938156L;

	private final Optional<T> optional;

	private final T otherAttribute;

	// CONSTRUCTION

	public ClassUsingOptional(T optionalValue, T otherAttributeValue) {
		this.optional = Optional.ofNullable(optionalValue);
		this.otherAttribute = otherAttributeValue;
	}

	// ATTRIBUTE ACCESS

	public Optional<T> getOptional() {
		return optional;
	}

	public T getOtherAttribute() {
		return otherAttribute;
	}

}
