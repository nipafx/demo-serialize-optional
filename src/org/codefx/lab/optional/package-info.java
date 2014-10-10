/**
 * A demo on how to serialize {@link java.util.Optional Optional}.
 * <p>
 * The central class to make this easier is {@link org.codefx.lab.optional.SerializableOptional SerializableOptional}.
 * While {@link org.codefx.lab.optional.ClassUsingOptional ClassUsingOptional} shows how serialization fails for classes
 * with an optional attribute, {@link org.codefx.lab.optional.TransformOnAccess TransformOnAccess} and
 * {@link org.codefx.lab.optional.TransformOnSerialization TransformOnSerialization} demonstrate how
 * {@code SerializableOptional} mitigates the problem.
 * <p>
 * All of this is tied together in {@link org.codefx.lab.optional.Demo Demo}.
 */
package org.codefx.lab.optional;