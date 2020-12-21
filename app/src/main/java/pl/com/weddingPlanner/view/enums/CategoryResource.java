package pl.com.weddingPlanner.view.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.exception.EnumValueNotFoundException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CategoryResource {

    MOST_IMPORTANT(CategoryEnum.MOST_IMPORTANT, R.string.category_most_important, "ic_dashboard"),
    CEREMONY(CategoryEnum.CEREMONY, R.string.category_most_ceremony, "ic_dashboard"),
    WEDDING_HALL(CategoryEnum.WEDDING_HALL, R.string.category_most_wedding_hall, "ic_dashboard"),
    SUBCONTRACTORS(CategoryEnum.SUBCONTRACTORS, R.string.category_most_subcontractors, "ic_dashboard"),
    STYLIZATION(CategoryEnum.STYLIZATION, R.string.category_most_stylization, "ic_dashboard"),
    FORMAL_DOCUMENTS(CategoryEnum.FORMAL_DOCUMENTS, R.string.category_most_formal_documents, "ic_dashboard"),
    ;

    private final CategoryEnum category;
    private final int resourceId;
    private final String iconCode;

    public static CategoryResource of(CategoryEnum categoryEnum) throws EnumValueNotFoundException {
        for (CategoryResource value : values()) {
            if (categoryEnum.equals(value.category))
                return value;
        }
        throw new EnumValueNotFoundException(categoryEnum.name());
    }
}