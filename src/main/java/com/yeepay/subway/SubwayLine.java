/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.subway;

import java.util.LinkedList;

/**
 * <p>Title: 地铁线路</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-5-6 23:01
 */
public class SubwayLine {

	/**
	 * 地铁线名
	 */
	private String name;

	/**
	 * 站个数
	 */
	private int num;

	/**
	 * 基础票价
	 */
	private double basePrice;

	/**
	 * 超过指定站数开始加收调整价格
	 */
	private int smoothNum;

	/**
	 * 调整价格
	 */
	private double adjustPrice;

	/**
	 * 是否环形线
	 */
	private boolean loop;

	private LinkedList<SubwayStation> subwayStations;

	public SubwayLine(String name) {
		this.name = name;
		this.subwayStations = new LinkedList<SubwayStation>();
	}

	public SubwayLine(String name, int num, double basePrice, int smoothNum, double adjustPrice, boolean loop) {
		this.name = name;
		this.num = num;
		this.basePrice = basePrice;
		this.smoothNum = smoothNum;
		this.adjustPrice = adjustPrice;
		this.loop = loop;
		this.subwayStations = new LinkedList<SubwayStation>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public int getSmoothNum() {
		return smoothNum;
	}

	public void setSmoothNum(int smoothNum) {
		this.smoothNum = smoothNum;
	}

	public double getAdjustPrice() {
		return adjustPrice;
	}

	public void setAdjustPrice(double adjustPrice) {
		this.adjustPrice = adjustPrice;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public LinkedList<SubwayStation> getSubwayStations() {
		return subwayStations;
	}

	public void setSubwayStations(LinkedList<SubwayStation> subwayStations) {
		this.subwayStations = subwayStations;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SubwayLine that = (SubwayLine) o;

		if (!name.equals(that.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "SubwayLine{" +
				"name='" + name + '\'' +
				", num=" + num +
				", basePrice=" + basePrice +
				", smoothNum=" + smoothNum +
				", adjustPrice=" + adjustPrice +
				", loop=" + loop +
				'}';
	}
}
