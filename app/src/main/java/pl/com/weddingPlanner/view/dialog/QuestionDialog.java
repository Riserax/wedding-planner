package pl.com.weddingPlanner.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.DialogQuestionBinding;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;

import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.budget.ExpensePaymentsFragment.PAYMENT_ID_EXTRA;

public class QuestionDialog extends CustomAlertDialog {

    public static final String CLASS_EXTRA = "classExtra";
    private final String classExpenseDetailsFragment = "class pl.com.weddingPlanner.view.budget.ExpenseDetailsFragment";
    private final String classNewPaymentActivity = "class pl.com.weddingPlanner.view.budget.NewPaymentActivity";

    private DialogQuestionBinding binding;

    public QuestionDialog(BaseActivity activity, String question) {
        super(activity, R.layout.dialog_question);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_question, null, false);

        setPositiveButton(R.string.dialog_yes, (dialog, which) -> activity.executeQuestionDialog());
        setNegativeButton(R.string.dialog_no, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
        setText(question);
    }

    public QuestionDialog(Context context, String question, Intent intent) {
        super(context, R.layout.dialog_question);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_question, null, false);

        setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
            deleteItem(intent);
            context.startActivity(intent);
        });
        setNegativeButton(R.string.dialog_no, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
        setText(question);
    }

    private void setText(String question) {
        binding.question.setText(question);
    }

    private void deleteItem(Intent intent) {
        int expenseId = getExpenseId(intent);
        int paymentId = getPaymentId(intent);
        String classExtra = getClassExtra(intent);

        if (classExpenseDetailsFragment.equals(classExtra)) {
            DAOUtil.deleteExpenseById(getContext(), expenseId);
            DAOUtil.deleteAllPaymentsByExpenseId(getContext(), expenseId);
        } else if (classNewPaymentActivity.equals(classExtra)) {
            DAOUtil.deletePaymentById(getContext(), paymentId);
        }
    }

    private int getExpenseId(Intent intent) {
        return intent.getIntExtra(EXPENSE_ID_EXTRA, 0);
    }

    private int getPaymentId(Intent intent) {
        return intent.getIntExtra(PAYMENT_ID_EXTRA, 0);
    }

    private String getClassExtra(Intent intent) {
        return intent.getStringExtra(CLASS_EXTRA);
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
