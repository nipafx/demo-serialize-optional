package org.codefx.lab.optional;

import java.io.Serializable;
import java.util.Optional;

/**
 * Demonstrates how to make a class serializable which has an {@link Optional} field.
 * <p>
 * Uses the "Transform For Access" approach as described in {@link SerializableOptional}.
 * <p>
 * Note that a class exposing an optional field via accessors smells of bad design. This is just for demo purposes!
 */
public class TransformForAccess<T extends Serializable> implements Serializable {

	// ATTRIBUTES

	private static final long serialVersionUID = -5721223145027479215L;

	private SerializableOptional<T> optional;

	private T otherField;

	// CONSTRUCTION

	public TransformForAccess(T optionalValue, T otherFieldValue) {
		setOptional(Optional.ofNullable(optionalValue));
		this.otherField = otherFieldValue;
	}

	// ATTRIBUTE ACCESS

	public Optional<T> getOptional() {
		return optional.asOptional();
	}

	private void setOptional(Optional<T> optional) {
		this.optional = SerializableOptional.fromOptional(optional);
	}

	public T getOtherField() {
		return otherField;
	}

}
