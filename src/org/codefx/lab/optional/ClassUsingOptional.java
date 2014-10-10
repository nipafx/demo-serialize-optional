package org.codefx.lab.optional;

import java.io.Serializable;
import java.util.Optional;

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
