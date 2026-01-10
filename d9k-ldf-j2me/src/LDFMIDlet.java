import jmunit.framework.cldc11.Test;
import jmunit.framework.cldc11.TestRunner;
import com.foo.Suite;

public class LDFMIDlet extends TestRunner {

    private Test nestedTest;

    public LDFMIDlet() {
        super(3000);
        this.nestedTest = new Suite();
    }

    protected Test getNestedTest() {
        return this.nestedTest;
    }

}
