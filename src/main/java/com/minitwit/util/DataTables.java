package com.minitwit.util;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Sample reference :
 * https://datatables.net/development/server-side/jsp
 */
public
class DataTables {

    public interface IGetPagingResult<E> {
        List<E> apply(int start, int length);
    }
    public interface IEachResult<E> {
        void apply(
            E result,
            JSONArray rowJson);
    }
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
                    paramName-> {
                        String value = req.queryParams(
                            paramName.replace("[name]", "[search][value]"));

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
}
