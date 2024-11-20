package hx.nine.eleven.commons.utils;

import java.math.BigDecimal;

/**
 * BegDecimal 计算
 * 
 * @author wml
 *
 */
public class BigDecimalUtil {

	// +
	public static BigDecimal add(double d1, double d2, int scale, int roundMode) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.add(bd2).setScale(scale, roundMode);
	}

	// -
	public static BigDecimal sub(double d1, double d2, int scale, int roundMode) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.subtract(bd2).setScale(scale, roundMode);
	}

	// *
	public static BigDecimal mul(double d1, double d2, int scale, int roundMode) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.multiply(bd2).setScale(scale, roundMode);
	}

	/* ??/ */
	public static BigDecimal div(double d1, double d2, int scale, int roundMode) throws Exception {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		// d=bd1.divide(bd2).setScale(scale, roundMode).doubleValue();
		return bd1.divide(bd2, scale, roundMode);
	}

	// +
	public static BigDecimal add(String d1, String d2, int scale, int roundMode) {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.add(bd2).setScale(scale, roundMode);
	}

	// -
	public static BigDecimal sub(String d1, String d2, int scale, int roundMode) {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.subtract(bd2).setScale(scale, roundMode);
	}

	// *
	public static BigDecimal mul(String d1, String d2, int scale, int roundMode) {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.multiply(bd2).setScale(scale, roundMode);
	}

	/* ??/ */
	public static BigDecimal div(String d1, String d2, int scale, int roundMode) throws Exception {
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		// d=bd1.divide(bd2).setScale(scale, roundMode).doubleValue();
		return bd1.divide(bd2, scale, roundMode);
	}

	public static BigDecimal format(String d1, int scale, int roundMode) throws Exception {
		if (StringUtils.isEmpty(d1)){
			return new BigDecimal(0);
		}

		BigDecimal bd1 = new BigDecimal(d1);
		return bd1.setScale(scale, roundMode);
	}

	public static BigDecimal format(String d1) throws Exception {
		return format(d1, 2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal format(BigDecimal d1) throws Exception {
		return d1.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param scale
	 * @param roundMode
	 * @return   做+法  返回bigdecimal
	 */
	public static BigDecimal add(BigDecimal d1, BigDecimal d2, int scale, int roundMode) {

		return d1.add(d2).setScale(scale, roundMode);
	}

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param scale
	 * @param roundMode
	 * @return  做-法  返回bigdecimal
	 */
	public static BigDecimal sub(BigDecimal d1, BigDecimal d2, int scale, int roundMode) {
		return d1.subtract(d2).setScale(scale, roundMode);
	}

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param scale
	 * @param roundMode
	 * @return  做*法  返回bigdecimal
	 */
	public static BigDecimal mul(BigDecimal d1, BigDecimal d2, int scale, int roundMode) {
		return d1.multiply(d2).setScale(scale, roundMode);
	}

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param scale
	 * @param roundMode
	 * @return  做 / 法  返回bigdecimal
	 * @throws Exception
	 */
	public static BigDecimal div(BigDecimal d1, BigDecimal d2, int scale, int roundMode) throws Exception {
		return d1.divide(d2, scale, roundMode);
	}

	public static void main(String[] args) throws Exception {
		BigDecimal totalpage = div("203","50",0, BigDecimal.ROUND_UP);
		System.out.println(totalpage.intValue());
	}
}
