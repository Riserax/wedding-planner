package pl.com.weddingPlanner.view.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.FragmentExpenseDetailsBinding;
import pl.com.weddingPlanner.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.enums.LocationEnum;
import pl.com.weddingPlanner.enums.PaymentStateEnum;
import pl.com.weddingPlanner.view.component.Assignees;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.PersonUtil;
import pl.com.weddingPlanner.view.util.ResourceUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.dialog.QuestionDialog.CLASS_EXTRA;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.getIcon;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ResourceUtil.AMOUNT_ZERO;

public class ExpenseDetailsFragment extends Fragment {

    private FragmentExpenseDetailsBinding binding;

    private final int expenseId;

    private Expense expenseDetails;
    private Category categoryDetails;

    private double paidPaymentsSum = 0;
    private double awaitingPaymentsSum = 0;

    private List<Person> payersList = new ArrayList<>();

    public ExpenseDetailsFragment(int expenseId) {
        this.expenseId = expenseId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_expense_details, container, false);

        getAndSetData();
        setComponents();
        setListeners();

        return binding.getRoot();
    }

    private void getAndSetData() {
        expenseDetails = DAOUtil.getExpenseById(getContext(), expenseId);
        categoryDetails = DAOUtil.getCategoryByNameAndType(getContext(), expenseDetails.getCategory(), CategoryTypeEnum.BUDGET.name());

        getAndSetPayments();
        getAndSetPayers();
    }

    private void getAndSetPayments() {
        List<Payment> allPayments = DAOUtil.getAllPaymentsByExpenseId(getContext(), expenseId);

        for (Payment payment : allPayments) {
            if (PaymentStateEnum.PENDING == PaymentStateEnum.valueOf(payment.getState())) {
                awaitingPaymentsSum += Double.parseDouble(payment.getAmount());
            } else if (PaymentStateEnum.PAID == PaymentStateEnum.valueOf(payment.getState())) {
                paidPaymentsSum += Double.parseDouble(payment.getAmount());
            }
        }
    }

    private void getAndSetPayers() {
        this.payersList = PersonUtil.getPersonsList(getContext(), expenseDetails.getPayers());
    }

    private void setComponents() {

        binding.title.setText(expenseDetails.getTitle());
        binding.editDateText.setText(expenseDetails.getEditDate());

        setRecipient();
        setForWhat();
        setProgressBarAndText();
        setInitialAmount();
        setRealExpenses();
        setAwaitingPayments();
        setPayers();
        setCategory();
    }

    private void setRecipient() {
        if (StringUtils.isNotBlank(expenseDetails.getRecipient())) {
            binding.recipient.setText(expenseDetails.getRecipient());
        } else {
            binding.recipient.setText(getResources().getString(R.string.no_description));
        }
    }

    private void setForWhat() {
        if (StringUtils.isNotBlank(expenseDetails.getForWhat())) {
            binding.forWhat.setText(expenseDetails.getForWhat());
        } else {
            binding.forWhat.setText(getResources().getString(R.string.no_description));
        }
    }

    private void setProgressBarAndText() {
        double initialAmount = Double.parseDouble(expenseDetails.getInitialAmount());
        int percentage;

        if (initialAmount == 0) {
            if (paidPaymentsSum > 0) {
                percentage = 100;
            } else {
                percentage = 0;
            }
        } else {
            percentage = (int) (paidPaymentsSum / initialAmount * 100);
        }

        String progress = percentage + "%";

        binding.progressBar.setProgress(percentage, true);
        binding.txtProgress.setText(progress);
    }

    private void setInitialAmount() {
        if (StringUtils.isNotBlank(expenseDetails.getInitialAmount())) {
            binding.initialAmount.setText(FormatUtil.convertToAmount(expenseDetails.getInitialAmount()));
        } else {
            binding.initialAmount.setText(FormatUtil.convertToAmount(AMOUNT_ZERO));
        }
    }

    private void setRealExpenses() {
        binding.realExpenses.setText(FormatUtil.convertToAmount(paidPaymentsSum));
    }

    private void setAwaitingPayments() {
        binding.awaitingPayments.setText(FormatUtil.convertToAmount(awaitingPaymentsSum));
    }

    private void setPayers() {
        if (StringUtils.isNotBlank(expenseDetails.getPayers())) {
            Assignees assignees = new Assignees(getContext(), payersList, LocationEnum.DETAILS);
            binding.payersLayout.addView(assignees.getAssigneesContainer());
        } else {
            binding.noPayers.setVisibility(View.VISIBLE);
        }
    }

    private void setCategory() {
        binding.categoryIcon.setImageDrawable(getIcon(getContext(), ResourceUtil.getResId(categoryDetails.getIconId(), R.drawable.class)));
        binding.categoryName.setText(expenseDetails.getCategory());
    }

    private void setListeners() {
        setExpenseFloatingButtonListener();
        setDeleteExpenseListener();
        setEditExpenseListener();
    }

    private void setExpenseFloatingButtonListener() {
        binding.expenseFloatingButton.setOnClickListener(v -> {
            LinearLayout deleteLayout = binding.deleteLayout;
            LinearLayout editLayout = binding.editLayout;

            if (deleteLayout.getVisibility() == View.GONE && editLayout.getVisibility() == View.GONE) {
                showFloatingMenu();
            } else {
                hideFloatingMenu();
            }
        });
    }

    private void setDeleteExpenseListener() {
        binding.deleteLayout.setOnClickListener(v -> {
            new QuestionDialog(getContext(), getResources().getString(R.string.expense_details_delete_question), getDeleteIntent()).showDialog();
            hideFloatingMenu();
        });
    }

    private void setEditExpenseListener() {
        binding.editLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewExpenseActivity.class);
            intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
            intent.putExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_expense_details);
            startActivity(intent);

            hideFloatingMenu();
        });
    }

    private void showFloatingMenu() {
        binding.deleteLayout.setVisibility(View.VISIBLE);
        binding.editLayout.setVisibility(View.VISIBLE);
        binding.backgroundFade.setVisibility(View.VISIBLE);
    }

    private void hideFloatingMenu() {
        binding.deleteLayout.setVisibility(View.GONE);
        binding.editLayout.setVisibility(View.GONE);
        binding.backgroundFade.setVisibility(View.GONE);
    }

    public Intent getDeleteIntent() {
        Intent intent = new Intent(getContext(), NavigationActivity.class);
        intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_budget);
        intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
        intent.putExtra(CLASS_EXTRA, ExpenseDetailsFragment.class.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
