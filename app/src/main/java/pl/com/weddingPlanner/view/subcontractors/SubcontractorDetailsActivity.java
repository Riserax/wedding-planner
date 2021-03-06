package pl.com.weddingPlanner.view.subcontractors;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.R;
import pl.com.weddingPlanner.databinding.ActivitySubcontractorDetailsBinding;
import pl.com.weddingPlanner.enums.CollaborationStage;
import pl.com.weddingPlanner.persistence.entity.Expense;
import pl.com.weddingPlanner.persistence.entity.Subcontractor;
import pl.com.weddingPlanner.util.DAOUtil;
import pl.com.weddingPlanner.view.BaseActivity;
import pl.com.weddingPlanner.view.budget.ExpenseActivity;
import pl.com.weddingPlanner.view.dialog.QuestionDialog;
import pl.com.weddingPlanner.view.util.FormatUtil;
import pl.com.weddingPlanner.view.util.LinksUtil;

import static pl.com.weddingPlanner.view.budget.ExpenseActivity.EXPENSE_ID_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.ACTIVITY_TITLE_EXTRA;
import static pl.com.weddingPlanner.view.util.ExtraUtil.SUBCONTRACTOR_ID_EXTRA;

public class SubcontractorDetailsActivity extends BaseActivity {

    private ActivitySubcontractorDetailsBinding binding;

    private Subcontractor subcontractorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subcontractor_details);

        getAndSetGuestDetails();

        setActivityToolbarContentWithBackIcon(R.string.header_title_subcontractor);

        setComponents();
        setListeners();
    }

    private void getAndSetGuestDetails() {
        int subcontractorId = getIntent().getExtras().getInt(SUBCONTRACTOR_ID_EXTRA, 0);
        subcontractorDetails = DAOUtil.getSubcontractorById(this, subcontractorId);
    }

    private void setComponents() {
        binding.name.setText(subcontractorDetails.getName());
        binding.category.setText(subcontractorDetails.getCategory());

        setEmail();
        setPhone();
        setWebsite();
        setAddress();
        setCollaborationStage();
        setCost();
        setNotes();
        setConnectedExpense();
    }

    private void setEmail() {
        if (StringUtils.isNotBlank(subcontractorDetails.getEmail())) {
            LinksUtil.makeLinkAlike(binding.email, this, subcontractorDetails.getEmail());
        } else {
            binding.email.setText(getString(R.string.field_not_specified));
        }
    }

    private void setPhone() {
        if (StringUtils.isNotBlank(subcontractorDetails.getPhone())) {
            LinksUtil.makeLinkAlike(binding.phone, this, subcontractorDetails.getPhone());
        } else {
            binding.phone.setText(getString(R.string.field_not_specified));
        }
    }

    private void setWebsite() {
        if (StringUtils.isNotBlank(subcontractorDetails.getWebsite())) {
            binding.www.setMovementMethod(LinkMovementMethod.getInstance());
            binding.www.setText(Html.fromHtml(subcontractorDetails.getWebsite(), 0));
        } else {
            binding.www.setText(getString(R.string.field_not_specified));
        }
    }

    private void setAddress() {
        if (StringUtils.isNotBlank(subcontractorDetails.getAddress())) {
            binding.address.setText(subcontractorDetails.getAddress());
        } else {
            binding.address.setText(getString(R.string.field_not_specified));
        }
    }

    private void setCollaborationStage() {
        if (!CollaborationStage.NONE.name().equals(subcontractorDetails.getCollaborationStage())) {
            int resourceId = CollaborationStage.valueOf(subcontractorDetails.getCollaborationStage()).getTextResourceId();
            binding.collaborationStage.setText(getString(resourceId));
        } else {
            binding.collaborationStage.setText(getString(R.string.field_not_specified));
        }
    }

    private void setCost() {
        if (StringUtils.isNotBlank(subcontractorDetails.getCost())) {
            binding.cost.setText(FormatUtil.convertToAmount(subcontractorDetails.getCost()));
        } else {
            binding.cost.setText(getString(R.string.field_not_specified));
        }
    }

    private void setNotes() {
        if (StringUtils.isNotBlank(subcontractorDetails.getNotes())) {
            binding.notes.setText(subcontractorDetails.getNotes());
        } else {
            binding.notes.setText(getString(R.string.field_lack));
        }
    }

    private void setConnectedExpense() {
        if (subcontractorDetails.getExpenseId() > 0) {
            Expense expense = DAOUtil.getExpenseById(this, subcontractorDetails.getExpenseId());
            LinksUtil.makeLinkAlike(binding.connectedExpense, this, expense.getTitle());
        } else {
            binding.connectedExpenseLayout.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        setEmailOnClickListener();
        setPhoneOnClickListener();
        setConnectedExpenseOnClickListener();
        setFloatingButtonListener();
        setDeleteSubcontractorListener();
        setEditSubcontractorListener();
    }

    private void setEmailOnClickListener() {
        binding.email.setOnClickListener(v -> {
            if (StringUtils.isNotBlank(subcontractorDetails.getEmail())) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{subcontractorDetails.getEmail()});
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(emailIntent);
            }
        });
    }

    private void setPhoneOnClickListener() {
        binding.phone.setOnClickListener(v -> {
            if (StringUtils.isNotBlank(subcontractorDetails.getPhone())) {
                Uri number = Uri.parse("tel:" + subcontractorDetails.getPhone());
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, number);

                startActivity(phoneIntent);
            }
        });
    }

    private void setConnectedExpenseOnClickListener() {
        binding.connectedExpense.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExpenseActivity.class);
            intent.putExtra(EXPENSE_ID_EXTRA, subcontractorDetails.getExpenseId());
            startActivity(intent);
        });
    }

    private void setFloatingButtonListener() {
        binding.floatingButton.setOnClickListener(v -> {
            if (binding.deleteLayout.getVisibility() == View.GONE && binding.editLayout.getVisibility() == View.GONE) {
                showFloatingMenu();
            } else {
                hideFloatingMenu();
            }
        });
    }

    private void setDeleteSubcontractorListener() {
        binding.deleteLayout.setOnClickListener(v -> {
            new QuestionDialog(SubcontractorDetailsActivity.this, getString(R.string.subcontractor_details_delete_question)).showDialog();
            hideFloatingMenu();
        });
    }

    private void setEditSubcontractorListener() {
        binding.editLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewSubcontractorActivity.class);
            intent.putExtra(SUBCONTRACTOR_ID_EXTRA, subcontractorDetails.getId());
            intent.putExtra(ACTIVITY_TITLE_EXTRA, R.string.header_title_subcontractor_edit);
            startActivity(intent);

            hideFloatingMenu();
        });
    }

    private void showFloatingMenu() {
        binding.deleteLayout.setVisibility(View.VISIBLE);
        binding.editLayout.setVisibility(View.VISIBLE);
        binding.backgroundFade.setVisibility(View.VISIBLE);
        binding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_clear));
    }

    private void hideFloatingMenu() {
        binding.deleteLayout.setVisibility(View.GONE);
        binding.editLayout.setVisibility(View.GONE);
        binding.backgroundFade.setVisibility(View.GONE);
        binding.floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu));
    }

    @Override
    public void executeQuestionDialog() {
        DAOUtil.deleteSubcontractor(this, subcontractorDetails);
        deleteConnectedExpenseAndPayments();

        Intent intent = new Intent(this, SubcontractorsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void deleteConnectedExpenseAndPayments() {
        DAOUtil.deleteExpenseById(this, subcontractorDetails.getExpenseId());
        DAOUtil.deleteAllPaymentsByExpenseId(this, subcontractorDetails.getExpenseId());
    }
}

