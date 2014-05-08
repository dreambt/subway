/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.subway;

/**
 * <p>Title: </p>
 * <p>Description: 描述</p>
 * <p>Copyright: Copyright (c)2011</p>
 * <p>Company: 易宝支付(YeePay)</p>
 *
 * @author baitao.ji
 * @version 0.1, 14-5-6 23:12
 */
public interface SubwaySystem {

	/**
	 * 计算
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	String calculate(String start, String end);

	/**
	 * 加载测试数据
	 *
	 * @param filePath 交通数据加载的绝对路径
	 */
	void loadData(String filePath);

}
