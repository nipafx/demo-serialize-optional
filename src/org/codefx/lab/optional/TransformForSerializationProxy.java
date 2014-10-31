package org.codefx.lab.optional;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 * Demonstrates how to make a class serializable which has an {@link Optional} field.
 * <p>
 * Uses the "Transform For Serialization Proxy" approach as described in {@link SerializableOptional}.
 * <p>
 * Note that a class exposing an optional field via accessors smells of bad design. This is just for demo purposes!
 */
@SuppressWarnings("serial")
public class TransformForSerializationProxy<T extends Serializable> implements Serializable {

	// ATTRIBUTES

	private transient Optional<T> optional;

	private T otherField;

	// CONSTRUCTION

	public TransformForSerializationProxy(T optionalValue, T otherFieldValue) {
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

	// SERIALIZATION

	private Object writeReplace() {
		return new SerializationProxy<>(this);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		throw new InvalidObjectException("Serialization proxy expected.");
	}

	private static class SerializationProxy<T extends Serializable> implements Serializable {

		private static final long serialVersionUID = -8040205505091040969L;

		private final SerializableOptional<T> optional;

		private final T otherField;

		public SerializationProxy(TransformForSerializationProxy<T> transform) {
			optional = SerializableOptional.fromOptional(transform.optional);
			otherField = transform.otherField;
		}

		private Object readResolve() {
			return new TransformForSerializationProxy<>(
					optional.asOptional().orElse(null),
					otherField);
		}

	}

}
