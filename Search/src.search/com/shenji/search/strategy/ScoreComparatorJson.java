package com.shenji.search.strategy;

import java.util.Comparator;

import com.shenji.web.bean.ItemBean;

public class ScoreComparatorJson <T extends ItemBean> implements Comparator<T> {
	public int compare(T o1, T o2) {
		ItemBean p1 = (ItemBean) o1;
		ItemBean p2 = (ItemBean) o2;
		if (p1.getScore() - p2.getScore() > 0)
			return -1;
		else if (p1.getScore() - p2.getScore() < 0)
			return 1;
		else
			return 0;
	}
}
