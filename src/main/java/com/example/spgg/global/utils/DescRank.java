package com.example.spgg.global.utils;

import com.example.spgg.domain.api.apimodel.attr.rank.Entry;

import java.util.Comparator;

public class DescRank implements Comparator<Entry>{

	@Override
	public int compare(Entry o1, Entry o2) {
		
		long o1point = o1.getLeaguePoints();
		long o2point = o2.getLeaguePoints();
		
        if (o1point > o2point) {  
            return -1;  
        } else if (o1point < o2point) {  
            return 1;  
        } else {  
            return 0;  
        }  
	}
	
}
