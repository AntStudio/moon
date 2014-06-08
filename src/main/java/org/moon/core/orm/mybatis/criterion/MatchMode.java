package org.moon.core.orm.mybatis.criterion;

/**
 * 
 * @author Gavin
 *
 */
public enum MatchMode {

	/**
	 * Match the entire string to the pattern
	 */
	EXACT {
		@Override
		public String toMatchString(String pattern) {
			return pattern;
		}
	},

	/**
	 * Match the start of the string to the pattern
	 */
	START {
		@Override
		public String toMatchString(String pattern) {
			return pattern + '%';
		}
	},

	/**
	 * Match the end of the string to the pattern
	 */
	END {
		@Override
		public String toMatchString(String pattern) {
			return '%' + pattern;
		}
	},

	/**
	 * Match the pattern anywhere in the string
	 */
	ANYWHERE {
		@Override
		public String toMatchString(String pattern) {
			return '%' + pattern + '%';
		}
	};
	public abstract String toMatchString(String pattern);
}
