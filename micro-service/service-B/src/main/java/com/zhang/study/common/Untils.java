package com.zhang.study.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhang.study.entity.IDCardEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description: 常量工具类
 * @author: 叶柳
 * @date: 2017年2月28日
 * 深圳天源迪科信息技术股份有限公司 版权所有.
 * <p>
 * 1.对象是否为空判断
 * 2.对象转型
 * 3.数组排序、去重
 * 4.中英Key转换
 */
public class Untils {
    private final static Logger logger = LoggerFactory.getLogger(Untils.class);
    private static final ThreadLocal<Map<String, DateFormat>> tl = new ThreadLocal<Map<String, DateFormat>>();
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static boolean isEmpty(String value) {
        return (value == null || value.trim().equals(""));
    }

    public static boolean isEmpty(StringBuffer value) {
        return (value == null || value.toString().trim().equals(""));
    }

    public static boolean isEmpty(StringBuilder value) {
        return (value == null || value.toString().trim().equals(""));
    }

    public static boolean isEmpty(List list) {
        return (list == null || list.size() == 0);
    }

    public static boolean isEmpty(Set set) {
        return (set == null || set.size() == 0);
    }

    public static boolean isEmpty(Map map) {
        return (map == null || map.size() == 0);
    }

    public static boolean isEmpty(Double value) {
        return (value == null || value.doubleValue() == 0.0);
    }

    public static boolean isEmpty(Long value) {
        return (value == null || value.longValue() == 0);
    }

    public static boolean isEmpty(Object[] value) {
        return (value == null || value.length == 0);
    }

    public static boolean isEmpty(Object obj) {
        return (obj == null || obj.equals(""));
    }

    /*
    * 随机生成一个不重复的ID
    * */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static String getUUID(String prefix) {
        return prefix + getUUID();
    }

    /*
    * 批量随机生成不重复的ID
	* */
    public static List<String> getUUID(String prefix, int size) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            res.add(prefix + getUUID());
        }
        return res;
    }

    /**
     * ObjectToString
     */
    public static String ObjectToString(Object o) {
        return o == null || "null".equals(o) ? "" : o.toString();
    }

    /**
     * 是否为数字
     */
    public static Boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static List<String> yearList(String startDate, String endDate) {
        List<String> yearList = new ArrayList<String>();
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            Integer startYear;
            startYear = Integer.parseInt(year.format(sd.parse(startDate)));
            Integer endYear = Integer.parseInt(year.format(sd.parse(endDate)));
            for (Integer i = endYear; i >= startYear; i--) {
                yearList.add(Integer.toString(i));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return yearList;
    }

    /**
     * 某年某月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONDAY, month);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 时间排序
     */
    public static void dateSort(List<Map<String, Object>> sortList, String cloum) {
        Date date = null;
        Date dateTemp = null;
        //SimpleDateFormat ck=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (!Untils.isEmpty(sortList)) {
                for (int i = 0, length = sortList.size() - 1; i < length; i++) {
                    int min = i;
                    for (int j = i + 1, lengtht = sortList.size(); j < lengtht; j++) {
                        Map<String, Object> resultMap = sortList.get(min);
                        Map<String, Object> resultMapTemp = sortList.get(j);

                        if (isEmpty(resultMapTemp.get(cloum)) || isEmpty(resultMap.get(cloum))) continue;
                        //resultMap.put(cloum, ck.format(DateFormatUtil.convertStringToDate(resultMap.get(cloum).toString())));
                        //resultMapTemp.put(cloum, ck.format(DateFormatUtil.convertStringToDate(resultMapTemp.get(cloum).toString())));
                        dateTemp = convertStringToDate(resultMapTemp.get(cloum).toString());
                        date = convertStringToDate(resultMap.get(cloum).toString());
                        if (date.getTime() < dateTemp.getTime()) {
                            min = j;
                        }
                    }
                    if (min != i) {
                        Map<String, Object> mapTemp = sortList.get(min);
                        sortList.set(min, sortList.get(i));
                        sortList.set(i, mapTemp);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("utils dateSort error", e);
        }
    }

    /**
     * 时间排序
     */
    public static void dateSortToDate(List<Map<String, Object>> sortList, String cloum) {
        Date date = null;
        Date dateTemp = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (!Untils.isEmpty(sortList)) {
                for (int i = 0, length = sortList.size() - 1; i < length; i++) {
                    int min = i;
                    for (int j = i + 1, lengtht = sortList.size(); j < lengtht; j++) {
                        Map<String, Object> resultMap = sortList.get(min);
                        Map<String, Object> resultMapTemp = sortList.get(j);

                        if (isEmpty(resultMapTemp.get(cloum)) || isEmpty(resultMap.get(cloum))) continue;
                        resultMap.put(cloum, sd.format(convertStringToDate(resultMap.get(cloum).toString())));
                        resultMapTemp.put(cloum, sd.format(convertStringToDate(resultMapTemp.get(cloum).toString())));

                        dateTemp = convertStringToDate(resultMapTemp.get(cloum).toString());
                        date = convertStringToDate(resultMap.get(cloum).toString());

                        if (date.getTime() < dateTemp.getTime()) {
                            min = j;
                        }
                    }
                    if (min != i) {
                        Map<String, Object> mapTemp = sortList.get(min);
                        sortList.set(min, sortList.get(i));
                        sortList.set(i, mapTemp);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("utils dateSort error", e);
        }
    }

    public static void main(String[] args) {
        Integer[] arrs = {0, 5, 7, 9, 1};
        for (int i = 0, length = arrs.length; i < length; i++) {
            for (int j = 0; j < arrs.length - 1 - i; j++) {
                if (arrs[j] < arrs[j + 1]) {
                    Integer temp = arrs[j + 1];
                    arrs[j + 1] = arrs[j];
                    arrs[j] = temp;
                }
            }
        }
        for (int i = 0; i < arrs.length; i++) {
            System.out.println(arrs[i]);
        }
    }

    public static void gjDateSort(List<Map<String, Object>> sortList, String cloum, SimpleDateFormat sd) throws ParseException {
        int month = 0;
        int monthTemp = 0;
        //Map<String,Object> date;
        Date date;
        Date dateTemp = null;
        SimpleDateFormat mm = new SimpleDateFormat("MMddhhmmss");
        try {
            if (!Untils.isEmpty(sortList)) {
                for (int i = 0, length = sortList.size() - 1; i < length; i++) {
                    int min = i;
                    for (int j = i + 1, lengtht = sortList.size(); j < lengtht; j++) {
                        Map<String, Object> resultMap = sortList.get(min);
                        date = sd.parse((String) resultMap.get(cloum));
                        month = Integer.parseInt(mm.format(date));

                        Map<String, Object> resultMapTemp = sortList.get(j);
                        dateTemp = sd.parse((String) resultMapTemp.get(cloum));
                        monthTemp = Integer.parseInt(mm.format(dateTemp));
                        if (month < monthTemp) {
                            min = j;
                        }
                    }
                    if (min != i) {
                        Map<String, Object> mapTemp = sortList.get(min);
                        sortList.set(min, sortList.get(i));
                        sortList.set(i, mapTemp);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("utils gjDateSort error", e);
        }
    }

    public static void sortCount(List<Map<String, Object>> sortList, String colum) {
        try {
            if (!Untils.isEmpty(sortList)) {
                int count;
                String city = "";
                String coutCity = "";
                for (int j = 0; j < sortList.size(); j++) {
                    Map<String, Object> szCf = sortList.get(j);
                    count = 1;
                    if (Untils.isEmpty(szCf.get(colum))) continue;
                    city = szCf.get(colum).toString().trim();
                    for (int i = sortList.size() - 1; i > j; i--) {
                        Map<String, Object> szCountCf = sortList.get(i);
                        if (Untils.isEmpty(szCountCf.get(colum))) continue;
                        coutCity = szCountCf.get(colum).toString().trim();
                        if (city.equalsIgnoreCase(coutCity)) {
                            count++;
                            sortList.remove(i);
                        }
                    }
                    szCf.put("count", count);
                }
            }
        } catch (Exception e) {
            logger.error("sortCount error", e);
        }
    }

    /**
     * 去重复
     */
    public static void disData(List<Map<String, Object>> sortList, String colum) {
        try {
            if (!Untils.isEmpty(sortList)) {
                String city = "";
                String coutCity = "";
                for (int j = 0; j < sortList.size(); j++) {
                    Map<String, Object> szCf = sortList.get(j);
                    if (Untils.isEmpty(szCf.get(colum))) continue;
                    city = szCf.get(colum).toString().trim();
                    for (int i = sortList.size() - 1; i > j; i--) {
                        Map<String, Object> szCountCf = sortList.get(i);
                        if (Untils.isEmpty(szCountCf.get(colum))) continue;
                        coutCity = szCountCf.get(colum).toString().trim();
                        if (city.equalsIgnoreCase(coutCity)) {
                            sortList.remove(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("sortCount error", e);
        }
    }

    /**
     * 身份证排序
     */
    public static List<Map<String, Object>> sortIdCard(List<Map<String, Object>> data, String getIdKey, boolean visible) {
        try {
            int start = data.size() - 1;
            for (int i = 0; i < data.size(); i++) {
                //if(!new IdCardCheck((String) data.get(i).get(getIdKey)).validate()){
                if (!new IDCardEntity().verify((String) data.get(i).get(getIdKey))) {
                    Map<String, Object> m = data.get(i);
                    m.put("validate", "false");
                    for (int j = start; j >= i; j--) {

                        //if(new IdCardCheck((String) data.get(j).get(getIdKey)).validate()){
                        if (new IDCardEntity().verify((String) data.get(j).get(getIdKey))) {
                            Map<String, Object> n = data.get(j);
                            data.set(i, n);
                            data.set(j, m);
                            start--;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.print("根据证件号码排序异常：" + e.getLocalizedMessage());
        }

        return data;
    }

    /**
     * 去重复 并删除为空的数据
     *
     * @param sortList
     */
    public static void disDataByKey(List<Map<String, Object>> sortList, String colum) {
        try {
            if (!Untils.isEmpty(sortList)) {
                String city = "";
                String coutCity = "";
                for (int j = 0; j < sortList.size(); j++) {
                    Map<String, Object> szCf = sortList.get(j);
                    if (Untils.isEmpty(szCf.get(colum))) {
                        sortList.remove(j);
                        --j;
                        continue;
                    }
                    city = szCf.get(colum).toString().trim();
                    for (int i = sortList.size() - 1; i > j; i--) {
                        Map<String, Object> szCountCf = sortList.get(i);
                        if (Untils.isEmpty(szCountCf.get(colum))) {
                            sortList.remove(i);
                            continue;
                        }
                        coutCity = szCountCf.get(colum).toString().trim();
                        if (city.equalsIgnoreCase(coutCity)) {
                            sortList.remove(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("disDataByKey error", e);
        }
    }

    public static void disDataByKey(List<Map<String, Object>> sortList, String colum, String colum2) {
        try {
            if (!Untils.isEmpty(sortList)) {
                String coutCity = "";
                for (int j = 0; j < sortList.size(); j++) {
                    Map<String, Object> szCf = sortList.get(j);
                    if (Untils.isEmpty(szCf.get(colum))) {
                        sortList.remove(j);
                        --j;
                        continue;
                    }
                    String city = szCf.get(colum).toString().trim();
                    city += szCf.get(colum2) == null ? "" : szCf.get(colum2).toString().trim();
                    for (int i = sortList.size() - 1; i > j; i--) {
                        Map<String, Object> szCountCf = sortList.get(i);
                        if (Untils.isEmpty(szCountCf.get(colum))) {
                            sortList.remove(i);
                            continue;
                        }
                        coutCity = szCountCf.get(colum) == null ? "" : szCountCf.get(colum).toString().trim();
                        coutCity += szCountCf.get(colum2) == null ? "" : szCountCf.get(colum2).toString().trim();
                        if (city.equalsIgnoreCase(coutCity)) {
                            sortList.remove(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("disDataByKey error", e);
        }
    }

    public static void sortCpsbCount(List<Map<String, Object>> sortList, String address, String date) {
        try {
            if (!Untils.isEmpty(sortList)) {
                String jkdz = "";
                String coutCity = "";
                for (int j = 0; j < sortList.size(); j++) {
                    Map<String, Object> szCf = sortList.get(j);
                    if (Untils.isEmpty(szCf.get(address))) continue;
                    jkdz = (String) szCf.get(address);
                    for (int i = sortList.size() - 1; i > j; i--) {
                        Map<String, Object> szCountCf = sortList.get(i);
                        if (Untils.isEmpty(szCountCf.get(address))) continue;
                        coutCity = (String) szCountCf.get(address);
                        if (jkdz.trim().equalsIgnoreCase(coutCity.trim())) {
                            if (isEmpty(szCountCf.get(date) == null) || (szCountCf.get(date).toString().trim().equalsIgnoreCase(szCf.get(date).toString().trim()))) {
                                sortList.remove(i);
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("sortCount error", e);
        }
    }


    /**
     * 中英key转换
     */
    public static List<Map<String, Object>> replaceKey(List<Map<String, Object>> resultDate, String[] oldKeys, String[] newKeys) {

        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        try {
            if (!Untils.isEmpty(resultDate)) {
                for (Map<String, Object> map : resultDate) {
                    if (oldKeys.length == newKeys.length) {
                        if (!isEmpty(map)) {
                            Map<String, Object> rtn = new HashMap<String, Object>();
                            for (int i = 0; i < oldKeys.length; i++) {
                                rtn.put(newKeys[i], map.get(oldKeys[i]));
                            }
                            resultMap.add(rtn);
                        } else {
                            logger.error("移动终端，转换key为中文失败，新旧key值长度不匹配！");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("移动终端，转换key为中文失败", e);
        }
        return resultMap;
    }


    public static String discResultByFiled(String result, String filterColumn) {
        JSONObject obj = JSONObject.parseObject(result);
        // if(url.contains("GA_KTC_MH_PASSENGER_OUT_DZ")){
        if (filterColumn != null) {
            if (Integer.parseInt(obj.get("COUNT").toString()) > 0) {
                JSONArray jsonArray = JSONObject.parseArray(obj.get(
                        "DATA").toString());
                Object obj_i = null;
                for (int i = jsonArray.size() - 1; i >= 0; i--) {
                    obj_i = ((JSONObject) jsonArray.get(i)).get(filterColumn);
                    if (obj_i == null || "".equals(obj_i.toString().trim())) {
                        jsonArray.remove(i);
                    }
                }

                for (int i = 0; i < jsonArray.size(); i++) {
                    String obj_old = ((JSONObject) jsonArray.get(i))
                            .getString(filterColumn);
                    for (int j = jsonArray.size() - 1; j > i; j--) {
                        String obj_j = ((JSONObject) jsonArray.get(j))
                                .getString(filterColumn);
                        if (obj_old.trim().equals(obj_j.trim())) {
                            jsonArray.remove(j);
                        }
                    }
                }
                obj.put("DATA", jsonArray);
            }
        }
        // }
        return obj.toString();
    }

    /**
     */
    public static DateFormat getDateFormat(String format) {
        Map<String, DateFormat> map = tl.get();

        if (map == null) {
            map = new HashMap<String, DateFormat>();
            tl.set(map);
        }

        if (StringUtils.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }

        DateFormat ret = map.get(format);

        if (ret == null) {
            ret = new SimpleDateFormat(format);
            map.put(format, ret);
        }

        return ret;
    }

    /**
     * Get Default DateFormat
     *
     * @return
     */
    public static DateFormat getDateFormat() {
        return getDateFormat(null);
    }

    public static Map<String, String> getDateYearCheck(int px) {
        Map<String, String> dateStr = new HashMap<String, String>();
        SimpleDateFormat sb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cld = Calendar.getInstance();
        Date today = cld.getTime();
        cld.set(Calendar.YEAR, cld.get(Calendar.YEAR) - px);
        dateStr.put("today", sb.format(today));
        dateStr.put("startDate", sb.format(cld.getTime()));
        return dateStr;

    }

    /**
     * 根据字符串生成日期格式，在转换为date对象
     */
    public static String parseStringToFormat(String date) throws ParseException {
        String parse = date;
        parse = parse.replaceFirst("^[0-9]{4}([^0-9])", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9])", "yy$1");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1MM$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}( ?)", "$1dd$2");
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9])", "$1HH$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1mm$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1ss$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,3}([^0-9]?)", "$1SSS$2");
        boolean flag = false;
        String reg = "^\\d+$";
        Pattern p = Pattern.compile(reg);
        for (int i = 0; i < parse.length(); i++) {
            if (p.matcher(String.valueOf(parse.charAt(i))).matches()) {
                flag = true;
                break;
            }
        }
        if (true == flag || date.equals(parse)) {
            String[] farmatArr = "y:y:y:y:M:M:d:d:H:H:m:m:s:s:S:S:S".split(":");
            String farmat = "";
            int index = 0;
            for (int i = 0; i < date.length(); i++) {
                if (p.matcher(String.valueOf(date.charAt(i))).matches()) {
                    farmat = farmat + farmatArr[index];
                    index++;
                } else {
                    farmat = farmat + String.valueOf(date.charAt(i));
                }
            }
            parse = farmat;
        }
        return parse;
    }

    /**
     * 根据字符型时间转成时间类型对象
     */
    public static Date convertStringToDate(String strDate) throws ParseException, Exception {
        Date date = null;
        if (Untils.isEmpty(strDate)) return null;
        String fromat = getTimeFormat(strDate);
        //UTC 通用时间处理
        if (strDate.length() == 24) {
            strDate = strDate.replace("Z", " UTC");
        }
        SimpleDateFormat df = new SimpleDateFormat(fromat, Locale.ENGLISH);
        date = df.parse(strDate);

        return (date);
    }

    /**
     * 获取时间格式
     */
    public static String getTimeFormat(String timeStr) {
        String formatStr = "";
        //20131007
        if (timeStr.length() == 8) {
            formatStr = "yyyyMMdd";
        }
        //2013-10-07
        else if (timeStr.length() == 10 && timeStr.indexOf("-") != -1) {
            formatStr = "yyyy-MM-dd";
        }
        //2013100713
        else if (timeStr.length() == 10 && timeStr.indexOf("2") == 0) {
            formatStr = "yyyyMMddHH";
        }
        //1425523220 UNIX时间
        else if (timeStr.length() == 10 && timeStr.indexOf("1") == 0) {
            formatStr = "UNIX";
        }
        //20130401210219
        else if (timeStr.length() == 14) {
            formatStr = "yyyyMMddHHmmss";
        }
        //2013-04-01 21:02:19
        else if (timeStr.length() == 19) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        //201305300058
        else if (timeStr.length() == 12) {
            formatStr = "yyyyMMddHHmm";
        }
        //2013-10-08 00:00:00.000
        else if (timeStr.length() == 23) {
            formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        //20131008000000000
        else if (timeStr.length() == 17) {
            formatStr = "yyyyMMddHHmmssSSS";
        }
        //2015-10-05 00:00:00.0
        else if (timeStr.length() == 21) {
            formatStr = "yyyy-MM-dd HH:mm:ss.S";
        }
        //Tue Jul 05 17:00:00 CST 2016
        else if (timeStr.length() == 28) {
            formatStr = "EEE MMM d HH:mm:ss 'CST' yyyy";
        }
        //2015-12-05T16:00:00.000Z
        else if (timeStr.length() == 24) {
            formatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
        } else {
            logger.error("未定义的时间格式，无法转换，只能使用默认数据格式：" + timeStr);
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        return formatStr;
    }


    /**
     * 获取时间格式'yyyy-mm-dd hh24:mi:ss'
     */
    public static String getTimeFormatString(String timeStr) {
        String formatStr = "";
        //20131007
        if (timeStr.length() == 8) {
            formatStr = "yyyyMMdd";
        }
        //2013-10-07
        else if (timeStr.length() == 10 && timeStr.indexOf("-") != -1) {
            formatStr = "yyyy-MM-dd";
        }
        //2013100713
        else if (timeStr.length() == 10 && timeStr.indexOf("2") == 0) {
            formatStr = "yyyyMMddHH";
        }
        //1425523220 UNIX时间
        else if (timeStr.length() == 10 && timeStr.indexOf("1") == 0) {
            formatStr = "UNIX";
        }
        //20130401210219
        else if (timeStr.length() == 14) {
            formatStr = "yyyyMMddHHmmss";
        }
        //2013-04-01 21:02:19
        else if (timeStr.length() == 19) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        //201305300058
        else if (timeStr.length() == 12) {
            formatStr = "yyyyMMddHHmm";
        }
        //2013-10-08 00:00:00.000
        else if (timeStr.length() == 23) {
            formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        //20131008000000000
        else if (timeStr.length() == 17) {
            formatStr = "yyyyMMddHHmmssSSS";
        }
        //2015-10-05 00:00:00.0
        else if (timeStr.length() == 21) {
            formatStr = "yyyy-MM-dd HH:mm:ss.S";
        }
        //Tue Jul 05 17:00:00 CST 2016
        else if (timeStr.length() == 28) {
            formatStr = "EEE MMM d HH:mm:ss 'CST' yyyy";
        }
        //2015-12-05T16:00:00.000Z
        else if (timeStr.length() == 24) {
            formatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
        } else {
            logger.error("未定义的时间格式，无法转换，只能使用默认数据格式：" + timeStr);
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        return formatStr;
    }

    /**
     * 切割并去重
     * 如： 张三 男 1986
     *
     * @author liu.ye
     */
    public static List<String> removeTrim(String searchConditions) {
        List<String> res = new ArrayList<>();
        if(StringUtils.isNotEmpty(searchConditions)){
            String[] searchFields = searchConditions.split(" ");
            for (String str : searchFields) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(str)) {
                    res.add(str);
                }
            }
        }
        return res;
    }

}
