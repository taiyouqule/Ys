package com.shenji.search.strategy;

import java.util.Comparator;

import com.shenji.web.bean.YsItemBean;

public class ScoreComparatorJson <T extends YsItemBean> implements Comparator<T> {
	public int compare(T o1, T o2) {
		YsItemBean p1 = (YsItemBean) o1;
		YsItemBean p2 = (YsItemBean) o2;
		if (p1.getScore() - p2.getScore() > 0)
			return -1;
		else if (p1.getScore() - p2.getScore() < 0)
			return 1;
		else
			return 0;
	}
}
