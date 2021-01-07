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

public class QuestionDialog extends CustomAlertDialog {

    private DialogQuestionBinding binding;

    public QuestionDialog(BaseActivity activity, String question) {
        super(activity, R.layout.dialog_question);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_question, null, false);

        setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
            activity.executeQuestionDialog();
        });
        setNegativeButton(R.string.dialog_no, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
        setText(question);
    }

    public QuestionDialog(Context context, String question, Intent intent) {
        super(context, R.layout.dialog_question);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_question, null, false);

        setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
            DAOUtil.deleteExpenseById(getContext(), getExpenseId(intent));
            context.startActivity(intent);
        });
        setNegativeButton(R.string.dialog_no, (dialog, which) -> {});

        setView(binding.getRoot()).setCancelable(true);
        setText(question);
    }

    private int getExpenseId(Intent intent) {
        return intent.getIntExtra(EXPENSE_ID_EXTRA, 0);
    }

    private void setText(String question) {
        binding.question.setText(question);
    }

    @Override
    public void showDialog() {
        super.showTwoButtonDialog();
    }
}
