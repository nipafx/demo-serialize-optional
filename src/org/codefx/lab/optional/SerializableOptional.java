package org.codefx.lab.optional;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public final class SerializableOptional<T> implements Serializable {

	private static final long serialVersionUID = -652697447004597911L;

	private boolean present;

	private T value;

	private SerializableOptional() {
		present = false;
	}

	private SerializableOptional(T value) {
		Objects.requireNonNull(value, "The argument 'value' must not be null.");
		this.present = true;
		this.value = value;
	}

	public static <T> SerializableOptional<T> fromOptional(Optional<T> optional) {
		if (optional.isPresent())
			return new SerializableOptional<T>(optional.get());
		else
			return new SerializableOptional<>();
	}

	public Optional<T> toOptional() {
		return Optional.ofNullable(value);
	}

}
