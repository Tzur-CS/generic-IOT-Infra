package dynamicjarloader;

import org.junit.Test;

public class DynamicJarLoaderTest {
    @Test
    public void testClassLoader() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        DynamicJarLoader check = new DynamicJarLoader("il.co.ilrd.command.Command");
        check.load("/home/tzur/git/java/projects/src/il/co/ilrd/command/test.jar");

    }

}
