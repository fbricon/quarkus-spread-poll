package foo.bar.poller;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativePollResourceIT extends PollResourceTest {

    // Execute the same tests but in native mode.
}