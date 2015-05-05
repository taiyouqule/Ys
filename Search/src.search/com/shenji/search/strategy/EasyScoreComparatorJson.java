package com.shenji.search.strategy;

import java.util.Comparator;

import com.shenji.web.bean.EasyItemBean;

public class EasyScoreComparatorJson <T extends EasyItemBean> implements Comparator<T> {
	public int compare(T o1, T o2) {
		EasyItemBean p1 = (EasyItemBean) o1;
		EasyItemBean p2 = (EasyItemBean) o2;
		if (p1.getScore() - p2.getScore() > 0)
			return -1;
		else if (p1.getScore() - p2.getScore() < 0)
			return 1;
		else
			return 0;
	}
}