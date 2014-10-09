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

		print("");

		demo.serializeClassUsingOptional();
	}

	// serialize "simple" objects, i.e. ones which contain no further instances, to demo serialization in general

	private void serializeString() throws Exception {
		String someString = "a string";
		String deserializedString = serializeAndDeserialize(someString);
		print("The deserialized 'String' is \"" + deserializedString + "\".");
	}

	private void failSerializingOptional() throws Exception {
		try {
			Optional<String> someOptional = Optional.of("another string");
			Optional<String> deserializedOptional = serializeAndDeserialize(someOptional);
			print("The deserialized 'Optional' should have the value \"" + deserializedOptional.get() + "\".");
		} catch (NotSerializableException e) {
			print("Serialization of 'Optional' failed as expected.");
		}
	}

	private void serializeSerializableOptional() throws Exception {
		Optional<String> someOptional = Optional.of("another string");
		SerializableOptional<String> serializableOptional = SerializableOptional.fromOptional(someOptional);
		Optional<String> deserializedOptional = serializeAndDeserialize(serializableOptional).toOptional();
		print("The deserialized 'SerializableOptional' has the value \"" + deserializedOptional.get() + "\".");
	}

	// serialize "complex" objects, i.e. ones which in a real application would contain more references to other
	// instances, to demo serialization in practice 

	private void serializeClassUsingOptional() throws Exception {
		try {
			ClassUsingOptional<String> usingOptional =
					new ClassUsingOptional<String>("optionalValue", "otherAttributeValue");
			ClassUsingOptional<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
			print("The deserialized 'ClassUsingOptional' should have the values \""
					+ deserializedUsingOptional.getOptional().get() + "\" / \""
					+ deserializedUsingOptional.getOtherAttribute() + "\".");
		} catch (NotSerializableException e) {
			print("Serialization of 'ClassUsingOptional' failed as expected.");
		}
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

	/**
	 * Prints the specified text to the console.
	 *
	 * @param text
	 *            the text to print
	 */
	private static void print(String text) {
		System.out.println(text);
	}

}
