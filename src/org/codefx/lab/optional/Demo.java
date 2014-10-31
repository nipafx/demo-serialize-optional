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
		demo.serializeTransformForSerializationProxy();
		demo.serializeTransformForCustomSerializedForm();
		demo.serializeTransformForAccess();

		print("");

		demo.callMethodsWithSerializableOptional();
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
	 * A class which contains an {@link Optional} can be serialized directly if the implementation customizes the
	 * serialization mechanism (e.g. with a serialization proxy).
	 */
	private void serializeClassUsingOptionalCorrectly() throws Exception {
		ClassUsingOptionalCorrectly<String> usingOptional =
				new ClassUsingOptionalCorrectly<>("optionalValue", "otherFieldValue");
		ClassUsingOptionalCorrectly<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'ClassUsingOptionalCorrectly' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	// -- in such a scenario SerializableOptional can be used in three different ways

	/**
	 * The class can implement the serialization proxy pattern and let the proxy have a field of type
	 * {@link SerializableOptional}.
	 */
	private void serializeTransformForSerializationProxy() throws Exception {
		TransformForSerializationProxy<String> usingOptional =
				new TransformForSerializationProxy<>("optionalValue", "otherFieldValue");
		TransformForSerializationProxy<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'TransformForSerializationProxy' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	/**
	 * The class can provide a custom serialized form and transform the {@link Optional} to a
	 * {@link SerializableOptional} on write and the other way on read.
	 */
	private void serializeTransformForCustomSerializedForm() throws Exception {
		TransformForCustomSerializedForm<String> usingOptional =
				new TransformForCustomSerializedForm<>("optionalValue", "otherFieldValue");
		TransformForCustomSerializedForm<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'TransformForCustomSerializedForm' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	/**
	 * Another possibility is to declare the field as {@link SerializableOptional} which means the default
	 * (de)serialization process works. Transformation to an {@link Optional} then has to happen on each access to the
	 * field.
	 */
	private void serializeTransformForAccess() throws Exception {
		TransformForAccess<String> usingOptional =
				new TransformForAccess<>("optionalValue", "otherFieldValue");
		TransformForAccess<String> deserializedUsingOptional = serializeAndDeserialize(usingOptional);
		print("The deserialized 'TransformForAccess' has the values \""
				+ deserializedUsingOptional.getOptional().get() + "\" / \""
				+ deserializedUsingOptional.getOtherField() + "\".");
	}

	// use 'SerializableOptional' in method signatures

	/**
	 * Shows how to quickly wrap and unwrap an {@link Optional} for RPC method calls which rely on serialization.
	 * <p>
	 * Note that {@link SearchAndLog}'s methods have {@link SerializableOptional} as argument and return type.
	 */
	private void callMethodsWithSerializableOptional() {
		SearchAndLog searchAndLog = new SearchAndLog();
		for (int id = 0; id < 7; id++) {
			// unwrap the returned optional using 'asOptional'
			Optional<String> searchResult = searchAndLog.search(id).asOptional();
			// wrap the optional using 'fromOptional'; if used often, this could be a static import
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

	private static class SearchAndLog {

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
