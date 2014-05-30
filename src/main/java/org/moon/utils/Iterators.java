package org.moon.utils;

import java.util.Iterator;

/**
 * @author Gavin
 * @date May 29, 2014
 */
public class Iterators {
	
	public  static interface CustomerHandler<T>{
		void handle(T t);
	}
	
	public static <T> void  forEach(Iterator<T> it,CustomerHandler<T> customerHandler){
		while(it.hasNext()){
			customerHandler.handle(it.next());
		}
	}
	
}
