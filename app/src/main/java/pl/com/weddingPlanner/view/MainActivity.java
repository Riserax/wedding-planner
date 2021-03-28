package pl.com.weddingPlanner.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.enums.CategoryType;
import pl.com.weddingPlanner.enums.PaymentState;
import pl.com.weddingPlanner.persistence.entity.AgeRange;
import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Guest;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.persistence.entity.Table;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.persistence.entity.Wedding;
import pl.com.weddingPlanner.util.DAOUtil;

import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar_Launcher);
        super.onCreate(savedInstanceState);

        fillDatabaseIfEmpty();
        forwardToAppropriateActivity();
    }

    private void fillDatabaseIfEmpty() {
        int categoriesCount = DAOUtil.getCategoriesCount(this);
        int peopleCount = DAOUtil.getPersonsCount(this);

        if (categoriesCount == 0 && peopleCount == 0) {
            fillDatabase();
        }
    }

    private void forwardToAppropriateActivity() {
        startApplication();
    }

    private void startApplication() {
        Intent intent = IntroActivity.createIntent(this);
        startActivity(intent);
    }

    private void fillDatabase() {
        insertWedding();
        insertCategories();
        insertBookmarks();
        insertPeople();
        insertTasks();
        insertSubTasks();
        insertExpenses();
        insertPayments();
        insertAgeRanges();
        insertTables();
        insertGuests();
        insertSubcontractors();
    }

    private void insertWedding() {
        Wedding wedding = Wedding.builder()
                .id(1)
                .name("Cały Ten Ślub")
                .date("2023-08-26")
                .time("13:00:00")
                .ceremonyVenue("Kościółek w starym stylu, gdzieś niedaleko sali")
                .banquetVenue("Sala Zielone Wzgórze")
                .people("1,2")
                .initialTotalAmount("40000.00")
                .creationDate("2020-09-14")
                .build();

        DAOUtil.insertWedding(this, wedding);
    }

    private void insertCategories() {
        insertTasksCategories();
        insertBudgetCategories();
        insertGuestsCategories();
        insertSubcontractorsCategories();
    }

    private void insertTasksCategories() {
        Category categoryTasks1 = Category.builder()
                .name("Najważniejsze")
                .type(CategoryType.TASKS.name())
                .iconId("ic_star")
                .build();

        Category categoryTasks2 = Category.builder()
                .name("Ceremonia")
                .type(CategoryType.TASKS.name())
                .iconId("ic_favorite")
                .build();

        Category categoryTasks3 = Category.builder()
                .name("Bankiet")
                .type(CategoryType.TASKS.name())
                .iconId("ic_local_bar")
                .build();

        Category categoryTasks4 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryType.TASKS.name())
                .iconId("ic_engineering")
                .build();

        Category categoryTasks5 = Category.builder()
                .name("Stylizacje P&P")
                .type(CategoryType.TASKS.name())
                .iconId("ic_checkroom")
                .build();

        Category categoryTasks6 = Category.builder()
                .name("Dokumenty formalne")
                .type(CategoryType.TASKS.name())
                .iconId("ic_assignment")
                .build();

        Category categoryTasks7 = Category.builder()
                .name(CATEGORY_OTHER)
                .type(CategoryType.TASKS.name())
                .iconId("ic_add_box")
                .build();

        DAOUtil.insertCategory(this, categoryTasks1);
        DAOUtil.insertCategory(this, categoryTasks2);
        DAOUtil.insertCategory(this, categoryTasks3);
        DAOUtil.insertCategory(this, categoryTasks4);
        DAOUtil.insertCategory(this, categoryTasks5);
        DAOUtil.insertCategory(this, categoryTasks6);
        DAOUtil.insertCategory(this, categoryTasks7);
    }

    private void insertBudgetCategories() {
        Category categoryBudget1 = Category.builder()
                .name("Ceremonia")
                .type(CategoryType.BUDGET.name())
                .iconId("ic_favorite")
                .build();

        Category categoryBudget2 = Category.builder()
                .name("Bankiet")
                .type(CategoryType.BUDGET.name())
                .iconId("ic_local_bar")
                .build();

        Category categoryBudget3 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryType.BUDGET.name())
                .iconId("ic_engineering")
                .build();

        Category categoryBudget4 = Category.builder()
                .name("Stylizacje")
                .type(CategoryType.BUDGET.name())
                .iconId("ic_checkroom")
                .build();

        Category categoryBudget5 = Category.builder()
                .name("Dekoracje")
                .type(CategoryType.BUDGET.name())
                .iconId("ic_style")
                .build();

        Category categoryBudget6 = Category.builder()
                .name(CATEGORY_OTHER)
                .type(CategoryType.BUDGET.name())
                .iconId("ic_add_box")
                .build();

        DAOUtil.insertCategory(this, categoryBudget1);
        DAOUtil.insertCategory(this, categoryBudget2);
        DAOUtil.insertCategory(this, categoryBudget3);
        DAOUtil.insertCategory(this, categoryBudget4);
        DAOUtil.insertCategory(this, categoryBudget5);
        DAOUtil.insertCategory(this, categoryBudget6);
    }

    private void insertGuestsCategories() {
        Category categoryGuests1 = Category.builder()
                .name("Rodzina Młodej Pary")
                .type(CategoryType.GUESTS.name())
                .iconId("ic_dashboard")
                .build();

        Category categoryGuests2 = Category.builder()
                .name("Znajomi Młodej Pary")
                .type(CategoryType.GUESTS.name())
                .iconId("ic_dashboard")
                .build();

        Category categoryGuests3 = Category.builder()
                .name("Osoby towarzyszące")
                .type(CategoryType.GUESTS.name())
                .iconId("ic_dashboard")
                .build();

        Category categoryGuests4 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryType.GUESTS.name())
                .iconId("ic_dashboard")
                .build();

        DAOUtil.insertCategory(this, categoryGuests1);
        DAOUtil.insertCategory(this, categoryGuests2);
        DAOUtil.insertCategory(this, categoryGuests3);
        DAOUtil.insertCategory(this, categoryGuests4);
    }

    private void insertSubcontractorsCategories() {
        Category categorySubcontractors1 = Category.builder()
                .name("Ceremonia")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_favorite")
                .build();

        Category categorySubcontractors2 = Category.builder()
                .name("Bankiet")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_local_bar")
                .build();

        Category categorySubcontractors3 = Category.builder()
                .name("Atrakcje")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_stars")
                .build();

        Category categorySubcontractors4 = Category.builder()
                .name("Foto & Wideo")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_camera")
                .build();

        Category categorySubcontractors5 = Category.builder()
                .name("Dekoracje")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_style")
                .build();

        Category categorySubcontractors6 = Category.builder()
                .name("Stylizacje")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_checkroom")
                .build();

        Category categorySubcontractors7 = Category.builder()
                .name("Transport")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_transportation")
                .build();

        Category categorySubcontractors8 = Category.builder()
                .name("Nocleg")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_hotel")
                .build();

        Category categorySubcontractors9 = Category.builder()
                .name("Akcesoria")
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_loupe")
                .build();

        Category categorySubcontractors10 = Category.builder()
                .name(CATEGORY_OTHER)
                .type(CategoryType.SUBCONTRACTORS.name())
                .iconId("ic_add_box")
                .build();

        DAOUtil.insertCategory(this, categorySubcontractors1);
        DAOUtil.insertCategory(this, categorySubcontractors2);
        DAOUtil.insertCategory(this, categorySubcontractors3);
        DAOUtil.insertCategory(this, categorySubcontractors4);
        DAOUtil.insertCategory(this, categorySubcontractors5);
        DAOUtil.insertCategory(this, categorySubcontractors6);
        DAOUtil.insertCategory(this, categorySubcontractors7);
        DAOUtil.insertCategory(this, categorySubcontractors8);
        DAOUtil.insertCategory(this, categorySubcontractors9);
        DAOUtil.insertCategory(this, categorySubcontractors10);
    }

    private void insertBookmarks() {
        Bookmark bookmarkImportant = Bookmark.builder()
                .name("Pilne")
                .colorId("#D60707")
                .build();

        Bookmark bookmarkSoon = Bookmark.builder()
                .name("Wkrótce")
                .colorId("#F99F1F")
                .build();

        Bookmark bookmarkContinuous = Bookmark.builder()
                .name("Ciągłe")
                .colorId("#0083DA")
                .build();

        Bookmark bookmarkPaid = Bookmark.builder()
                .name("Opłacone")
                .colorId("#018786")
                .build();

        DAOUtil.insertBookmark(this, bookmarkImportant);
        DAOUtil.insertBookmark(this, bookmarkSoon);
        DAOUtil.insertBookmark(this, bookmarkContinuous);
        DAOUtil.insertBookmark(this, bookmarkPaid);
    }

    public void insertPeople() {
        Person personMH = Person.builder()
                .name("Mag Hon")
                .role(1)
                .build();

        Person personKC = Person.builder()
                .name("Kam Cal")
                .role(1)
                .build();

        Person personOG = Person.builder()
                .name("Ol Grusz")
                .role(2)
                .build();

        Person personKS = Person.builder()
                .name("Kam Smol")
                .role(2)
                .build();

        DAOUtil.insertPerson(this, personMH);
        DAOUtil.insertPerson(this, personKC);
        DAOUtil.insertPerson(this, personOG);
        DAOUtil.insertPerson(this, personKS);
    }

    private void insertTasks() {
        Task taskFilled = Task.builder()
                .id(1)
                .title("Uzupełnione zadanie")
                .category("Najważniejsze")
                .description("Wielolinijkowy opis zadania, który może mieć maksymalnie 250 znaków, ale ten tyle nie ma, więc muszę go troszkę przeciągnąć, lejąc wodę o niczym")
                .bookmarks("1,2,3,4")
                .assignees("1,2,3")
                .status("NEW")
                .date("2021-03-23")
                .time("16:30")
                .build();

        Task taskNotFilled = Task.builder()
                .id(2)
                .title("Nieuzupełnione zadanie")
                .category(CATEGORY_OTHER)
                .status("DONE")
                .date("2021-03-31")
                .build();

        DAOUtil.insertTask(this, taskFilled);
        DAOUtil.insertTask(this, taskNotFilled);
    }

    private void insertSubTasks() {
        SubTask subTask1 = SubTask.builder()
                .taskId(1)
                .name("Pierwsze podzadanie pierwszego zadania")
                .build();

        SubTask subTask2 = SubTask.builder()
                .taskId(1)
                .name("Drugie podzadanie pierwszego zadania")
                .build();

        DAOUtil.insertSubTask(this, subTask1);
        DAOUtil.insertSubTask(this, subTask2);
    }

    private void insertExpenses() {
        Expense expense1 = Expense.builder()
                .title("Wypełniony wydatek")
                .initialAmount("25000.00")
                .category("Bankiet")
                .recipient("Sala weselna \"Zielone wzgórze\"")
                .forWhat("Sala weselna, goście")
                .payers("1,2")
                .subcontractorId(1)
                .subExpenses("1,2,3")
                .editDate("2021-01-03 21:40")
                .build();

        Expense expense2 = Expense.builder()
                .title("Niewypełniony wydatek")
                .initialAmount("0.00")
                .category(CATEGORY_OTHER)
                .subcontractorId(0)
                .editDate("2020-12-30 16:16")
                .build();

        DAOUtil.insertExpense(this, expense1);
        DAOUtil.insertExpense(this, expense2);
    }

    private void insertPayments() {
        Payment payment1 = Payment.builder()
                .expenseId(1)
                .title("Zaliczka")
                .date("2021-01-31")
                .amount("2000.00")
                .payer("1")
                .state(PaymentState.PAID.name())
                .info("2000 zł")
                .build();

        Payment payment2 = Payment.builder()
                .expenseId(1)
                .title("Pierwsza rata")
                .date("2023-03-31")
                .amount("12500.00")
                .payer("2")
                .state(PaymentState.PENDING.name())
                .info("Połowa całości (minus zaliczka)")
                .build();

        DAOUtil.insertPayment(this, payment1);
        DAOUtil.insertPayment(this, payment2);
    }

    private void insertAgeRanges() {
        AgeRange ageRange0 = AgeRange.builder()
                .range("1-5")
                .build();

        AgeRange ageRange1 = AgeRange.builder()
                .range("6-10")
                .build();

        AgeRange ageRange2 = AgeRange.builder()
                .range("11-14")
                .build();

        AgeRange ageRange3 = AgeRange.builder()
                .range("15-18")
                .build();

        AgeRange ageRange4 = AgeRange.builder()
                .range("19-25")
                .build();

        AgeRange ageRange5 = AgeRange.builder()
                .range("26-35")
                .build();

        AgeRange ageRange6 = AgeRange.builder()
                .range("36-45")
                .build();

        AgeRange ageRange7 = AgeRange.builder()
                .range("46-60")
                .build();

        AgeRange ageRange8 = AgeRange.builder()
                .range("60+")
                .build();

        DAOUtil.insertAgeRange(this, ageRange0);
        DAOUtil.insertAgeRange(this, ageRange1);
        DAOUtil.insertAgeRange(this, ageRange2);
        DAOUtil.insertAgeRange(this, ageRange3);
        DAOUtil.insertAgeRange(this, ageRange4);
        DAOUtil.insertAgeRange(this, ageRange5);
        DAOUtil.insertAgeRange(this, ageRange6);
        DAOUtil.insertAgeRange(this, ageRange7);
        DAOUtil.insertAgeRange(this, ageRange8);
    }

    private void insertTables() {
        Table table1 = Table.builder()
                .number(1)
                .name("Stół Młodej Pary")
                .capacity(2)
                .build();

        Table table2 = Table.builder()
                .number(2)
                .capacity(10)
                .build();

        DAOUtil.insertTable(this, table1);
        DAOUtil.insertTable(this, table2);
    }

    private void insertGuests() {
        Guest guest1 = Guest.builder()
                .type("GUEST")
                .connectedWithId(0)
                .nameSurname("Jan Kowalski")
                .tableNumber(1)
                .presence("NONE")
                .build();

        Guest guest2 = Guest.builder()
                .type("GUEST")
                .connectedWithId(3)
                .nameSurname("Adam Nowak")
                .ageRange("60+")
                .tableNumber(1)
                .presence("NONE")
                .build();

        Guest accompany1 = Guest.builder()
                .type("ACCOMPANY")
                .connectedWithId(2)
                .nameSurname("Krystyna Uzupełniona")
                .ageRange("46-60")
                .category("Osoby towarzyszące")
                .tableNumber(2)
                .presence("AWAITING")
                .contact("tel: 666 777 888, mail: krysia@poczta.pl")
                .notes("Vege, no-gluten, uczulona na orzechy")
                .build();

        DAOUtil.insertGuest(this, guest1);
        DAOUtil.insertGuest(this, guest2);
        DAOUtil.insertGuest(this, accompany1);
    }

    private void insertSubcontractors() {
        Subcontractor subcontractor1 = Subcontractor.builder()
                .name("Sala Zielone Wzgórze")
                .category("Bankiet")
                .email("zielona43@gmail.com")
                .phone("785 134 822")
                .website("<a href=\"https://salazielonewzgorze.pl\">https://salazielonewzgorze.pl</a>")
                .address("Zielona 43, Koniusza, Kraków")
                .collaborationStage("CONFIRMED")
                .expenseId(1)
                .cost("25000.00")
                .notes("Zapłacona zaliczka")
                .build();

        Subcontractor subcontractor2 = Subcontractor.builder()
                .name("Mateusz Gumula Photography")
                .category("Foto & Wideo")
                .email("kontakt@mateuszgumula.pl")
                .phone("604 052 252")
                .website("<a href=\"http://www.mateuszgumula.pl\">http://www.mateuszgumula.pl</a>")
                .collaborationStage("CONFIRMED")
                .expenseId(0)
                .cost("3000.00")
                .build();

        DAOUtil.insertSubcontractor(this, subcontractor1);
        DAOUtil.insertSubcontractor(this, subcontractor2);
    }
}