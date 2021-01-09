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
import pl.com.weddingPlanner.model.Assignees;
import pl.com.weddingPlanner.persistence.entity.Category;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Payment;
import pl.com.weddingPlanner.persistence.entity.Person;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.NavigationActivity;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.enums.CategoryTypeEnum;
import pl.com.weddingPlanner.view.enums.StateEnum;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.ResourceUtil;

import static pl.com.weddingPlanner.view.NavigationActivity.FRAGMENT_TO_LOAD_ID;
import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.ComponentsUtil.getIcon;
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
            if (StateEnum.AWAITING == StateEnum.valueOf(payment.getState())) {
                awaitingPaymentsSum += Double.parseDouble(payment.getAmount());
            } else if (StateEnum.PAID == StateEnum.valueOf(payment.getState())) {
                paidPaymentsSum += Double.parseDouble(payment.getAmount());
            }
        }
    }

    private void getAndSetPayers() {
        if (StringUtils.isNotBlank(expenseDetails.getPayers())) {
            String[] assigneesIds = expenseDetails.getPayers().split(",", -1);

            List<Person> payers = new ArrayList<>();
            for (String payerIdString : assigneesIds) {
                int payerId = Integer.parseInt(payerIdString);
                payers.add(DAOUtil.getPersonById(getContext(), payerId));
            }

            this.payersList = payers;
        }
    }

    private void setComponents() {

        binding.title.setText(expenseDetails.getTitle());
        binding.editDateText.setText(expenseDetails.getEditDate());

        setRecipient();
        setForWhat();
        setProgressBarAndText(paidPaymentsSum);
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

    private void setProgressBarAndText(double realExpensesSum) {
        double initialAmount = Double.parseDouble(expenseDetails.getInitialAmount());
        int percentage;

        if (initialAmount == 0) {
            if (realExpensesSum > 0) {
                percentage = 100;
            } else {
                percentage = 0;
            }
        } else {
            percentage = (int) (realExpensesSum / initialAmount * 100);
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
            Assignees assignees = new Assignees(getContext(), payersList);
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
    }

    private void setExpenseFloatingButtonListener() {
        binding.expenseFloatingButton.setOnClickListener(v -> {
            LinearLayout deleteLayout = binding.deleteLayout;
            LinearLayout editLayout = binding.editLayout;
            LinearLayout backgroundFade = binding.backgroundFade;

            if (deleteLayout.getVisibility() == View.GONE && editLayout.getVisibility() == View.GONE) {
                deleteLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.VISIBLE);
                backgroundFade.setVisibility(View.VISIBLE);
            } else {
                deleteLayout.setVisibility(View.GONE);
                editLayout.setVisibility(View.GONE);
                backgroundFade.setVisibility(View.GONE);
            }
        });
    }

    private void setDeleteExpenseListener() {
        binding.deleteLayout.setOnClickListener(v -> new QuestionDialog(getContext(), getResources().getString(R.string.expense_details_delete_question), getIntent()).showDialog());
    }

    public Intent getIntent() {
        Intent intent = new Intent(getContext(), NavigationActivity.class);
        intent.putExtra(FRAGMENT_TO_LOAD_ID, R.id.navigation_budget);
        intent.putExtra(EXPENSE_ID_EXTRA, expenseId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
