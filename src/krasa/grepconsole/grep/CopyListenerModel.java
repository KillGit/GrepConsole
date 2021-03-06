package krasa.grepconsole.grep;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class CopyListenerModel {
	private final boolean caseSensitive;
	private final boolean wholeLine;
	private final String expression;
	private final String unlessExpression;
	private final boolean regex;

	public CopyListenerModel(boolean caseSensitive, boolean wholeLine, boolean regex, String expression,
			String unlessExpression) {
		this.caseSensitive = caseSensitive;
		this.wholeLine = wholeLine;
		this.expression = expression;
		this.unlessExpression = unlessExpression;
		this.regex = regex;
	}

	public Matcher matcher() {
		Pattern unlessExpressionPattern = null;
		Pattern expressionPattern = null;
		if (!StringUtils.isBlank(expression)) {
			expressionPattern = Pattern.compile(expression, computeFlags());
		}
		if (!StringUtils.isBlank(unlessExpression)) {
			unlessExpressionPattern = Pattern.compile(unlessExpression, computeFlags());
		}
		return new Matcher(expressionPattern, unlessExpressionPattern, wholeLine);
	}

	public String getExpression() {
		return expression;
	}

	private int computeFlags() {
		int i = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		int j = regex ? 0 : Pattern.LITERAL;
		return i | j;
	}

	public static class Matcher {
		private final Pattern expressionPattern;
		private final Pattern unlessExpressionPattern;
		private final boolean wholeLine;

		public Matcher(Pattern expressionPattern, Pattern unlessExpressionPattern, boolean wholeLine) {
			this.expressionPattern = expressionPattern;
			this.unlessExpressionPattern = unlessExpressionPattern;
			this.wholeLine = wholeLine;
		}

		public boolean matches(String s) {
			if (!StringUtils.isEmpty(s)) {
				if (matchesPattern(expressionPattern, s) && !matchesPattern(unlessExpressionPattern, s)) {
					return true;
				}
			}
			return false;
		}

		private boolean matchesPattern(Pattern pattern, String matchedLine) {
			boolean matches = false;
			if (pattern != null) {
				if (matchedLine.endsWith("\n")) {
					matchedLine = matchedLine.substring(0, matchedLine.length() - 1);
				}
				if (wholeLine) {
					matches = pattern.matcher(matchedLine).matches();
				} else {
					matches = pattern.matcher(matchedLine).find();
				}
			}
			return matches;
		}

	}
}
