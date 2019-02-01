package langusta3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import langusta3.core.SpelledWord;
import langusta3.cz.CzechXMLGenerator;
import langusta3.pattern.FormInfoTO;
import langusta3.pattern.Pattern;
import langusta3.sk.SlovakXMLGenerator;
import langusta3.spelling.NaiveSpeller;
import langusta3.xml.XMLAlphabet;
import langusta3.xml.XMLException;
import langusta3.xml.XMLGenerator;

/**
 *
 * @author marx
 */
public class ExportAjka {

    public static void main(String[] args) throws XMLException, FileNotFoundException, IOException {
        String czNounPattern = args[0];
        String czInputFile = args[1];

        NaiveSpeller ns = new NaiveSpeller(new XMLAlphabet("cz"));
        XMLGenerator xg = new CzechXMLGenerator(ns, new FileInputStream(czNounPattern));
        xg.load();

        BufferedReader input = new BufferedReader(new FileReader(czInputFile));

        String line;
        while ((line = input.readLine()) != null) {
            line = line.trim();
            String[] w = line.split(":");
            if (w.length != 2) {
                continue;
            }
            String[] patterns = w[1].split(";");

            Set<FormInfoTO> sfi = new HashSet<FormInfoTO>();

            for (String pattern : patterns) {
                Pattern p = xg.getPattern(pattern);

                if (p == null) {
                    System.err.println("Pattern " + pattern + " not found");
                    continue;
                }

                for (SpelledWord s : ns.wordSpelling(w[0])) {
                    Map<String, SpelledWord> base;

                    base = p.getBases(s);

                    if (base != null) {
                        List<FormInfoTO> l = p.getWordForms(p.getBases(s));

                        for (FormInfoTO f : l) {
                            sfi.add(f);
                        }
                    }
                }

            }

            Set<FormInfoTO> toRemove = new HashSet<FormInfoTO>();
            Set<FormInfoTO> toAppend = new HashSet<FormInfoTO>();
            
            for (FormInfoTO f : sfi) {
                if (f.getPattern().endsWith("_Z")) {
                    if (f.getTag().startsWith("+")) {
                        FormInfoTO x = new FormInfoTO();
                        x.setLemma(f.getLemma());
                        x.setTag(f.getTag().substring(1));
                        x.setWord(f.getWord());
                        toRemove.add(f);
                        toAppend.add(x);                      
                    } else if (f.getTag().startsWith("-")) {
                        FormInfoTO x = new FormInfoTO();
                        x.setLemma(f.getLemma());
                        x.setTag(f.getTag().substring(1));
                        x.setWord(f.getWord());
                        toRemove.add(x);
                        toRemove.add(f);
                    }
                }
            }

            for (FormInfoTO f : toAppend) {
                sfi.remove(f);
                sfi.add(f);
            }

            for (FormInfoTO f: toRemove) {
                sfi.remove(f);
            }
            
            for (FormInfoTO f : sfi) {
                System.out.println(f.getLemma().toString() + ":" + f.getWord() + ":" + f.getTag());
            }
        }
    }
}
