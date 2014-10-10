package org.codefx.lab.optional;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 * Demonstrates how to make a class serializable which has an {@link Optional} attribute.
 * <p>
 * Uses the "Transform On Serialization" approach as described in {@link SerializableOptional}.
 */
public class TransformOnSerialization<T extends Serializable> implements Serializable {

	// ATTRIBUTES

	private static final long serialVersionUID = -5721223145027479215L;

	private transient Optional<T> optional;

	private T otherAttribute;

	// CONSTRUCTION

	public TransformOnSerialization(T optionalValue, T otherAttributeValue) {
		this.optional = Optional.ofNullable(optionalValue);
		this.otherAttribute = otherAttributeValue;
	}

	// SERIALIZATION

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(SerializableOptional.fromOptional(optional));
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		optional = ((SerializableOptional<T>) in.readObject()).asOptional();
	}

	// ATTRIBUTE ACCESS

	public Optional<T> getOptional() {
		return optional;
	}

	public T getOtherAttribute() {
		return otherAttribute;
	}

}
