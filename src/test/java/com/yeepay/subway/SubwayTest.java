package com.yeepay.subway; /**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * <p>Title: 五子棋</p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-4-12 21:11
 */
@RunWith(Parameterized.class)
public class SubwayTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubwayTest.class);

	private static SubwaySystem subwaySystem;

	@Test
	public void testName() throws Exception {
		String traversalResult = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("calculateTotal");
		for (int i = 0; i < 1000; ++i) {
//			StopWatch stopWatch1 = new StopWatch();
//			stopWatch1.start("calculate");
			traversalResult = subwaySystem.calculate(start, end);
//			stopWatch1.stop();
//			LOGGER.debug("calculate used {} ms", stopWatch1.getTotalTimeMillis());
		}
		stopWatch.stop();
		LOGGER.debug("calculate total used {} ms", stopWatch.getTotalTimeMillis());
		assertEquals(expected, traversalResult);
	}

	// 起始站点
	private String start;

	// 目的站点
	private String end;

	// 期望输出内容
	private String expected;

	private static String filePath;

	@Parameterized.Parameters
	public static Collection prepareData() {
		if (null == filePath) {
			filePath = ClassLoader.getSystemClassLoader().getResource("subway/subway.txt").getPath();
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		}

		// 构建北京地铁
		subwaySystem = new SubwaySystemImpl();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start("loadData");
		subwaySystem.loadData(filePath + "subway.txt");
		stopWatch.stop();
		LOGGER.debug("loadData used {} ms", stopWatch.getTotalTimeMillis());

		Object[][] object = new Object[5][3];
		for (int i = 0; i < 5; ++i) {
			File inFile = new File(filePath + "subway" + (i + 1) + ".in");
			File outFile = new File(filePath + "subway" + (i + 1) + ".out");
			BufferedReader br = null;
			BufferedReader br2 = null;
			try {
				br = new BufferedReader(new FileReader(inFile));
				br2 = new BufferedReader(new FileReader(outFile));

				if (inFile.canRead() && outFile.canRead()) {
					String[] args = br.readLine().split(",");
					object[i][0] = args[0];
					object[i][1] = args[1];
					object[i][2] = br2.readLine();
				}
			} catch (FileNotFoundException e) {
				System.out.println("文件木找着");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("未知 IO 异常：" + e.getMessage());
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
				try {
					if (null != br2) {
						br2.close();
					}
				} catch (Exception e) {
					// do nothing
				}
			}
		}
		return Arrays.asList(object);
	}

	public SubwayTest(String start, String end, String expected) {
		this.start = start;
		this.end = end;
		this.expected = expected;
	}

}
