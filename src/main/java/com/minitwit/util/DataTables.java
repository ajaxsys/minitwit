package com.minitwit.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import spark.Request;
import spark.Response;

/**
 * Sample reference :
 * https://datatables.net/development/server-side/jsp
 */
public
class DataTables {

    @FunctionalInterface
    public interface IGetPagingResult<E> {
        List<E> apply(int start, int length);
    }
    @FunctionalInterface
    public interface IEachResult<E> {
        void apply(
            E result,
            JSONArray rowJson);
    }
    @FunctionalInterface
    public interface IGetPagingCount {
        int apply();
    }
    public static class SearchCondition {

        private Request req;

        public SearchCondition(Request req) {
            this.req = req;
        }

        public Optional<String> getStr(String colomnName) {
            Optional<String> dataTalbeParamName =
                req.queryParams().
                    stream().
                    filter(
                        param->
                            param.endsWith("[name]")
                            &&
                            req.queryParams(param).compareTo(colomnName) == 0).
                    findFirst();

            Optional<String> dataTalbeParamValue =
                dataTalbeParamName.map(
                    nameOfKey-> {
                        String value = req.queryParams(
                            toNameOfValue(nameOfKey));

                        return StringUtil.isBlank(value) ?
                            null:
                            value;
                    });

            return dataTalbeParamValue;
        }

        Pattern INT = Pattern.compile("-?[0-9]+");

        public Optional<Integer> getInt(String name) {
            return getStr(name).map(
                v->
                    INT.matcher(v).matches() ?
                        Integer.parseInt(v) :
                        null);
        }
    }

    public static
    <E>
    String
    pagingAjax(
        Request req,
        Response res,
        IGetPagingResult<E> getPagingResult,
        IEachResult<E> eachResult,
        IGetPagingCount getPagingCount) {

        int length = 10;
        int start = 0;

        String sStart = req.queryParams("start");
        String sLength = req.queryParams("length");

        try {
            if (sStart != null) {
                start = Integer.parseInt(sStart);
                if (start < 0)
                    start = 0;
            }
            if (sLength != null) {
                length = Integer.parseInt(sLength);
                if (length < 10 || length > 100)
                    length = 10;
            }
        } catch (NumberFormatException e) {
            // use default setting
        }

        System.out.println("start=" + start + " length=" + length);

        final List<E> messages = getPagingResult.apply(
                start,
                length);

        JSONObject result = new JSONObject();

        JSONArray array = new JSONArray();
        for (E rs : messages) {
            JSONArray ja = new JSONArray();
            eachResult.apply(rs, ja);
            array.put(ja);
        }

        int total = getPagingCount.apply();

        result.put("iTotalRecords", total);
        result.put("iTotalDisplayRecords", total); // Not use: count after like sql
        result.put("aaData", array);

        res.type("application/json");
        res.header("Cache-Control", "no-store");

        return result.toString();
    }

    public static
    SearchCondition
    parse(
        Request req) {

        return new SearchCondition(req);
    }

    /* Http request from datatable, sample:
    draw:5
    columns[0][data]:0
    columns[0][name]:username
    columns[0][searchable]:true
    columns[0][orderable]:false
    columns[0][search][value]:user008
    columns[0][search][regex]:false
    columns[1][data]:1
    columns[1][name]:text
    columns[1][searchable]:true
    columns[1][orderable]:false
    columns[1][search][value]:
    columns[1][search][regex]:false
    columns[2][data]:2
    columns[2][name]:time
    columns[2][searchable]:true
    columns[2][orderable]:false
    columns[2][search][value]:
    columns[2][search][regex]:false
    columns[3][data]:3
    columns[3][name]:email
    columns[3][searchable]:true
    columns[3][orderable]:false
    columns[3][search][value]:user008
    columns[3][search][regex]:false
    start:0
    length:25
    search[value]:
    search[regex]:false
    _:1473832421865*/
    public static
    <T>
    T
    parse(
        Request req,
        T pojo) {

        // As sample above, extract {username=user008, email=user008}
        Map<String, String> searchKeyValues =
            req.queryParams().
                stream().
                filter(
                    nameOfKey->
                        nameOfKey.endsWith("[name]")
                        &&
                        ! StringUtil.isBlank(
                            req.queryParams(
                                toNameOfValue(nameOfKey)))).
                collect(
                    Collectors.toMap(
                        nameOfKey->
                            req.queryParams(nameOfKey),
                        nameOfKey->
                            req.queryParams(
                                toNameOfValue(nameOfKey))));

        try {
            BeanUtils.populate(pojo, searchKeyValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return pojo;
    }

    // e.g: columns[3][name]:email -> columns[3][search][value]:user008
    private static
    String
    toNameOfValue(
        String nameOfKey) {

        return nameOfKey.replace("[name]", "[search][value]");
    }
}
