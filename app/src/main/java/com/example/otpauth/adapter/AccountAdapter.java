package com.example.otpauth.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otpauth.R;
import com.example.otpauth.model.Account;
import com.example.otpauth.model.Accounts;
import com.google.gson.Gson;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.example.otpauth.activity.mainActivity.MainActivityKt.STORED_ACCOUNTS;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<Account> accountList;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private Gson gson;

    public AccountAdapter(List<Account> accountList,
                          SharedPreferences sharedPreferences) {
        this.accountList = accountList;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null, false);
        sharedPreferencesEditor = sharedPreferences.edit();
        gson = new Gson();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
        holder.bindData(accountList.get(position));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView issuer, username, code, timer;
        private ProgressBar timerBar;
        private View view;
        private ImageView imgViewRemoveItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            issuer = (TextView) itemView.findViewById(R.id.issuer);
            code = (TextView) itemView.findViewById(R.id.secretCode);
            username = (TextView) itemView.findViewById(R.id.username);
            imgViewRemoveItem = (ImageView) itemView.findViewById(R.id.imgViewRemoveItem);
            timer = (TextView) itemView.findViewById(R.id.textView_countdown);
            timerBar = (ProgressBar) itemView.findViewById(R.id.progress_countdown);
            timerBar.setMax(15);
            timerBar.setProgress(15);
            imgViewRemoveItem.setOnClickListener(this);
        }

        public void bindData(Account data){
            issuer.setText(data.getIssuer());
            username.setText(data.getUsername());
            code.setText(data.getOtp());
            timer.setText(String.valueOf(data.getTimer()));
            timerBar.setProgress(data.getTimer());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Account deleted!", Toast.LENGTH_SHORT).show();
            removeAt(getAdapterPosition());
        }
    }

    public void removeAt(int position) {
        Accounts storedAccounts = readStoredAccounts();
        storedAccounts.accounts.remove(position);
        String storedAccountsString = gson.toJson(storedAccounts);
        sharedPreferencesEditor.putString(STORED_ACCOUNTS, storedAccountsString);
        sharedPreferencesEditor.apply();

        accountList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, accountList.size());
    }

    private Accounts readStoredAccounts() {
        String storedAccountsString = sharedPreferences.getString(STORED_ACCOUNTS, "{ accounts: [] }");
        Accounts storedAccounts = gson.fromJson(storedAccountsString, Accounts.class);
        return storedAccounts;
    }

}
