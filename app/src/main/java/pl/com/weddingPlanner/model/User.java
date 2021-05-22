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
public class User {
    public String username;
    public String email;
    public List<WeddingItem> weddings;
    public String currentWedding;
    public List<String> invitedPeople;
}
