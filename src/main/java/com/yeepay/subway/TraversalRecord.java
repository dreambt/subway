/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.subway;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-5-6 23:13
 */
public class TraversalRecord implements Cloneable {

	/**
	 * 最优解
	 */
	private boolean optimal;

	/**
	 * 总价
	 */
	private double price;

	/**
	 * 当前地铁线上的第几站
	 */
//	private int count;

	/**
	 * 路径
	 */
	private List<SubwayStation> path;

	/**
	 * 移动方向
	 */
	private MoveDirectionEnum direction;

	public TraversalRecord(SubwayStation source) {
		this.path = new LinkedList<SubwayStation>();
		this.path.add(source);
	}

	/**
	 * just for clone
	 *
	 * @param price
	 * @param direction
	 */
	private TraversalRecord(double price, MoveDirectionEnum direction) {
		this.price = price;
		this.direction = direction;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<SubwayStation> getPath() {
		return path;
	}

	public void setPath(List<SubwayStation> path) {
		this.path = path;
	}

	public MoveDirectionEnum getDirection() {
		return direction;
	}

	public void setDirection(MoveDirectionEnum direction) {
		this.direction = direction;
	}

	public boolean isOptimal() {
		return optimal;
	}

	public void setOptimal(boolean optimal) {
		this.optimal = optimal;
	}

//	public int getCount() {
//		return count;
//	}

//	public void setCount(int count) {
//		this.count = count;
//	}

	@Override
	protected Object clone() {
		TraversalRecord traversalRecord = new TraversalRecord(this.price, this.direction);
		traversalRecord.setPath(new LinkedList<SubwayStation>(this.path));
		return traversalRecord;
	}

	@Override
	public String toString() {
		if (null == path || 0 == path.size()) {
			return "null";
		}

		StringBuilder sb = new StringBuilder(Math.round(price) + ":");
		for (SubwayStation ss : path) {
			sb.append(ss.getName()).append(",");
		}
		if (sb.length() > 0) {
			return sb.subSequence(0, sb.length() - 1).toString();
		} else {
			return sb.toString();
		}
	}

}
