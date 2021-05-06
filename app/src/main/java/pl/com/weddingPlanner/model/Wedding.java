package pl.com.weddingPlanner.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wedding {
    private String id;
    private String name;
    private String date;
    private String time;
    private String ceremonyVenue;
    private String banquetVenue;
    private Integer initialBudget;
    private String ownerUid;
    private String partnerUid;
    private String partnerName;
    private String partnerEmail;
    private String people;
    private String creationDate;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("date", date);
        result.put("time", time);
        result.put("ceremonyVenue", ceremonyVenue);
        result.put("banquetVenue", banquetVenue);
        result.put("initialBudget", initialBudget);
        result.put("ownerUid", ownerUid);
        result.put("partnerUid", partnerUid);
        result.put("partnerName", partnerName);
        result.put("partnerEmail", partnerEmail);
        result.put("people", people);
        result.put("creationDate", creationDate);

        return result;
    }
}
