package com.developer.ramanbansal.billsplitter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.ramanbansal.billsplitter.R;
import com.developer.ramanbansal.billsplitter.adapter.Helper;
import com.developer.ramanbansal.billsplitter.adapter.ExpenAdapter;
import com.developer.ramanbansal.billsplitter.model.Expen;

public class ExpenActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private boolean doubleBackToExitPressedOnce;
    private ListView lvList;
    private TextView tvEmpty;
    private ExpenAdapter expenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expen);

        initViews();

        Cursor result = expenAdapter.getExpen();
        if (result.getCount() != 0) {
            tvEmpty.setVisibility(TextView.GONE);
            String[] columns = new String[]{Helper.COLUMN_EXPEN_NAME};
            int[] to = new int[]{R.id.tvItemExpenName};
            SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item_expen, result, columns, to, 0);
            lvList.setAdapter(dataAdapter);
        } else {
            tvEmpty.setVisibility(TextView.VISIBLE);
            tvEmpty.setText("Click on '+' to add new expen");
        }
    }

    private void initViews() {
        this.doubleBackToExitPressedOnce = false;
        this.expenAdapter = new ExpenAdapter(this);
        this.tvEmpty = (TextView) findViewById(R.id.tvExpenEmpty);
        this.lvList = (ListView) findViewById(R.id.lvExpenList);
        this.lvList.setOnItemClickListener(this);
        this.lvList.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) lvList.getItemAtPosition(position);
        String curId = cursor.getString(cursor.getColumnIndexOrThrow(Helper.COLUMN_EXPEN_ID));
        Intent intent = new Intent(getBaseContext(), MemberActivity.class);
        intent.putExtra("expenId", curId);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAdd:
                addExpen();
                return true;
            case R.id.bExit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addExpen() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExpenActivity.this);
        alertDialog.setTitle("Add Bill");
        alertDialog.setMessage("Provide a name for new bill?");
        final EditText input = new EditText(ExpenActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("")) {
                            Toast.makeText(ExpenActivity.this, "Enter bill name!", Toast.LENGTH_SHORT).show();
                        } else {
                            Expen createdExpen = expenAdapter.createExpen(input.getText().toString());
                            Toast.makeText(ExpenActivity.this, createdExpen.getName() + " bill created!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), ExpenActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        expenAdapter.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}