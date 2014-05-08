/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.subway;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-5-8 11:02
 */
public abstract class AbstractSubwaySystem {

	/**
	 * 所有地铁线路
	 */
	protected Map<String, SubwayLine> trafficNetwork;

	/**
	 * 所有换乘车站(不同线路上的同一个换乘车站分别存储，方便查找同一线路上的前后地铁站)
	 */
	protected Map<String, List<SubwayStation>> transferStationMap;

	/**
	 * 所有车站
	 */
	protected Map<String, SubwayStation> subwayStationMap;

	public String calculate(final String start, final String end) {
		// 数据校验
		SubwayStation source = checkAndGetStation(start);
		SubwayStation destination = checkAndGetStation(end);

		// 初始化试探路径
		List<TraversalRecord> traversalList = new ArrayList<TraversalRecord>();
		TraversalRecord record = new TraversalRecord(source);
		traversalList.add(record);

		// 用于保存已经经过的地铁站
		Map<String, SubwayStation> traversedStation = new HashMap<String, SubwayStation>();

		// 开始遍历
		List<TraversalRecord> finalTraversalList = traversal(traversalList, traversedStation, destination);

		// 构造结果并返回
		return buildResult(finalTraversalList);
	}

	/**
	 * 广度优先遍历
	 *
	 * @param traversalList    试探路径
	 * @param traversedStation 已经经过的非换乘地铁站
	 * @param destination      目的地
	 */
	abstract List<TraversalRecord> traversal(List<TraversalRecord> traversalList, Map<String, SubwayStation> traversedStation, final SubwayStation destination);

	public void loadData(String filePath) {
		File inFile = new File(filePath);
		if (!inFile.isFile() || !inFile.canRead()) {
			System.out.println("耍猴呢？");
			System.exit(0);
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inFile));

			// 指定初始化大小，避免没有必要的resize操作（假设每条地铁线平均有15站）
			int totalSubwayLine = Integer.parseInt(br.readLine());
			trafficNetwork = new HashMap<String, SubwayLine>((int) (totalSubwayLine / 0.75));
			transferStationMap = new HashMap<String, List<SubwayStation>>((int) (totalSubwayLine * 2 / 0.75));
			subwayStationMap = new HashMap<String, SubwayStation>((int) (totalSubwayLine * 16 / 0.75));

			// 加载每一条地铁线路
			for (int i = 0; i < totalSubwayLine; ++i) {
				String[] subwayLineArgs = br.readLine().split(",");
				String lineName = subwayLineArgs[0];
				int num = Integer.parseInt(subwayLineArgs[1]);
				double basePrice = Double.parseDouble(subwayLineArgs[2]);
				int smoothNum = Integer.parseInt(subwayLineArgs[3]);
				double adjustPrice = Double.parseDouble(subwayLineArgs[4]);
				boolean loop = "loop".equals(subwayLineArgs[5]);

				// 如果该换线路已存在则直接取用，如果不存在则新建
				SubwayLine subwayLine = trafficNetwork.get(lineName);
				if (null == subwayLine) {
					subwayLine = new SubwayLine(lineName, num, basePrice, smoothNum, adjustPrice, loop);
					trafficNetwork.put(lineName, subwayLine);
				} else {
					subwayLine.setNum(num);
					subwayLine.setBasePrice(basePrice);
					subwayLine.setSmoothNum(smoothNum);
					subwayLine.setAdjustPrice(adjustPrice);
					subwayLine.setLoop(loop);
				}

				// 加载地铁线路上每一个地铁站
				for (int j = 0; j < num; ++j) {
					String[] subwayStationArgs = br.readLine().split(",");
					String stationName = subwayStationArgs[0];
					SubwayStation subwayStation = null;
					if (subwayStationArgs.length == 2) {
						String[] canTransferSubwayLine = subwayStationArgs[1].split(":");

						// 如果该换乘站已存在则直接取用，如果不存在则新建
						subwayStation = getOrAddTransferSubwayStation(subwayLine, stationName, canTransferSubwayLine.length);

						// 保存该站的换乘信息
						subwayStation.getCanTransferSubwayLines().add(subwayLine);
						for (String lineName1 : canTransferSubwayLine) {
							SubwayLine subwayLine1 = getOrAddSubwayLine(lineName1);
							subwayStation.getCanTransferSubwayLines().add(subwayLine1);
						}
					} else {
						// TODO 暂时没有考虑1号线与八通线并存的几个站
						subwayStation = new SubwayStation(subwayLine, stationName);
						subwayStationMap.put(stationName, subwayStation);
					}
					subwayLine.getSubwayStations().add(subwayStation);
				}
			}

			// 将换乘站加入所有站列表
			for (Map.Entry entry : transferStationMap.entrySet()) {
				String stationName = (String) entry.getKey();
				List<SubwayStation> subwayStations = (List<SubwayStation>) entry.getValue();
				if (null != subwayStations) {
					for (SubwayStation station : subwayStations) {
						subwayStationMap.put(stationName, station);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件木找着");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("未知 IO 异常：" + e.getMessage());
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			System.out.println("地图有问题啊，不怕地铁出轨？");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("站数或者价格有问题吧");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("未知错误：" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * 计算当前站费用
	 *
	 * @param traversalRecord 当前遍历路径
	 * @param subwayLine      当前地铁线
	 * @return
	 */
	public void billing(TraversalRecord traversalRecord, final SubwayLine subwayLine) {
		double totalPrice = 0;
//		traversalRecord.setCount(traversalRecord.getCount() + 1);
//		if (1 == traversalRecord.getCount()) {
//			totalPrice += subwayLine.getBasePrice();
//		} else if (traversalRecord.getCount() > subwayLine.getSmoothNum()) {
//			totalPrice += subwayLine.getAdjustPrice();
//		}
		int count = traversalRecord.getPath().size();
		if (1 == count) {
			totalPrice = subwayLine.getBasePrice();
		} else if (count > subwayLine.getSmoothNum()) {
			totalPrice += subwayLine.getAdjustPrice();
		}
		traversalRecord.setPrice(traversalRecord.getPrice() + totalPrice);
	}

	private String buildResult(final List<TraversalRecord> traversalList) {
		StringBuilder sb = new StringBuilder();
		for (TraversalRecord traversalRecord : traversalList) {
			if (traversalRecord.isOptimal()) {
				sb.append(traversalRecord.toString()).append(";");
			}
		}
		if (sb.lastIndexOf(";") == sb.length() - 1) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return sb.toString();
		}
	}

	/**
	 * 检查用户输入并返回地铁站信息
	 *
	 * @param stationName 站名
	 * @return
	 */
	private SubwayStation checkAndGetStation(String stationName) {
		SubwayStation station = subwayStationMap.get(stationName);
		if (null == station) {
			System.out.println("错误的地铁站名称");
			System.exit(0);
		}
		return station;
	}

	/**
	 * 根据 lineName 从缓存中获取地铁线
	 *
	 * @param lineName 地铁线
	 */
	private SubwayLine getOrAddSubwayLine(String lineName) {
		SubwayLine subwayLine = trafficNetwork.get(lineName);
		if (null == subwayLine) {
			subwayLine = new SubwayLine(lineName);
			trafficNetwork.put(lineName, subwayLine);
		}
		return subwayLine;
	}

	/**
	 * 根据 stationName 从缓存中获取换乘站
	 *
	 * @param subwayLine  所属地铁线
	 * @param stationName 站名
	 * @param size        该换乘站可换乘的地铁线数，用于优化Map
	 */
	private SubwayStation getOrAddTransferSubwayStation(SubwayLine subwayLine, String stationName, int size) {
		List<SubwayStation> subwayStationList = transferStationMap.get(stationName);
		if (null != subwayStationList) {
			for (SubwayStation station : subwayStationList) {
				if (station.getSubwayLine().equals(subwayLine)) {
					return station;
				}
			}
		} else {
			subwayStationList = new ArrayList<SubwayStation>();
			transferStationMap.put(stationName, subwayStationList);
		}

		SubwayStation subwayStation = new SubwayStation(subwayLine, stationName, true, size);
		transferStationMap.get(stationName).add(subwayStation);
		return subwayStation;
	}

}
