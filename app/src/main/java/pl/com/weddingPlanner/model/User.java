package pl.com.weddingPlanner.model;

import com.google.firebase.database.IgnoreExtraProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
}
