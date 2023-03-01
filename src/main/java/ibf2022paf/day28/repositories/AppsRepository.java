package ibf2022paf.day28.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static ibf2022paf.day28.Constants.*;

@Repository
public class AppsRepository {

    @Autowired
    private MongoTemplate template;

    // Create the DB operations in Mongo and convert them to Java
    public List<Document> getAppByCategory(){
        //Criteria criteria = Criteria.where(FIELD_RATING).is(Float.NaN);
        Criteria criteria = Criteria.where(FIELD_RATING).ne(Float.NaN);
        MatchOperation matchNonNaN = Aggregation.match(criteria);
        GroupOperation groupByCategory = Aggregation.group(FIELD_CATEGORY)
            .push(FIELD_APP).as(FIELD_APPS)
            //.avg(FIELD_RATING).as(FIELD_AVG_RATING);
            .and(FIELD_RATING,
                    AggregationExpression.from(
                        MongoExpression.create("""
                                $avg: "$Rating"
                                """)
                    )
            );  

        // SortOperation sortByCount = Aggregation.sort(
        //     Sort.by(Direction.ASC,FIELD_COUNT));

        // Aggregation pipeline = Aggregation.newAggregation(
        //     groupByCategory, sortByCount);

        // AggregationResults<Document> results = template.aggregate(
        //     pipeline, PLAYSTORE, Document.class);

        //return results.getMappedResults();

        Aggregation pipeline = Aggregation.newAggregation(matchNonNaN, groupByCategory);
        //Aggregation pipeline = Aggregation.newAggregation(matchNonNaN);
            
        // Returns a List of Documents
        return template.aggregate(pipeline, COLLECTION_APPLICATIONS, Document.class)
            .getMappedResults();
    }
}
