package pl.com.weddingPlanner.view.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;

import static pl.com.weddingPlanner.view.util.ListViewUtil.getSeparatedString;

public class PersonUtil {

    public static List<Person> getPersonsList(Context context, String personsString) {
        List<Person> persons = new ArrayList<>();

        if (StringUtils.isNotBlank(personsString)) {
            String[] personsIds = personsString.split(",", -1);

            for (String personIdString : personsIds) {
                int personId = Integer.parseInt(personIdString);
                persons.add(DAOUtil.getPersonById(context, personId));
            }
        }

        return persons;
    }

    public static String getPersonsStringFromList(Context context, String personsString) {
        List<Person> persons = getPersonsList(context, personsString);

        List<String> personsNames = new ArrayList<>();
        for (Person person : persons) {
            personsNames.add(person.getName());
        }

        return getSeparatedString(personsNames);
    }
}
