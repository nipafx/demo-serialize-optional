package org.codefx.lab.optional;

import java.io.Serializable;
import java.util.Optional;

/**
 * A class which has an field of type {@link Optional}. Because it does not treat this correctly, it fails to be
 * executed.
 * <p>
 * Note that a class exposing an optional field via accessors smells of bad design. This is just for demo purposes!
 */
public class ClassUsingOptional<T> implements Serializable {

	// ATTRIBUTES

	private static final long serialVersionUID = 5258682920412938156L;

	private final Optional<T> optional;

	private final T otherField;

	// CONSTRUCTION

	public ClassUsingOptional(T optionalValue, T otherFieldValue) {
		this.optional = Optional.ofNullable(optionalValue);
		this.otherField = otherFieldValue;
	}

	// ATTRIBUTE ACCESS

	public Optional<T> getOptional() {
		return optional;
	}

	public T getOtherField() {
		return otherField;
	}

}
