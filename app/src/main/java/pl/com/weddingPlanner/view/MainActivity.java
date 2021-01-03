package pl.com.weddingPlanner.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pl.com.weddingPlanner.persistence.entity.Bookmark;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.persistence.entity.SubTask;
import pl.com.weddingPlanner.persistence.entity.Task;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fillDatabaseIfEmpty();
        goToActivity();
    }

    private void fillDatabaseIfEmpty() {
        List<Category> categories = DAOUtil.getAllCategories(this);
        List<Person> people = DAOUtil.getAllPersons(this);

        if (categories.isEmpty() && people.isEmpty()) {
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
                .iconId("ic_dashboard")
                .build();

        Category categoryTasks3 = Category.builder()
                .name("Sala weselna")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_dashboard")
                .build();

        Category categoryTasks4 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_engineering")
                .build();

        Category categoryTasks5 = Category.builder()
                .name("Stylizacje P&P")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_style")
                .build();

        Category categoryTasks6 = Category.builder()
                .name("Dokumenty formalne")
                .type(CategoryTypeEnum.TASKS.name())
                .iconId("ic_assignment")
                .build();

        Category categoryTasks7 = Category.builder()
                .name("Inne")
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
                .iconId("ic_dashboard")
                .build();

        Category categoryBudget2 = Category.builder()
                .name("Sala weselna")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_dashboard")
                .build();

        Category categoryBudget3 = Category.builder()
                .name("Podwykonawcy")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_engineering")
                .build();

        Category categoryBudget4 = Category.builder()
                .name("Stylizacje")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_style")
                .build();

        Category categoryBudget5 = Category.builder()
                .name("Dekoracje")
                .type(CategoryTypeEnum.BUDGET.name())
                .iconId("ic_dashboard")
                .build();

        Category categoryBudget6 = Category.builder()
                .name("Inne")
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
                .iconId("ic_dashboard")
                .build();

        Category categorySubcontractors2 = Category.builder()
                .name("Bankiet")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_dashboard")
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
                .iconId("ic_dashboard")
                .build();

        Category categorySubcontractors6 = Category.builder()
                .name("Stylizacje")
                .type(CategoryTypeEnum.SUBCONTRACTORS.name())
                .iconId("ic_style")
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
                .iconId("ic_dashboard")
                .build();

        Category categorySubcontractors10 = Category.builder()
                .name("Inne")
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
                .category("Różne")
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
}