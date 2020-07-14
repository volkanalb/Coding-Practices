package com.vvv.stack05;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> book = new ArrayList<>();
		book.add("This is line 1");
		book.add("This is line 2");
		book.add("Happy ending. This is end");
		
		Integer wordCount = book.stream(). 				//aka line stream
				map(line -> line.split("\\s").length).	//for each line split into words, then map into word count
				reduce(0,(sum,next)->sum+next);			//sum of words
		System.out.println(wordCount);
	}

}
