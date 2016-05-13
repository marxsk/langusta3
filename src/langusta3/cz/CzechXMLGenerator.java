package langusta3.cz;

import java.io.InputStream;
import langusta3.spelling.iSpeller;
import langusta3.xml.XMLException;
import langusta3.xml.XMLGenerator;

/**
 *
 * @author marx
 */
public class CzechXMLGenerator extends XMLGenerator {

    public CzechXMLGenerator(iSpeller speller, InputStream is) throws XMLException {
        super(speller, is);

        globalFilters.add(new DeTeNeRe("detenere"));
    }
}
