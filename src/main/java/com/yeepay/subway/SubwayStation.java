/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.subway;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title: 地铁站</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-5-6 23:00
 */
public class SubwayStation implements Cloneable {

	/**
	 * 所属地铁线（换乘车站随机指定一个用于获取起始票价）
	 */
	private SubwayLine subwayLine;

	/**
	 * 站名
	 */
	private String name;

	/**
	 * 是否换乘站
	 */
	private boolean transferStation = false;

	/**
	 * 可换乘线路
	 */
	private Set<SubwayLine> canTransferSubwayLines;

	public SubwayStation(SubwayLine subwayLine, String name) {
		this.subwayLine = subwayLine;
		this.name = name;
		this.canTransferSubwayLines = new HashSet<SubwayLine>();
	}

	public SubwayStation(SubwayLine subwayLine, String name, boolean transferStation, int size) {
		this.subwayLine = subwayLine;
		this.name = name;
		this.transferStation = transferStation;
		this.canTransferSubwayLines = new HashSet<SubwayLine>((int) (size / 0.75));
	}

	public SubwayStation(SubwayLine subwayLine, String name, boolean transferStation, Set<SubwayLine> canTransferSubwayLines) {
		this.subwayLine = subwayLine;
		this.name = name;
		this.transferStation = transferStation;
		this.canTransferSubwayLines = canTransferSubwayLines;
	}

	public SubwayLine getSubwayLine() {
		return subwayLine;
	}

	public void setSubwayLine(SubwayLine subwayLine) {
		this.subwayLine = subwayLine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTransferStation() {
		return transferStation;
	}

	public void setTransferStation(boolean transferStation) {
		this.transferStation = transferStation;
	}

	public Set<SubwayLine> getCanTransferSubwayLines() {
		return canTransferSubwayLines;
	}

	public void setCanTransferSubwayLines(Set<SubwayLine> canTransferSubwayLines) {
		this.canTransferSubwayLines = canTransferSubwayLines;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SubwayStation station = (SubwayStation) o;

		if (name != null ? !name.equals(station.name) : station.name != null) return false;
		if (subwayLine != null ? !subwayLine.equals(station.subwayLine) : station.subwayLine != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = subwayLine != null ? subwayLine.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "SubwayStation{" +
				"subwayLine=" + subwayLine.getName() +
				", name='" + name + '\'' +
				", transferStation=" + transferStation +
				", canTransferSubwayLines=" + canTransferSubwayLines +
				'}';
	}
}
