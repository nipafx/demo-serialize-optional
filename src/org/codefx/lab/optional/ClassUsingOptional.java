package org.codefx.lab.optional;

import java.io.Serializable;
import java.util.Optional;

public class ClassUsingOptional<T> implements Serializable {

	private final Optional<T> optional;

	private final T otherAttribute;

	public ClassUsingOptional(T optionalValue, T otherAttributeValue) {
		this.optional = Optional.ofNullable(optionalValue);
		this.otherAttribute = otherAttributeValue;
	}

	public Optional<T> getOptional() {
		return optional;
	}

	public T getOtherAttribute() {
		return otherAttribute;
	}

}
