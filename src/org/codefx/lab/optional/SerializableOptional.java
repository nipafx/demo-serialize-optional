package org.codefx.lab.optional;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Convenience class to wrap an {@link Optional} for serialization.
 * <p>
 * Note that it does not provide any of the methods {@code Optional} has as its only goal is to enable serialization.
 * But it holds a reference to the {@code Optional} which was used to create it (can be accessed with
 * {@link #asOptional()}). Instances of this class are immutable.
 * <p>
 * There are two ways to use this class to serialize instances which have an optional attribute.
 * <p>
 * <h2>Transform For (De)Serialization</h2> The attribute can be declared as
 * {@code transient Optional<T> optionalAttribute}, which will exclude it from serialization.
 * <p>
 * The class then needs to implement custom (de)serialization methods {@code writeObject} and {@code readObject}. They
 * must transform the {@code optionalAttribute} to a {@code SerializableOptional} when writing the object and after
 * reading such an instance transform it back to an {@code Optional}.
 * <p>
 * <h3>Code Example</h3>
 * 
 * <pre>
 * private void writeObject(ObjectOutputStream out) throws IOException {
 * 	out.defaultWriteObject();
 * 	out.writeObject(
 * 		SerializableOptional.fromOptional(optionalAttribute));
 * }
 * 
 * private void readObject(ObjectInputStream in)
 * 	throws IOException, ClassNotFoundException {
 * 
 * 	in.defaultReadObject();
 * 	optionalAttribute =
 * 		((SerializableOptional<T>) in.readObject()).toOptional();
 * }
 * </pre>
 * <h2>Transform For Access</h2> The attribute can be declared as {@code SerializableOptional<T> optionalAttribute}.
 * This will include it in the (de)serialization process so it does not need to be customized.
 * <p>
 * But methods interacting with the attribute need to get an {@code Optional} instead. This can easily be done by
 * writing the accessor methods such that they transform the attribute on each access.
 * <p>
 * Note that {@link #asOptional()} simply returns the {@code Optional} which with this instance was created so no
 * constructor needs to be invoked.
 * <p>
 * <h3>Code Example</h3>
 * 
 * <pre>
 * public Optional<T> getOptionalAttribute() {
 * 	return optionalAttribute.asOptional();
 * }
 * 
 * private void setOptionalAttribute(Optional<T> optionalAttribute) {
 * 	this.optionalAttribute = SerializableOptional.fromOptional(optionalAttribute);
 * }
 * </pre>
 * 
 * @param <T>
 *            the type of the wrapped value
 */
public final class SerializableOptional<T extends Serializable> implements Serializable {

	// ATTRIBUTES

	private static final long serialVersionUID = -652697447004597911L;

	/**
	 * The wrapped {@link Optional}. Note that this attribute is transient so it will not be (de)serializd
	 * automatically.
	 */
	private transient Optional<T> optional;

	// CONSTRUCTION AND TRANSFORMATION

	private SerializableOptional(Optional<T> optional) {
		Objects.requireNonNull(optional, "The argument 'optional' must not be null.");
		this.optional = optional;
	}

	/**
	 * Creates a serializable optional from the specified optional.
	 * 
	 * @param optional
	 *            the {@link Optional} from which the serializable wrapper will be created
	 * @return a {@link SerializableOptional} which wraps the specified optional
	 */
	public static <T extends Serializable> SerializableOptional<T> fromOptional(Optional<T> optional) {
		return new SerializableOptional<>(optional);
	}

	/**
	 * Creates a serializable optional for the specified value by wrapping it in an {@link Optional}
	 * 
	 * @param value
	 *            the value which will be contained in the wrapped {@link Optional}; may be null
	 * @return a {@link SerializableOptional} which wraps the an optional for the specified value
	 */
	public static <T extends Serializable> SerializableOptional<T> ofNullable(T value) {
		return new SerializableOptional<>(Optional.ofNullable(value));
	}

	/**
	 * Returns the {@code Optional} instance which with this instance was created.
	 * 
	 * @return this instance as an {@link Optional}
	 */
	public Optional<T> asOptional() {
		return optional;
	}

	// SERIALIZATION

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		// write the value contained in #optional
		if (optional.isPresent())
			out.writeObject(optional.get());
		else
			out.writeObject(null);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		// read the value and create an 'Optional' from it
		@SuppressWarnings("unchecked")
		T optionalValue = (T) in.readObject();
		optional = Optional.ofNullable(optionalValue);
	}

}
