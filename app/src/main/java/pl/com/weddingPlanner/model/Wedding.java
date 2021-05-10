package pl.com.weddingPlanner.model;

import java.util.List;

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
    private String initialBudget;
    private String ownerUid;
    private String partnerUid;
    private String partnerName;
    private String partnerEmail;
    private List<String> people;
    private String joinCode;
    private String creationDate;
}
