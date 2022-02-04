package com.hzsh.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 */
@Service
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static final long ONE_MILLIS = 1000;
	private static final long ONE_MINUTE = 60;
	private static final long ONE_HOUR = 3600;
	private static final long ONE_DAY = 86400;
	private static final long ONE_MONTH = 2592000;
	private static final long ONE_YEAR = 31104000;
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM", "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMM" };

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 * "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm",
	 * "yyyy.MM", "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMM"}
	 */
	public static Date parseDate(String str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str, parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String formatCurrentDate() {
		return formatCurrentDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatCurrentDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (pattern != null) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 获取当前时间戳（yyyyMMddHHmmss）
	 *
	 * @return nowTimeStamp
	 */
	public static long getCurrentTimestamp() {
		long nowTimeStamp = Long.parseLong(getCurrentTimestampStr());
		return nowTimeStamp;
	}

	/**
	 * 获取当前时间戳（yyyyMMddHHmmss）
	 *
	 * @return
	 */
	public static String getCurrentTimestampStr() {
		return formatDate(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 获取Unix时间戳
	 *
	 * @return
	 */
	public static long getCurrentUnixTimestamp() {
		long nowTimeStamp = System.currentTimeMillis() / 1000;
		return nowTimeStamp;
	}

	/**
	 * 获取Unix时间戳
	 *
	 * @return
	 */
	public static String getCurrentUnixTimestampStr() {
		return String.valueOf(getCurrentUnixTimestamp());
	}

	/**
	 * 转换Unix时间戳
	 *
	 * @return nowTimeStamp
	 */
	public static long parseUnixTimeStamp(long time) {
		return time / ONE_MILLIS;
	}

	/**
	 * 获取前一周
	 *
	 * @param date
	 * @return
	 */
	public static Date getBeforeWeek(Date date) {
		return getAddDate(date, Calendar.WEEK_OF_YEAR, -1);
	}

	/**
	 * 获取前一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getBeforeDay(Date date) {
		return getAddDate(date, Calendar.DAY_OF_YEAR, -1);
	}

	/**
	 * 获取前一月
	 *
	 * @param date
	 * @return
	 */
	public static Date getBeforeMouth(Date date) {
		return getAddDate(date, Calendar.MONTH, -1);
	}

	/**
	 * 获取前一年
	 *
	 * @param date
	 * @return
	 */
	public static Date getBeforeYear(Date date) {
		return getAddDate(date, Calendar.YEAR, -1);
	}

	/**
	 * 获取前一周
	 *
	 * @param date
	 * @return
	 */
	public static Date getAfterWeek(Date date) {
		return getAddDate(date, Calendar.WEEK_OF_YEAR, 1);
	}

	/**
	 * 获取前一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getAfterDay(Date date) {
		return getAddDate(date, Calendar.DAY_OF_YEAR, 1);
	}

	/**
	 * 获取前一月
	 *
	 * @param date
	 * @return
	 */
	public static Date getAfterMouth(Date date) {
		return getAddDate(date, Calendar.MONTH, 1);
	}

	/**
	 * 获取前一年
	 *
	 * @param date
	 * @return
	 */
	public static Date getAfterYear(Date date) {
		return getAddDate(date, Calendar.YEAR, 1);
	}

	/**
	 * 增加日期
	 *
	 * @param date
	 * @param field  Calendar.MONTH,Calendar.DAY_OF_YEAR
	 * @param amount 正数为将来时间, 负数为过去时间
	 * @return
	 */
	public static Date getAddDate(Date date, int field, int amount) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(field, amount);
		Date dateFrom = cl.getTime();
		return dateFrom;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String formatTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatCurrentDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String formatYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String formatMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String formatDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String formatWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 获取过去的天数
	 *
	 * @param date
	 * @return
	 */
	public static long getBeforeDays(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (ONE_DAY * ONE_MILLIS);
	}

	/**
	 * 获取过去的小时
	 *
	 * @param date
	 * @return
	 */
	public static long getBeforeHours(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (ONE_HOUR * ONE_MILLIS);
	}

	/**
	 * 获取过去的分钟
	 *
	 * @param date
	 * @return
	 */
	public static long getBeforeMinutes(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (ONE_MINUTE * ONE_MILLIS);
	}

	/**
	 * 获取过去的秒
	 *
	 * @param date
	 * @return
	 */
	public static long getBeforeSeconds(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / ONE_MILLIS;
	}

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDays(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (ONE_MILLIS * ONE_DAY);
	}

	/**
	 * 距离今天多久
	 *
	 * @param createAt
	 * @return
	 */
	public static String formatTextFromtoday(Date createAt) {
		// 定义最终返回的结果字符串。
		String interval = null;
		if (createAt == null) {
			return "";
		}
		long millisecond = System.currentTimeMillis() - createAt.getTime();

		long second = millisecond / ONE_MILLIS;

		if (second <= 0) {
			second = 0;
		}
		// *--------------微博体（标准）
		if (second == 0) {
			interval = "刚刚";
		} else if (second < ONE_MINUTE / 2) {
			interval = second + "秒以前";
		} else if (second >= ONE_MINUTE / 2 && second < ONE_MINUTE) {
			interval = "半分钟前";
		} else if (second >= ONE_MINUTE && second < ONE_MINUTE * ONE_MINUTE) {
			// 大于1分钟 小于1小时
			long minute = second / ONE_MINUTE;
			interval = minute + "分钟前";
		} else if (second >= ONE_HOUR && second < ONE_DAY) {
			// 大于1小时 小于24小时
			long hour = (second / ONE_MINUTE) / ONE_MINUTE;
			interval = hour + "小时前";
		} else if (second >= ONE_DAY && second <= ONE_DAY * 2) {
			// 大于1D 小于2D
			interval = "昨天" + formatDate(createAt, "HH:mm");
		} else if (second >= ONE_DAY * 2 && second <= ONE_DAY * 7) {
			// 大于2D小时 小于 7天
			long day = ((second / ONE_MINUTE) / ONE_MINUTE) / 24;
			interval = day + "天前";
		} else if (second <= ONE_DAY * 365 && second >= ONE_DAY * 7) {
			// 大于7天小于365天
			interval = formatDate(createAt, "MM-dd HH:mm");
		} else if (second >= ONE_DAY * 365) {
			// 大于365天
			interval = formatDate(createAt, "yyyy-MM-dd HH:mm");
		} else {
			interval = "0";
		}
		return interval;
	}

	/**
	 * 距离截止日期还有多长时间
	 *
	 * @param date
	 * @return
	 */
	public static String formatTextFromDeadline(Date date) {
		long deadline = date.getTime() / ONE_MILLIS;
		long now = (System.currentTimeMillis()) / ONE_MILLIS;
		long remain = deadline - now;
		if (remain <= ONE_HOUR) {
			return "只剩下" + remain / ONE_MINUTE + "分钟";
		} else if (remain <= ONE_DAY) {
			return "只剩下" + remain / ONE_HOUR + "小时" + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
		} else {
			long day = remain / ONE_DAY;
			long hour = remain % ONE_DAY / ONE_HOUR;
			long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
		}

	}

	/**
	 * Unix时间戳转换成指定格式日期字符串
	 *
	 * @param timestampString 时间戳 如："1473048265";
	 * @param pattern         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
	 * @return 返回结果 如："2016-09-05 16:06:42";
	 */
	public static String unixTimeStamp2Date(String timestampString, String pattern) {
		if (StringUtils.isBlank(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		Long timestamp = Long.parseLong(timestampString) * ONE_MINUTE;
		String date = new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
		return date;
	}

	/**
	 * 日期格式字符串转换成Unix时间戳
	 *
	 * @param dateStr 字符串日期
	 * @param pattern 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2UnixTimeStamp(String dateStr, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return String.valueOf(sdf.parse(dateStr).getTime() / ONE_MINUTE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(formatDate(getBeforeDay(new Date()), "yyyy-MM-dd"));
	 * System.out.println(formatDate(getBeforeWeek(new Date()), "yyyy-MM-dd"));
	 * System.out.println(formatDate(getBeforeYear(new Date()), "yyyy-MM-dd"));
	 * System.out.println(formatDate(getAfterDay(new Date()), "yyyy-MM-dd"));
	 * System.out.println(formatDate(getAfterWeek(new Date()), "yyyy-MM-dd"));
	 * System.out.println(formatDate(getAfterYear(new Date()), "yyyy-MM-dd")); }
	 */

	public static Date reportGetDate(Date d, String type, int number) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		if (type.equals("YEAR"))
			calendar.add(Calendar.YEAR, number);
		if (type.equals("MONTH"))
			calendar.add(Calendar.MONTH, number);
		if (type.equals("DAY"))
			calendar.add(Calendar.DAY_OF_MONTH, number);
		if (type.equals("HOUR"))
			calendar.add(Calendar.HOUR_OF_DAY, number);
		Date date = calendar.getTime();
		return date;
	}

	// 获取当前月第一天
	@SneakyThrows
	public static String getNowFirstDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		return first;
	}

	// 获取当前日期
	@SneakyThrows
	public static String getNowDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前时间String类型
		String timeString = sdf.format(date);
		return timeString;
	}

	// 处理判断条件
	public Boolean isOpinion(String Opinion) {

		boolean isboolean = false;
		String[] springtimeString = { "<", "<=", "=", ">", ">=" };
		String[] Opinionarry1 = {};// 2<3
		String[] Opinionarry2 = {};// 2<=3
		String[] Opinionarry5 = {};// 2<3<4

		String zjString = "";
		Opinionarry1 = Opinion.split(springtimeString[0]);

		Opinionarry2 = Opinion.split(springtimeString[1]);// <=

		if (Opinionarry1.length == 3 && !isNumeric(Opinionarry1[1]) && isNumeric(Opinionarry1[2])) {// 10<={2003000009:柴油P值}<20
																									// 情况
			Opinionarry5 = Opinion.split(springtimeString[1]);// <=
			zjString = Opinionarry5[1].split(springtimeString[0])[0];

			if (Double.valueOf(Opinionarry1[0]) <= Double.valueOf(zjString)
					&& Double.valueOf(zjString) < Double.valueOf(Opinionarry5[1].split(springtimeString[0])[1])) {
				isboolean = true;
			}
		}

		if (Opinionarry1.length == 3 && !isNumeric(Opinionarry1[2]) && isNumeric(Opinionarry1[1])) {// 10<{2003000009:柴油P值}<=20
																									// 情况
			Opinionarry5 = Opinion.split(springtimeString[1]);// <=
			zjString = Opinionarry5[0].split(springtimeString[0])[1];

			if (Double.valueOf(Opinionarry1[0]) < Double.valueOf(zjString)
					&& Double.valueOf(zjString) <= Double.valueOf(Opinionarry5[1])) {
				isboolean = true;
			}
		}

		if (Opinionarry1.length == 2 && isNumeric(Opinionarry1[0]) && isNumeric(Opinionarry1[1])) {// {2003000009:柴油P值}<20
			if (Double.valueOf(Opinionarry1[0]) < Double.valueOf(Opinionarry1[1])) {
				isboolean = true;
			}
		}
		if (Opinionarry2.length == 2 && isNumeric(Opinionarry2[0]) && isNumeric(Opinionarry2[1])) {// {2003000009:柴油P值}<=20
			if (Double.valueOf(Opinionarry2[0]) <= Double.valueOf(Opinionarry2[1])) {
				isboolean = true;
			}
		}

		if (Opinionarry1.length == 3 && isNumeric(Opinionarry1[0]) && isNumeric(Opinionarry1[1])
				&& isNumeric(Opinionarry1[2])) {// 10<{2003000009:柴油P值}<20 情况
			if (Double.valueOf(Opinionarry1[0]) < Double.valueOf(Opinionarry1[1])
					&& Double.valueOf(Opinionarry1[1]) < Double.valueOf(Opinionarry1[2])) {
				isboolean = true;
			}
		}
		if (Opinionarry2.length == 3 && isNumeric(Opinionarry2[0]) && isNumeric(Opinionarry2[1])
				&& isNumeric(Opinionarry2[2])) {// 10<={2003000009:柴油P值}<=20 情况
			if (Double.valueOf(Opinionarry2[0]) <= Double.valueOf(Opinionarry2[1])
					&& Double.valueOf(Opinionarry2[1]) <= Double.valueOf(Opinionarry2[2])) {
				isboolean = true;
			}
		}

		String[] Opinionarry3 = {};
		Opinionarry3 = Opinion.split(springtimeString[4]);
		if (Opinionarry3.length == 2 && isNumeric(Opinionarry3[0]) && isNumeric(Opinionarry3[1])) {// {2003000009:柴油P值}>=20
			if (Double.valueOf(Opinionarry3[0]) >= Double.valueOf(Opinionarry3[1])) {
				isboolean = true;
			}
		}

		String[] Opinionarry4 = {};
		Opinionarry4 = Opinion.split(springtimeString[3]);
		if (Opinionarry4.length == 2 && isNumeric(Opinionarry4[0]) && isNumeric(Opinionarry4[1])) {// {2003000009:柴油P值}>20
			if (Double.valueOf(Opinionarry4[0]) > Double.valueOf(Opinionarry4[1])) {
				isboolean = true;
			}
		}
		return isboolean;
	}

	public boolean isNumeric(String str) {

		Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");// 这个是对的
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	// double 小数点处理
	public double getResultByDouble(double doublevale, int len) {
		if (len == 0) {
			len = 2;
		}
		BigDecimal b = new BigDecimal(doublevale);
		double f1 = b.setScale(len, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;

	}
	/**
	 * 获取两个日期相差的天数
	 *
	 * @param startDateStr
	 * @param endDateStr
	 */
	public static int getDayBetweenTwoDate(String startDateStr, String endDateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int days = 0;
		try {
			Date startDate = simpleDateFormat.parse(startDateStr);
			Date endDate = simpleDateFormat.parse(endDateStr);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);
			// 计算两个日期相差的天数
			// startCalendar.getTime().getTime()返回long毫秒数形式,毫秒转为秒所以除以1000
			// 1天=24小时，1小时=60分，1分=60秒，所以两个时间的差再除以60 * 60 * 24换算成天的形式
			days = ((int) (startCalendar.getTime().getTime() / 1000) - (int) (endCalendar.getTime().getTime() / 1000))
					/ (60 * 60 * 24);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}



	/**
	 * 获取传入日期的所在月的第一天和最后一天
	 */
	public static String[] start_end(String riqi) {

		String year = riqi.substring(0, 4);
		String month = riqi.substring(5, 7);

		int iyear = Integer.parseInt(year);
		int imonth = Integer.parseInt(month);
		String fanhuizhi[] = new String[2];


		if ((imonth == 1) || (imonth == 3) || (imonth == 5) || (imonth == 7) || (imonth == 8) || (imonth == 10)
				|| (imonth == 12)) {
			fanhuizhi[0] = year + "-" + month + "-01";
			fanhuizhi[1] = year + "-" + month + "-31";

		} else if ((imonth == 2)) {
			if (iyear % 4 == 0) {
				fanhuizhi[0] = year + "-02-01";
				fanhuizhi[1] = year + "-02-29";
			} else {
				fanhuizhi[0] = year + "-02-01";
				fanhuizhi[1] = year + "-02-28";
			}
		} else {
			fanhuizhi[0] = year + "-" + month + "-01";
			fanhuizhi[1] = year + "-" + month + "-30";
		}
		return fanhuizhi;
	}
}
