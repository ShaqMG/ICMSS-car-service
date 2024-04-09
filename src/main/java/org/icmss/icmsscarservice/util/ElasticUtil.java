package org.icmss.icmsscarservice.util;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.json.JsonData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;

import java.util.List;
import java.util.function.Function;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticUtil {

    public static MatchQuery matchQuery(String field, FieldValue value) {
        return MatchQuery.of(m ->
                m.field(field).query(value)
        );
    }

    public static MatchQuery fuzzyMatchQuery(String field, FieldValue value, Integer fuzziness) {
        return MatchQuery.of(m ->
                m.field(field).query(value).fuzziness(fuzziness.toString())
        );
    }

    public static TermsQuery termsQuery(String field, List<FieldValue> values) {
        return TermsQuery.of(t ->
                t.field(field).terms(tv -> tv.value(values))
        );
    }

    public static TermQuery termQuery(String field, FieldValue value) {
        return TermQuery.of(t ->
                t.field(field).value(value)
        );
    }

    public static RangeQuery rangeGTEQuery(String field, Object value) {
        String jsonString = JsonUtil.toJsonString(value);
        JsonData jsonData = JsonData.fromJson(jsonString);
        return RangeQuery.of(r ->
                r.field(field).gte(jsonData)
        );
    }

    public static RangeQuery rangeLTEQuery(String field, Object value) {
        String jsonString = JsonUtil.toJsonString(value);
        JsonData jsonData = JsonData.fromJson(jsonString);
        return RangeQuery.of(r ->
                r.field(field).lte(jsonData)
        );
    }

    public static void addAggregation(NativeQueryBuilder queryBuilder, String name, String field, int size) {
        queryBuilder.withAggregation(name, getTermsAggregation(field, size));
    }

    public static void addNestedAggregation(NativeQueryBuilder queryBuilder, String name, String field, int size) {
        String aggregationName = name + "_nested";
        Aggregation aggregation = Aggregation.of(
                a -> a
                        .nested(n -> n.path(name))
                        .aggregations(aggregationName, getTermsAggregation(field, size))
        );
        queryBuilder.withAggregation(name, aggregation);
    }

    public static <T> List<FieldValue> convertFieldValues(List<T> values,
                                                          Function<? super T, FieldValue> mapper) {
        return values.stream().map(mapper).toList();
    }

    public static Aggregation getTermsAggregation(String field, int size) {
        return AggregationBuilders.terms(t -> t.field(field).size(size));
    }

    public static <T> List<T> convertAggregationToList(Aggregate aggregation,
                                                       Function<? super StringTermsBucket, T> mapper) {

        return aggregation
                .sterms()
                .buckets()
                .array()
                .stream()
                .map(mapper)
                .toList();
    }

    public static <T> List<T> convertNestedAggregationToList(String name,
                                                             Aggregate aggregation,
                                                             Function<? super StringTermsBucket, T> mapper) {

        Aggregate nestedAggregation = aggregation
                .nested()
                .aggregations()
                .get(name);
        return convertAggregationToList(nestedAggregation, mapper);
    }

}
