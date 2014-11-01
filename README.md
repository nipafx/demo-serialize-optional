# Serialize Optional

This project provides the code samples for the [CodeFX post about serializing `Optional`](http://blog.codefx.org/jdk/serialize-optional/). It demonstrates how an `Optional` instance can be (de)serialized using different approaches.

For fields it shows how it fails and how it is done correctly. It provides a class `SerializableOptional`, which wraps an `Optional` for serialization (e.g. for RMI) and some classes using it. Finally, it provides a demo which ties it all together.

## Code

### Classes Using Optional

The classes [`ClassUsingOptional`](https://github.com/CodeFX-org/demo-serialize-optional/blob/master/src/org/codefx/lab/optional/ClassUsingOptional.java) and [`ClassUsingOptionalCorrectly`](https://github.com/CodeFX-org/demo-serialize-optional/blob/master/src/org/codefx/lab/optional/ClassUsingOptionalCorrectly.java) each contain a field with type `Optional`. While the first fails at serialization (using the default serialization mechanism), the second does not (because it properly implements the [serialization proxy pattern](http://blog.codefx.org/design/patterns/serialization-proxy-pattern/)).

### SerializableOptional

The [`SerializableOptional`](https://github.com/CodeFX-org/demo-serialize-optional/blob/master/src/org/codefx/lab/optional/SerializableOptional.java) is a simple wrapper for an `Optional` which implements `Serializable`.

### Classes Using SerializableOptional

The comment on `SerializableOptional` describes different approaches on how to use it to serialize a class with an optional field. The classes `TransformFor...` show examples for these approaches.

## Disclaimer

That `Optional` is not serializable is no mere oversight. The reasons are described in all detail in a [blog post from CodeFX](http://blog.codefx.org/jdk/dev/why-isnt-optional-serializable/). The gist is:

* `Optional` was only meant for return values, so there is no need to serialize it.
* Making a class serializable heavily reduces the flexibility to change it later.
* `Optional` is a [value-based class](docs.oracle.com/javase/8/docs/api/java/lang/doc-files/ValueBased.html) and those should not be serialized.
