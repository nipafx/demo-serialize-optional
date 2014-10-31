package org.codefx.lab.optional;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 * A class which has a field of type {@link Optional}. It uses a serialization proxy to correctly store the field.
 * <p>
 * Note that a class exposing an optional field via accessors smells of bad design. This is just for demo purposes!
 */
@SuppressWarnings("serial")
public class ClassUsingOptionalCorrectly<T> implements Serializable {

	// ATTRIBUTES

	private final Optional<T> optional;

	private final T otherField;

	// CONSTRUCTION

	public ClassUsingOptionalCorrectly(T optionalValue, T otherFieldValue) {
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

	// SERIALIZATION BY PROXY

	private Object writeReplace() {
		return new SerializationProxy<T>(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required.");
	}

	private static class SerializationProxy<T> implements Serializable {

		private static final long serialVersionUID = -2167289103897309061L;

		private T optionalValue;

		private T otherFieldValue;

		public SerializationProxy(ClassUsingOptionalCorrectly<T> classUsingOptional) {
			optionalValue = classUsingOptional.optional.orElse(null);
			otherFieldValue = classUsingOptional.otherField;
		}

		private Object readResolve() {
			return new ClassUsingOptionalCorrectly<T>(optionalValue, otherFieldValue);
		}

	}

}
