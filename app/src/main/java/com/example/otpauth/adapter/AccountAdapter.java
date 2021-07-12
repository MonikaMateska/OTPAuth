package com.example.otpauth.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otpauth.R;
import com.example.otpauth.model.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>{

    private List<Account> accountList;

    public AccountAdapter(List<Account> accountList) {
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null, false);
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

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView issuer, code;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            issuer = (TextView) itemView.findViewById(R.id.issuer);
            code = (TextView) itemView.findViewById(R.id.secretCode);
        }

        public void bindData(Account data){
            issuer.setText(data.getUsername() + "@" + data.getIssuer());
            code.setText("!@# #@!");
        }
    }

}
