package com.minitwit.util;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sample reference :
 * https://datatables.net/development/server-side/jsp
 */
public
class DataTables {

    public interface IGetPagingResult<E> {
        List<E> apply(int start, int length, SearchCondition param);
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
                    paramName->
                        req.queryParams(paramName.replace("[name]", "[search][value]")));

            return dataTalbeParamValue;
        }

        public Optional<Integer> getInt(String name) {
            return Optional.empty();
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
                length,
                new SearchCondition(req));

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
}
