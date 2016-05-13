package langusta3.pattern;

import langusta3.pattern.rule.AbstractRule;
import langusta3.pattern.condition.AbstractCondition;
import java.util.ArrayList;
import java.util.List;
import langusta3.core.SpelledWord;
import langusta3.pattern.condition.Equal;
import langusta3.pattern.condition.GrammarClass;
import langusta3.pattern.rule.Append;
import langusta3.pattern.rule.Chop;
import langusta3.spelling.iSpeller;

/** Generator contains common setting used in patterns
 *
 *  Condition, rules and filters can be defined outside this package. So we
 *  need method to add them into application and reuse this info for all
 *  loaded patterns.
 *
 * @author marx
 */
public class Generator {
    private iSpeller speller;
    protected List<AbstractRule> rules;
    protected List<AbstractCondition> conditions;
    protected List<AbstractFilter> filters;
    protected List<Pattern> patterns;
    protected List<AbstractFilter> globalFilters;

    public Generator (iSpeller speller) {
        this.speller = speller;
        this.rules = new ArrayList<AbstractRule>();
        this.conditions = new ArrayList<AbstractCondition>();
        this.filters = new ArrayList<AbstractFilter>();
        this.patterns = new ArrayList<Pattern>();
        this.globalFilters = new ArrayList<AbstractFilter>();

        conditions.add(new Equal("equal"));
        conditions.add(new GrammarClass("class"));

        rules.add(new Append("append"));
        rules.add(new Chop("chop"));
    }

    public iSpeller getSpeller() {
        return speller;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public Pattern getPattern(String id) {
        for (Pattern p : this.patterns) {
            if (p.getID().equals(id)) {
                return p;
            }
        }

        return null;
    }

    public void addPattern(Pattern pattern) {
        /** @todo: kontrola na unikátne ID **/
        this.patterns.add(pattern);
    }

    public List<Pattern> getPossiblePatterns(SpelledWord word) {
        List<Pattern> result = new ArrayList<Pattern>();

        for (Pattern p : this.getPatterns()) {
            if (p.testWordAsLemma(word)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<AbstractCondition> getConditions() {
        return conditions;
    }

    public List<AbstractRule> getRules() {
        return rules;
    }

    public List<AbstractFilter> getFilters() {
        return filters;
    }

    public List<AbstractFilter> getGlobalFilters() {
        return globalFilters;
    }
}
