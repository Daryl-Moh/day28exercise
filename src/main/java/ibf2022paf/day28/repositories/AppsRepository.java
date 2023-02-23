package ibf2022paf.day28.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import static ibf2022paf.day28.Constants.*;

@Repository
public class AppsRepository {

    @Autowired
    private MongoTemplate template;

    public List<Document> groupAppByCategory(){

        Criteria criteria = Criteria.where(FIELD_RATING).ne(Float.NaN);
        MatchOperation matchNonNaN = Aggregration.match(criteria);
        GroupOperation groupByCategory = Aggregation.group(FIELD_CATEGORY)
            .push(FIELD_APP).as(FIELD_APPS)
            .and()

            
            .count().as(FIELD_COUNT);

        SortOperation sortByCount = Aggregation.sort(
            Sort.by(Direction.ASC,FIELD_COUNT));

        Aggregation pipeline = Aggregation.newAggregation(
            groupByCategory, sortByCount);

        AggregationResults<Document> results = template.aggregate(
            pipeline, PLAYSTORE, Document.class);
            
        return results.getMappedResults();
    }
}
