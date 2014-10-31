/**
 * A demo on how to serialize {@link java.util.Optional Optional}.
 * <p>
 * While {@link org.codefx.lab.optional.ClassUsingOptional ClassUsingOptional} shows how serialization fails for naive
 * classes with an optional field, {@link org.codefx.lab.optional.ClassUsingOptionalCorrectly
 * ClassUsingOptionalCorrectly} has customized (de)serialization and handles the case well.
 * <p>
 * Sometimes, a more explicit approach might be necessary (e.g. for RMI) or at least useful (e.g. for the Serialization
 * Proxy Pattern) and for such cases the class {@link org.codefx.lab.optional.SerializableOptional SerializableOptional}
 * exists. {@link org.codefx.lab.optional.TransformForSerializationProxy TransformForSerializationProxy},
 * {@link org.codefx.lab.optional.TransformForCustomSerializedForm TransformForCustomSerializedForm} and
 * {@link org.codefx.lab.optional.TransformForAccess TransformForAccess} demonstrate how {@code SerializableOptional}
 * mitigates the problem.
 * <p>
 * All of this is tied together in {@link org.codefx.lab.optional.Demo Demo}.
 */
package org.codefx.lab.optional;