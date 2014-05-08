/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.subway;

import java.util.*;

/**
 * <p>Title: 北京市地铁换乘（不支持Y自行线路）</p>
 * <p>Description: 票价2元</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-5-6 23:11
 */
public class SubwaySystemImpl extends AbstractSubwaySystem implements SubwaySystem {

	public List<TraversalRecord> traversal(List<TraversalRecord> traversalList, Map<String, SubwayStation> traversedStation, SubwayStation destination) {
		List<TraversalRecord> newTraversalList = new ArrayList<TraversalRecord>();
		Map<String, SubwayStation> currentTraversedStation = new HashMap<String, SubwayStation>();
		boolean found = false;
		for (TraversalRecord traversalRecord : traversalList) {
			// 获取当前站信息（双向链表，为方便输出采用后入式，性能无影响）
			SubwayStation currentStation = traversalRecord.getPath().get(traversalRecord.getPath().size() - 1);
			billing(traversalRecord, currentStation.getSubwayLine());

			// 如果已经找到
			if (currentStation.equals(destination)) {
				traversalRecord.setOptimal(true);
				newTraversalList.add(traversalRecord);// 差点扔了
				found = true;
				continue;
			}

			// 剪枝：其他线路如果已经找到最短路径则不再深入探索（存在已经深入探索的情况，在构造结果时排除）
			if (!found) {
				// 记录本轮深度优先遍历经过的地铁站，不再探索
				if (null == currentTraversedStation.get(currentStation.getName())) {
					currentTraversedStation.put(currentStation.getName(), currentStation);
				}

				newTraversalList.addAll(goNextStation(traversalRecord, traversedStation, currentStation));
			}
		}
		if (!found) {
			traversedStation.putAll(currentTraversedStation);
			newTraversalList = traversal(newTraversalList, traversedStation, destination);
		}
		return newTraversalList;
	}

	/**
	 * 获取指定地铁站下一步的可行探索路径
	 *
	 * @param traversalRecord
	 * @param traversedStation
	 * @param currentStation
	 * @return
	 */
	private List<TraversalRecord> goNextStation(TraversalRecord traversalRecord, Map<String, SubwayStation> traversedStation, SubwayStation currentStation) {
		List<TraversalRecord> nextTraversalList = new ArrayList<TraversalRecord>();

		// 当前线路的所有站点
		LinkedList<SubwayStation> currentSubwayLine = currentStation.getSubwayLine().getSubwayStations();
		int index = currentSubwayLine.indexOf(currentStation);
		ListIterator<SubwayStation> subwayStations = currentSubwayLine.listIterator(index);

		// 获取下一站信息
		SubwayStation nextStation = null;
		if (MoveDirectionEnum.PREVIOUS != traversalRecord.getDirection() && subwayStations.hasNext() && null != subwayStations.next() && subwayStations.hasNext()) {
			nextStation = subwayStations.next();
			// 没有途经过的地铁站才会加入
			if (null == traversedStation.get(nextStation.getName())) {
				TraversalRecord traversalRecord1 = (TraversalRecord) traversalRecord.clone();
				traversalRecord1.setDirection(MoveDirectionEnum.NEXT);
				traversalRecord1.getPath().add(nextStation);
				nextTraversalList.add(traversalRecord1);
			}
		}

		// 获取前一站信息
		subwayStations = currentSubwayLine.listIterator(index);
		SubwayStation preStation = null;
		if (MoveDirectionEnum.NEXT != traversalRecord.getDirection() && subwayStations.hasPrevious()) {
			preStation = subwayStations.previous();
			// 没有途经过的地铁站才会加入
			if (null == traversedStation.get(preStation.getName())) {
				TraversalRecord traversalRecord1 = (TraversalRecord) traversalRecord.clone();
				traversalRecord1.setDirection(MoveDirectionEnum.PREVIOUS);
				traversalRecord1.getPath().add(preStation);
				nextTraversalList.add(traversalRecord1);
			}
		}

		// 对换乘车站做特殊处理
		if (currentStation.isTransferStation()) {
			// 获取当前换乘站在所有可换乘线路上的副本
			List<SubwayStation> availableTransferStationList = transferStationMap.get(currentStation.getName());
			if (null != availableTransferStationList) {
				for (SubwayStation transferStation : availableTransferStationList) {
					// 排除当前副本
					if (transferStation.equals(currentStation)) {
						continue;
					}

					LinkedList<SubwayStation> currentSubwayLine1 = transferStation.getSubwayLine().getSubwayStations();
					int index1 = currentSubwayLine1.indexOf(transferStation);
					ListIterator<SubwayStation> subwayStations1 = currentSubwayLine1.listIterator(index1);

					// 获取前一站信息
					SubwayStation nextStation1 = null;
					if (subwayStations1.hasNext() && null != subwayStations1.next() && subwayStations1.hasNext()) {
						nextStation1 = subwayStations1.next();
						// 没有途经过的地铁站才会加入
						if (null == traversedStation.get(nextStation1.getName())) {
							TraversalRecord traversalRecord1 = (TraversalRecord) traversalRecord.clone();
							traversalRecord1.setDirection(MoveDirectionEnum.NEXT);
							traversalRecord1.getPath().add(nextStation1);
							nextTraversalList.add(traversalRecord1);
						}
					}

					// 获取上一站信息
					subwayStations1 = currentSubwayLine1.listIterator(index1);
					SubwayStation preStation1 = null;
					if (subwayStations1.hasPrevious()) {
						preStation1 = subwayStations1.previous();
						// 没有途经过的地铁站才会加入
						if (null == traversedStation.get(preStation1.getName())) {
							TraversalRecord traversalRecord1 = (TraversalRecord) traversalRecord.clone();
							traversalRecord1.setDirection(MoveDirectionEnum.PREVIOUS);
							traversalRecord1.getPath().add(preStation1);
							nextTraversalList.add(traversalRecord1);
						}
					}
				}
			}
		}
		return nextTraversalList;
	}

}
