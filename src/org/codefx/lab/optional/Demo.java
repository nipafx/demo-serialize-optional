package org.codefx.lab.optional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class Demo {

	public static void main(String[] args) throws Exception {
		Demo demo = new Demo();

		demo.serializeString();
		demo.failSerializingOptional();
		demo.serializeSerializableOptional();
	}

	private void serializeString() throws Exception {
		String someString = "a string";
		String deserializedString = serializeAndDeserialize(someString);
		System.out.println("The deserialized string is \"" + deserializedString + "\".");
	}

	private void failSerializingOptional() throws Exception {
		try {
			Optional<String> someOptional = Optional.of("another string");
			Optional<String> deserializedOptional = serializeAndDeserialize(someOptional);
			System.out.println("The deserialized Optional should have the value \"" + deserializedOptional.get()
					+ "\".");
		} catch (NotSerializableException e) {
			System.out.println("Serialization of Optional failed as expected.");
		}
	}

	private void serializeSerializableOptional() throws Exception {
		Optional<String> someOptional = Optional.of("another string");
		SerializableOptional<String> serializableOptional = SerializableOptional.fromOptional(someOptional);
		Optional<String> deserializedOptional = serializeAndDeserialize(serializableOptional).toOptional();
		System.out.println("The deserialized SerializableOptional has the value \"" + deserializedOptional.get());
	}

	/**
	 * Serializes the specified instance to disk. Then deserializes the file and returns the deserialized value.
	 * 
	 * @param serialized
	 *            the instance to be serialized
	 * @return the deserialized instance
	 * @throws Exception
	 *             if (de)serialization fails
	 */
	private static <T> T serializeAndDeserialize(T serialized) throws Exception {
		File serializeFile = new File("_serialized");
		// serialize
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializeFile))) {
			out.writeObject(serialized);
		}
		// deserialize
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializeFile))) {
			@SuppressWarnings("unchecked")
			T deserialized = (T) in.readObject();
			return deserialized;
		}
	}
}
