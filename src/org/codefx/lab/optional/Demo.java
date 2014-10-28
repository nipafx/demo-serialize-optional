package org.codefx.lab.optional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

/**
 * Demonstrates serialization with {@link Optional} and {@link SerializableOptional}.
 */
public class Demo {

	public static void main(String[] args) throws Exception {
		Demo demo = new Demo();

		demo.serializeString();
		demo.failSerializingOptional();
		demo.serializeEmptySerializableOptional();
		demo.serializeNonEmptySerializableOptional();

		print("");

		demo.failSerializingClassUsingOptional();
		demo.serializeClassUsingOptionalCorrectly();
		demo.serializeWithTransformOnSerialization();
		demo.serializeWithTransformOnAccess();

		print("");

		demo.callMethods();
	}

	// DEMO

	// serialize "simple" objects, i.e. ones which contain no further instances, to demo serialization in general

	/**
	 * To get started, serialize a string, deserialize it and print its value.
	 */
	private void serializeString() throws Exception {
		String someString = "a string";
		String deserializedString = serializeAndDeserialize(someString);
		print("The deserialized 'String' is \"" + deserializedString + "\".");
	}

	/**
	 * Try the same with an {@code Optional<String>}, which will fail as {@link Optional} is not {@link Serializable}.
	 */
	private void failSerializingOptional() throws Exception {
		try {
			Optional<String> someOptional = Optional.of("another string");
			Optional<String> deserializedOptional = serializeAndDeserialize(someOptional);
			print("The deserialized 'Optional' should have the value \"" + deserializedOptional.get() + "\".");
		} catch (NotSerializableException e) {
			print("Serialization of 'Optional' failed as expected.");
		}
	}

	/**
	 * Create a {@link SerializableOptional} from an empty {@link Optional} and (de)serialize it successfully.
	 */
	private void serializeEmptySerializableOptional() throws Exception {
		Optional<String> someOptional = Optional.empty();
		SerializableOptional<String> serializableOptional = SerializableOptional.fromOptional(someOptional);
		Optional<String> deserializedOptional = serializeAndDeserialize(serializableOptional).asOptional();
		print("The deserialized empty 'SerializableOptional' has no value: " + !deserializedOptional.isPresent() + ".");
	}

	/**
	 * Create a {@link SerializableOptional} from a nonempty {@link Optional} and (de)serialize it successfully.
	 */
	private void serializeNonEmptySerializableOptional() throws Exception {
		Optional<String> someOptional = Optional.of("another string");
		SerializableOptional<String> serializableOptional = SerializableOptional.fromOptional(someOptional);
		Optional<String> deserializedOptional = serializeAndDeserialize(serializableOptional).asOptional();
		print("The deserialized non-empty 'SerializableOptional' has the value \"" + deserializedOptional.get() + "\".");
	}

	// serialize "complex" objects, i.e. ones which in a real application would contain more references to other
	// instances, to demo serialization in practice 

	/**
	 * A class which contains an {@link Optional} can not be serialized directly if not properly implemented.
	 */
	private void failSerializingClassUsingOptional() throws Exception {
		try {
			ClassUsingOptional<String> usingOptional =
					new ClassUsingOptional<>("optionalValue", "otherFieldValue");
			ClassUsingOptional<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
			print("The deserialized 'ClassUsingOptional' should have the values \""
					+ deserializedUsingOptional.getOptional().get() + "\" / \""
					+ deserializedUsingOptional.getOtherField() + "\".");
		} catch (NotSerializableException e) {
			print("Serialization of 'ClassUsingOptional' failed as expected.");
		}
	}

	/**
	 * A class which contains an {@link Optional} can be serialized directly if the implementation provides customized
	 * (de)serialization methods.
	 */
	private void serializeClassUsingOptionalCorrectly() throws Exception {
		ClassUsingOptionalCorrectly<String> usingOptional =
				new ClassUsingOptionalCorrectly<>("optionalValue", "otherFieldValue");
		ClassUsingOptionalCorrectly<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'ClassUsingOptionalCorrectly' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	/**
	 * But it can customize the (de)serialization and transform the {@link Optional} to a {@link SerializableOptional}
	 * on write and the other way on read.
	 */
	private void serializeWithTransformOnSerialization() throws Exception {
		TransformOnSerialization<String> usingOptional =
				new TransformOnSerialization<>("optionalValue", "otherFieldValue");
		TransformOnSerialization<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'TransformOnSerialization' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	/**
	 * Another possibility is to declare the field as {@link SerializableOptional} which means the default
	 * (de)serialization process works. Transformation then has to happen on each access to the field.
	 */
	private void serializeWithTransformOnAccess() throws Exception {
		TransformOnAccess<String> usingOptional =
				new TransformOnAccess<>("optionalValue", "otherFieldValue");
		TransformOnAccess<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'TransformOnAccess' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	// use 'SerializableOptional' in method signatures

	/**
	 * Shows how to quickly wrap and unwrap an {@link Optional} for RPC method calls which rely on serialization.
	 */
	private void callMethods() {
		SearchAndRegister searchAndLog = new SearchAndRegister();
		for (int id = 0; id < 7; id++) {
			// unwrap the returned optional using 'asOptional'
			Optional<String> searchResult = searchAndLog.search(id).asOptional();
			// wrap the optional using 'fromOptional'
			searchAndLog.log(id, SerializableOptional.fromOptional(searchResult));
		}
	}

	// USABILITY

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

	// INNER CLASS FOR METHOD CALLS

	private static class SearchAndRegister {

		Random random = new Random();

		public SerializableOptional<String> search(int id) {
			boolean searchSuccessfull = random.nextBoolean();
			if (searchSuccessfull)
				return SerializableOptional.of("found something!");
			else
				return SerializableOptional.empty();
		}

		public void log(int id, SerializableOptional<String> item) {
			print("Search result for id " + id + ": " + item.asOptional().orElse("empty search result"));
		}

	}

}
