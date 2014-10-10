# Serialize Optional

This project provides the code samples for the CodeFX post about serializing `Optional`. It demonstrates how an `Optional` instance can be (de)serialized using different approaches.

For attributes it shows how it fails and how it is done correctly. It provides a class `SerializableOptional`, which wraps an optional for serialization (e.g. for RMI) and some classes using it. Finally, it provides a demo which ties it all together.

## Code

### Classes Using Optional

...

### SerializableOptional

A simple wrapper for an `Optional` which implements `Serializable`. It uses the Serialization Proxy Pattern (see *Effective Java* by Joshua Bloch, Item 78) to write and read the wrapped optional's value.

### Classes Using SerializableOptional

...

## Disclaimer

That `Optional` is not serializable is no mere oversight. A [discussion on the OpenJDK mailing list](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-September/thread.html#3186) gives some insights into the reasoning of the [JSR 335 Expert Group](https://www.jcp.org/en/jsr/detail?id=335), which was responsible for its inclusion in Java 8. Some quotes (out of temporal order):

Optional was only meant for return values, so there is no need to serialize it:

> [...] Optional should not be on any more than needed to support the optional-return idiom only. (Someone suggested maybe even renaming it to OptionalReturn to beat users over the head with this design orientation; perhaps we should have taken that suggestion.)  I get that lots of people want Optional to be something else. But, its not simply the case that the EG "forgot" to make it serializable; they explicitly chose not to.
> --[Brian Goetz](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-September/003274.html)

> Using Optional as a field type doesn't seem to offer much.
> Would the Optional field be wrapped in accessors? 
> If it's not wrapped, the result is that orElse(default) gets spread across the usage of the field which is a poor way to apply a default.
> If if it is wrapped, why not apply the default in the accessor or in the setter and not bother with Optional?
> --[Mike Duigou](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-September/003273.html)

> Optional is nice from the API point of view, but not if you store it in a field.
> If it's not something that should be stored in field, there is no point to make it serializable.
> --[Remi Forax](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-September/003203.html)

Making optional serializable promotes its usage in many places which might impact performance negatively:

> There is a good reason to not allow Optional to implement Serializable, it promotes a bad way to use Optional, at least from the VM point of view.
> [... performance considerations regarding cache misses and inlining ... ]
> --[Remi Forax](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-September/003199.html)

But even if the expert group would have deemed a serializable optional a good idea, there is still this argument against serializable classes in general:

> Making something in the JDK serializable makes a dramatic increase in our maintenance costs, because it means that the representation is frozen for all time.  This constrains our ability to evolve implementations in the future, and the number of cases where we are unable to easily fix a bug or provide an enhancement, which would otherwise be simple, is enormous.  So, while it may look like a simple matter of "implements Serializable" to you, it is more than that.  The amount of effort consumed by working around an earlier choice to make something serializable is staggering.
> --[Brian Goetz](http://mail.openjdk.java.net/pipermail/jdk8-dev/2013-September/003276.html)
