import junit.framework.TestCase;

public class Misc extends TestCase {

    public void testExtension() {
        String path = "/resources/index.jpg";
        int index = path.indexOf("\\?");
        String parsedPath = path;
        String extension = "";
        if (index > 0) {
            parsedPath = path.substring(0, index);
        }

        int i = parsedPath.lastIndexOf('.');
        if (i > 0) {
            extension = parsedPath.substring(i + 1);
        }

        System.out.println(extension);

    }
}
