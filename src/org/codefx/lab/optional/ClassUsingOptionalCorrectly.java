package org.codefx.lab.optional;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 * A class which has an field of type {@link Optional}. It customizes (de)serialization and is thus serializable.
 * <p>
 * Note that a class exposing an optional field via accessors smells of bad design. This is just for demo purposes!
 */
public class ClassUsingOptionalCorrectly<T> implements Serializable {

	// ATTRIBUTES

	private static final long serialVersionUID = 5258682920412938156L;

	private transient Optional<T> optional;

	private final T otherField;

	// CONSTRUCTION

	public ClassUsingOptionalCorrectly(T optionalValue, T otherFieldValue) {
		this.optional = Optional.ofNullable(optionalValue);
		this.otherField = otherFieldValue;
	}

	// SERIALIZATION

	/*
	 * Alternatively the Serialization Proxy Pattern could be used. See the SerializableOptional for how to do that.
	 */

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		// write the value contained in #optional
		out.writeObject(optional.orElse(null));
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// read the value and create an 'Optional' from it
		optional = Optional.ofNullable((T) in.readObject());
	}

	// ATTRIBUTE ACCESS

	public Optional<T> getOptional() {
		return optional;
	}

	public T getOtherField() {
		return otherField;
	}

}
