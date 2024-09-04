package com.miniproj.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SearchBookJSON {
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<NaverBook> items;
	
}
