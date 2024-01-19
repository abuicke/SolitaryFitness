`@SmallTest`: <200ms, no external dependencies or resource access such as file system, network, or
databases. Tests that interact with hardware, make binder calls, or facilitate android instrumentation
should not use this annotation.

`@MediumTest`: <1000ms, focused on a very limited subset of components or a single component. Resource
access to the file system through well-defined interfaces like databases, or [android.content.Context].
Network access should be restricted. Long-running or blocking operations should be avoided. Use fake
objects instead.

`@LargeTest`: >1000ms, tests the app as a whole, e.g. UI tests. These tests fully participate in the
system and may make use of all resources such as databases, file systems, and networks.