package com.unbank.common.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by Administrator on 2015/9/5.
 */
public class CommonUtils {
	
	/*
	 * 日期为度开始时间, 2014-01-01
	 */
	private static final long start;

	static {
		Calendar s = Calendar.getInstance();
		s.set(Calendar.YEAR, 2014);
		s.set(Calendar.MONTH, Calendar.JANUARY);
		s.set(Calendar.DAY_OF_MONTH, 1);
		s.set(Calendar.HOUR, 0);
		s.set(Calendar.MINUTE, 0);
		s.set(Calendar.SECOND, 0);
		s.set(Calendar.MILLISECOND, 0);
		start = s.getTime().getTime();
	}
    /**
     * String\List\Set\Map为空判断 null "null" "" 都认为是true
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof String) {
            String str = (String) obj;
            return str.trim().length() == 0 || str.toLowerCase().equals("null");
        } else if (obj instanceof List) {
            List ls = (List) obj;
            return ls.size() == 0;
        } else if (obj instanceof Set) {
            Set ls = (Set) obj;
            return ls.size() == 0;
        } else if (obj instanceof Map) {
            Map ls = (Map) obj;
            return ls.size() == 0;
        } else if (obj instanceof Object[]) {
            Object[] ls = (Object[]) obj;
            return ls.length == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param str
     * @param length
     * @param encoding
     * @return
     */
    public static String getSpecifiedLength(String str, int length,
                                            String encoding) {
        if (str != null) {
            if (isEmpty(encoding))
                encoding = "UTF-8";
            try {
                String tempStr = new String(str.getBytes(encoding),
                        "ISO-8859-1");
                int tLength = tempStr.length();
                if (length > tLength)
                    length = tLength;
                return new String(tempStr.substring(0, length).getBytes(
                        "ISO-8859-1"), encoding);
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }
        return str;
    }

    /**
     * 转成INT 不能转换返回0
     *
     * @param v
     * @return
     */
    public static int toInt(String v) {
        if (!isEmpty(v)) {
            try {
                return Integer.parseInt(v.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 是否是字母 是返回true
     *
     * @param v
     * @return
     */
    public static boolean isAlphabet(String v) {
        if (!isEmpty(v)) {
            return v.matches("[[a-z]|[A-Z]]*");
        }
        return false;
    }

    public static java.util.Date getDate(String dtStr) {
        if (dtStr == null)
            return null;
        try {
            if (dtStr.trim().length() == 10) {
                dtStr = dtStr + " 00:00:00";
            } else if (dtStr.trim().length() > 19) {
                dtStr = dtStr.substring(0, 19);
            }
            java.text.SimpleDateFormat df = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            java.util.Date dateInstance = df.parse(dtStr);
            return dateInstance;
        } catch (Exception ex) {
            return null;
        }

    }

    public static int getAge(java.util.Date birthDate) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(birthDate.getTime());
        return getAge(current);
    }

    public static int getAge(java.sql.Date birthDate) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(birthDate.getTime());
        return getAge(current);
    }

    public static int getAge(Calendar birthCalendar) {
        Calendar current = Calendar.getInstance();
        int age = current.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (current.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH) > 0) {
            age++;
        } else if (current.get(Calendar.MONTH) == birthCalendar
                .get(Calendar.MONTH)
                && current.get(Calendar.DAY_OF_MONTH) >= birthCalendar
                .get(Calendar.DAY_OF_MONTH)) {
            age++;
        }
        return age;
    }

    /**
     * 格式化小数位数
     *
     * @param d
     * @return
     */
    public static String format(double d, int u) {
        try {
            BigDecimal bigDecimal = new BigDecimal(d);
            return bigDecimal.setScale(u, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * 格式化日期为字符串.
     *
     * @param date    日期字符串
     * @param pattern 类型
     * @return 结果字符串
     */
    public static String formatDate(java.util.Date date, String pattern) {

        if (date == null) {
            return null;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 获得上月最后一天的日期
     *
     * @return
     */
    public static java.util.Date getMonthEndPrevious() {

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);
        lastDate.set(Calendar.DATE, 1);
        lastDate.roll(Calendar.DATE, -1);
        return lastDate.getTime();
    }

    /**
     * 计算当月最后一天,返回字符串
     *
     * @return
     */
    public static java.util.Date getMonthEndCurrent() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);
        lastDate.add(Calendar.MONTH, 1);
        lastDate.add(Calendar.DATE, -1);
        return lastDate.getTime();
    }

    /**
     * 获取非空字符串，null返回""
     *
     * @param str
     * @return
     */
    public static String getNotNullValue(String str) {
        if (isEmpty(str))
            return "";
        else
            return str;
    }

    /**
     * OBJECT为空判断
     *
     * @param obj
     * @return
     */
    public static boolean isObjectNull(Object obj) {
        if (obj == null)
            return true;
        return false;
    }

    /**
     * 将java.sql.Date转为Calendar类型
     *
     * @param date
     * @return
     */
    public static Calendar conversion(java.sql.Date date) {
        if (isObjectNull(date))
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date toDate(Calendar cal) {
        if (cal == null) {
            return null;
        } else {
            Date date = new Date(cal.getTimeInMillis());
            return date;
        }
    }

    public static Timestamp toTimestamp(Calendar cal) {
        if (cal == null) {
            return null;
        } else {
            Timestamp date = new Timestamp(cal.getTimeInMillis());
            return date;
        }
    }

    public static String getCNString(String str1, String encoding) {
        if (str1 != null) {
            if (isEmpty(encoding))
                encoding = "UTF-8";
            try {
                return new String(str1.getBytes("ISO-8859-1"), encoding);
            } catch (UnsupportedEncodingException e) {
                return str1;
            }
        }
        return str1;
    }

    public static String getCNString(String cnText, String sourceEncode,
                                     String targetEncode) {
        if (isNotEmpty(cnText)) {
            if (isEmpty(sourceEncode))
                sourceEncode = "ISO-8859-1";
            if (isEmpty(targetEncode))
                targetEncode = "UTF-8";
            try {
                return new String(cnText.getBytes(sourceEncode), targetEncode);
            } catch (UnsupportedEncodingException e) {
                return cnText;
            }
        }
        return cnText;
    }

    /**
	 * 判断是否为手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static void putMap(Map<String, Object> map, String message, boolean flag, int status){
		map.put("msg", message);
		map.put("success", flag);
		map.put("status", status);
	}
	
	/**
	 * 时间维度
	 * 
	 * @param cal
	 * @return
	 */
	public static int timeSk(Calendar cal) {
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		int timeSk = (h * 3600) + (m * 60) + s + 1;
		return timeSk;

	}

	/**
	 * 日期维度
	 * 
	 * @param cal
	 * @return
	 */
	public static int dateSk(Calendar cal) {
		long end = cal.getTime().getTime();
		// System.out.println(end);
		// System.out.println(start);
		//
		// 1403248867065
		// 1388560867065
		int dateSk = (int) ((end - start) / (1000 * 60 * 60 * 24)) + 1;
		return dateSk;
	}
	
	/**
	 * System.out.println(inet_ntoa(167772907));
	 * @param add
	 * @return
	 */
	public static String inet_ntoa(long add) {  
	    return ((add & 0xff000000) >> 24) + "." + ((add & 0xff0000) >> 16)  
	            + "." + ((add & 0xff00) >> 8) + "." + ((add & 0xff));  
	}  
	
	public static long inet_aton(Inet4Address add) {  
	    byte[] bytes = add.getAddress();  
	    long result = 0;  
	    for (byte b : bytes) {  
	        if ((b & 0x80L) != 0) {  
	            result += 256L + b;  
	        } else {  
	            result += b;  
	        }  
	        result <<= 8;  
	    }  
	    result >>= 8;  
	    return result;  
	}  
	  
	/** 
	 * System.out.println(inet_aton("10.0.2.235")); 
	 */  
	public static Integer inet_aton(String add) {  
		Integer result = 0;  
	    // number between a dot  
		Integer section = 0;  
	    // which digit in a number  
	    int times = 1;  
	    // which section  
	    int dots = 0;  
	    for (int i = add.length() - 1; i >= 0; --i) {  
	        if (add.charAt(i) == '.') {  
	            times = 1;  
	            section <<= dots * 8;  
	            result += section;  
	            section = 0;  
	            ++dots;  
	        } else {  
	            section += (add.charAt(i) - '0') * times;  
	            times *= 10;  
	        }  
	    }  
	    section <<= dots * 8;  
	    result += section;  
	    return result;  
	}  
	
	/**
	 * 处理异常信息并返回结果
	 * try {
			String str = null;
			System.out.println(Integer.parseInt(str));
		} catch (NumberFormatException e) {
			System.out.println(getExceptionMessage(e));;
		}
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionMessage(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = e.getStackTrace();
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i].getClassName().contains("com.unbank")) {
				sb.append("class: ").append(stacks[i].getClassName()).append("; method: ").append(stacks[i].getMethodName()).append("; line: ").append(stacks[i].getLineNumber())
						.append(";  Exception: ");
				break;
			}
		}
		
		String message = e.toString();
		if (message.lastIndexOf(":") != -1)
			message = message.substring(0, message.lastIndexOf(":"));
		return sb.append(message).toString();
	}
	
	
	
	public static StringBuffer putLogInfo(Map<String, String> infoMap) {
		StringBuffer sb = null;
		
		if(isNotEmpty(infoMap)){
			sb = new StringBuffer();
			
			for (Map.Entry<String, String> entry : infoMap.entrySet()) {
				if(sb.length() > 0){
					sb.append(",").append(entry.getKey()).append("=").append(entry.getValue());
				}else{
					sb.append(entry.getKey()).append("=").append(entry.getValue());
				}
			}
		}
		return sb;
	}
	
	/**
	 * 分割字符串并去空格
	 * @param str
	 * @param splitType
	 * @return
	 */
	public static List<String> splitStr(String str, String splitType){
		if(CommonUtils.isEmpty(str))
			return null;
		
		String [] ags = str.split(splitType);
		List<String> returnList = new ArrayList<String>();
		for (String word : ags) {
			// 排除搜索条件为空格的数据
			if(CommonUtils.isNotEmpty(word.trim())){
				returnList.add(word.trim());
			}
		}
		if(CommonUtils.isNotEmpty(returnList)){
			return returnList;
		}
		return null;
	}
	
	public static void putMap(Map<String, String> map, String status, String message, String esId){
		map.put("status", status);
		map.put("msg", message);
		if(isNotEmpty(esId)){
			map.put("esId", esId);
		}
	}

	public static String substring(String str, int start, int end) {
		if (str == null) {
			return null;
		} else {
			if (end < 0) {
				end += str.length();
			}

			if (start < 0) {
				start += str.length();
			}

			if (end > str.length()) {
				end = str.length();
			}

			if (start > end) {
				return "";
			} else {
				if (start < 0) {
					start = 0;
				}

				if (end < 0) {
					end = 0;
				}

				return str.substring(start, end);
			}
		}
	}
	
	public static List<List<String>> convertList(List<String> list) {
		List<String> temp = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String s = "";
			temp.add(list.get(i));
			s += list.get(i);
			for (int j = i + 1; j < list.size() ; j++) {
				s += "_" + list.get(j);
				temp.add(s);
			}
		}
		
		List<List<String>> result = new ArrayList<List<String>>();
		for (String s : temp) {
			String[] ags = s.split("_");
			List<String> ls = new ArrayList<String>();
			for (String str : ags) {
				ls.add(str);
			}
			result.add(ls);
		}
		return result;
	}
	
	public static List<List<String>> convertList(List<String> list, List<String> list2) {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> temp = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String s = "";
			temp.add(list.get(i));
			s += list.get(i);
			for (int j = i + 1; j < list.size() ; j++) {
				s += "_" + list.get(j);
				temp.add(s);
			}
		}
		List<String> temp2 = new ArrayList<String>();
		
		for (int i = 0; i < temp.size(); i++) {
			String s = "";
			s += temp.get(i);
//			temp2.add(s);
			if(CommonUtils.isNotEmpty(list2)){
				for (String str : list2) {
					temp2.add(temp.get(i) + "_" + str);
				}
				for (int j = 0; j < list2.size() ; j++) {
					s += "_" + list2.get(j);
					if(!temp2.contains(s)){
						temp2.add(s);
					}
					
				}
			}
			
		}
		if(CommonUtils.isNotEmpty(temp2)){
			for (String s : temp2) {
				String[] ags = s.split("_");
				List<String> ls = new ArrayList<String>();
				for (String str : ags) {
					ls.add(str);
				}
				result.add(ls);
			}
		}
		
		return result;
	}
	
	public static List<List<String>> convertList2(List<String> list,
			List<String> list2) {
		List<List<String>> result = new ArrayList<List<String>>();
		String ss = "";
		for (int i = 0; i < list.size(); i++) {
			if("".equals(ss)){
				ss += list.get(i);
			}else{
				ss += "_" + list.get(i);
			}
			
		}
		List<String> temp2 = new ArrayList<String>();
		
		if(CommonUtils.isNotEmpty(list2)){
			String sss = ss;
			for (String str : list2) {
				temp2.add(sss + "_" + str);
			}
			for (int j = 0; j < list2.size() ; j++) {
				sss += "_" + list2.get(j);
				if(!temp2.contains(sss)){
					temp2.add(sss);
				}
				
			}
		}
		
		if(CommonUtils.isNotEmpty(temp2)){
			for (String s : temp2) {
				String[] ags = s.split("_");
				List<String> ls = new ArrayList<String>();
				for (String str : ags) {
					ls.add(str);
				}
				result.add(ls);
			}
		}
		
		return result;
	}
	
	public static String getHtml(Map<String, String> params, String url) {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpClientBuilder httpClientBuilder = new HttpClientBuilder(false,
				poolingHttpClientConnectionManager, cookieStore);
		CloseableHttpClient httpClient = httpClientBuilder.getHttpClient();
		Fetcher fetcher1 = new Fetcher(cookieStore, httpClient);
		String html = null;
		try {
			html = fetcher1.post(url, params, null, "utf-8");
		} catch (Exception e) {
		}
		return html;
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("银行");
		list.add("金融");

		List<String> list2 = new ArrayList<String>();
		list2.add("支付");
		list2.add("现金");
		
		List<List<String>> result = convertList2(list, list2);
		
		System.out.println(result);
	}
}
