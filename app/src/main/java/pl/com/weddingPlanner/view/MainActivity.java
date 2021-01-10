package pl.com.weddingPlanner.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.enums.StateEnum;

import static pl.com.weddingPlanner.view.util.ResourceUtil.CATEGORY_OTHER;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fillDatabaseIfEmpty();
        goToActivity();
    }

    private void fillDatabaseIfEmpty() {
        int categoriesCount = DAOUtil.getCategoriesCount(this);
        int peopleCount = DAOUtil.getPersonsCount(this);

        if (categoriesCount == 0 && peopleCount == 0) {
            fillDatabase();
        }
    }

    private void goToActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    private void fillDatabase() {
        insertCategories();
        insertBookmarks();
        insertPeople();
        insertTasks();
        insertSubTasks();
        insertExpenses();
        insertPayments();
    }

    private void insertCategories() {
        insertTasksCategories();
        insertBudgetCategories();
        insertSubcontractorsCategories();
    }

    private void insertTasksCategories() {
        Category categoryTasks1 = Category.builder()
                .name("Najważniejsze")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_star")
                .build();

        Category categoryTasks2 = Category.builder()
                .name("Ceremonia")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_favorite")
                .build();

        Category categoryTasks3 = Category.builder()
                .name("Bankiet")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_local_bar")
                .build();

        Category categoryTasks4 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_engineering")
                .build();

        Category categoryTasks5 = Category.builder()
                .name("Stylizacje P&P")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_checkroom")
                .build();

        Category categoryTasks6 = Category.builder()
                .name("Dokumenty formalne")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_assignment")
                .build();

        Category categoryTasks7 = Category.builder()
                .name(CATEGORY_OTHER)
                .type(CategoryTypeEnum.TASKS.name())
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
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_favorite")
                .build();

        Category categoryBudget2 = Category.builder()
                .name("Bankiet")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_local_bar")
                .build();

        Category categoryBudget3 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_engineering")
                .build();

        Category categoryBudget4 = Category.builder()
                .name("Stylizacje")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_checkroom")
                .build();

        Category categoryBudget5 = Category.builder()
                .name("Dekoracje")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_style")
                .build();

        Category categoryBudget6 = Category.builder()
                .name(CATEGORY_OTHER)
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_add_box")
                .build();

        DAOUtil.insertCategory(this, categoryBudget1);
        DAOUtil.insertCategory(this, categoryBudget2);
        DAOUtil.insertCategory(this, categoryBudget3);
        DAOUtil.insertCategory(this, categoryBudget4);
        DAOUtil.insertCategory(this, categoryBudget5);
        DAOUtil.insertCategory(this, categoryBudget6);
    }

    private void insertSubcontractorsCategories() {
        Category categorySubcontractors1 = Category.builder()
                .name("Ceremonia")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_favorite")
                .build();

        Category categorySubcontractors2 = Category.builder()
                .name("Bankiet")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_local_bar")
                .build();

        Category categorySubcontractors3 = Category.builder()
                .name("Atrakcje")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_stars")
                .build();

        Category categorySubcontractors4 = Category.builder()
                .name("Foto & Wideo")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_camera")
                .build();

        Category categorySubcontractors5 = Category.builder()
                .name("Dekoracje")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_style")
                .build();

        Category categorySubcontractors6 = Category.builder()
                .name("Stylizacje")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_checkroom")
                .build();

        Category categorySubcontractors7 = Category.builder()
                .name("Transport")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_transportation")
                .build();

        Category categorySubcontractors8 = Category.builder()
                .name("Nocleg")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_hotel")
                .build();

        Category categorySubcontractors9 = Category.builder()
                .name("Akcesoria")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_loupe")
                .build();

        Category categorySubcontractors10 = Category.builder()
                .name(CATEGORY_OTHER)
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
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
                .date("2021-01-23")
                .time("16:30")
                .subTasks("1,2")
                .build();

        Task taskNotFilled = Task.builder()
                .id(2)
                .title("Nieuzupełnione zadanie")
                .category(CATEGORY_OTHER)
                .date("2021-01-31")
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
                .subExpenses("1,2,3")
                .editDate("2021-01-03 21:40")
                .build();

        Expense expense2 = Expense.builder()
                .title("Niewypełniony wydatek")
                .initialAmount("0.00")
                .category(CATEGORY_OTHER)
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
                .state(StateEnum.PAID.name())
                .info("2000 zł")
                .build();

        Payment payment2 = Payment.builder()
                .expenseId(1)
                .title("Pierwsza rata")
                .date("2023-03-31")
                .amount("12500.00")
                .payer("2")
                .state(StateEnum.PENDING.name())
                .info("Połowa całości (minus zaliczka)")
                .build();

        DAOUtil.insertPayment(this, payment1);
        DAOUtil.insertPayment(this, payment2);
    }
}