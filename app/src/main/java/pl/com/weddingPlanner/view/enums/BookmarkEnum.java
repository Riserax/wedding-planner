package pl.com.weddingPlanner.view.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookmarkEnum {

    IMPORTANT("Ważne"),
    CONTINUES("Ciągłe"),
    SOON("Wkrótce"),
    DONE("Załatwione"),
    PAYED("Opłacone"),
    ;

    private String value;
}
