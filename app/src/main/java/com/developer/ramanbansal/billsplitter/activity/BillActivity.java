package com.developer.ramanbansal.billsplitter.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.developer.ramanbansal.billsplitter.R;
import com.developer.ramanbansal.billsplitter.adapter.BillAdapter;
import com.developer.ramanbansal.billsplitter.adapter.Helper;

public class BillActivity extends AppCompatActivity {

    private String expenId;

    private ListView lvList;
    private BillAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        initViews();

        Cursor result = billAdapter.getBill(Integer.parseInt(expenId));
        String[] columns = new String[]{Helper.COLUMN_BILL_NAME, Helper.COLUMN_BILL_AMOUNT, Helper.COLUMN_BILL_SHARE, Helper.COLUMN_BILL_MEMBERS};
        int[] to = new int[]{R.id.tvBillName, R.id.tvBillAmount, R.id.tvBillShare, R.id.tvBillMembers};
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item_bill, result, columns, to, 0);
        lvList = (ListView) findViewById(R.id.lvBillList);
        lvList.setAdapter(dataAdapter);
    }

    private void initViews() {
        expenId = getIntent().getStringExtra("expenId");
        this.billAdapter = new BillAdapter(this);
        this.lvList = (ListView) findViewById(R.id.lvBillList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bBillAdd:
                Intent intent = new Intent(getApplicationContext(), AddBillActivity.class);
                intent.putExtra("expenId", expenId);
                startActivity(intent);
                return true;
            case R.id.bBillCalc:
                Intent intent1 = new Intent(getApplicationContext(), SplitActivity.class);
                intent1.putExtra("expenId", expenId);
                startActivity(intent1);
                return true;
            case R.id.bExit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        billAdapter.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}