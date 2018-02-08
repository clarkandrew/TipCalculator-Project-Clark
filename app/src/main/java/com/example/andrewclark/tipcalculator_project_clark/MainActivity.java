package com.example.andrewclark.tipcalculator_project_clark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnKeyListener;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    public double selectedTip = 0;
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        final TextView totalBill = findViewById(R.id.totalBill);
        final TextView totalSplit = findViewById(R.id.totalSplit);
        final TextView customTipText = findViewById(R.id.customTip);
        final TextView result = findViewById(R.id.resultText);
        savedInstanceState.putDouble(getString(R.string.selTip), selectedTip);
        savedInstanceState.putString(getString(R.string.totBill), totalBill.getText().toString());
        savedInstanceState.putString(getString(R.string.totSplit), totalSplit.getText().toString());
        savedInstanceState.putString(getString(R.string.custTipText), customTipText.getText().toString());
        savedInstanceState.putString(getString(R.string.res), result.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null) {
            selectedTip = 0;
        } else {

            final TextView totalBill = findViewById(R.id.totalBill);
            final TextView totalSplit = findViewById(R.id.totalSplit);
            final TextView customTipText = findViewById(R.id.customTip);
            final Button calcButton = findViewById(R.id.calcButton);
            final TextView result = findViewById(R.id.resultText);
            totalBill.setText(savedInstanceState.getString(getString(R.string.totBill)));
            totalSplit.setText(savedInstanceState.getString(getString(R.string.totSplit)));
            customTipText.setText(savedInstanceState.getString(getString(R.string.custTipText)));
            result.setText(savedInstanceState.getString(getString(R.string.res)));
            if (!savedInstanceState.getString(getString(R.string.res)).equals(getString(R.string.defaultRes))) {
                result.setVisibility(View.VISIBLE);
            }
            final RadioGroup radioTips = findViewById(R.id.tipGroup);
            selectedTip = savedInstanceState.getDouble(getString(R.string.selTip));
            if (selectedTip == 0) {
                customTipText.setVisibility(View.VISIBLE);
            } else {
                customTipText.setVisibility(View.GONE);

            }

            boolean ready = readyCheck(savedInstanceState.getString(getString(R.string.totBill)), savedInstanceState.getString(getString(R.string.totSplit)), savedInstanceState.getString(getString(R.string.totSplit)));

            if (ready == true) {
                calcButton.setEnabled(true);
            } else {
                calcButton.setEnabled(false);
            }

        }

    }

    public boolean readyCheck(String bill, String split, String custom) {
        if (bill.isEmpty() || split.isEmpty()) {
            return false;
        } else {
            if (Double.parseDouble(bill) >= 0 && Double.parseDouble(split) >= 0 && (custom.isEmpty() || custom == "" || Double.parseDouble(custom) >= 1)) {
                return true;
            } else {
                return false;
            }
        }


    }

    private void showErrorAlert(String errorMessage, final int fieldId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.err)
                .setMessage(errorMessage)
                .setNeutralButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findViewById(fieldId).requestFocus();
                            }
                        }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RadioGroup radioTips = findViewById(R.id.tipGroup);
        final TextView totalBill = findViewById(R.id.totalBill);
        final TextView totalSplit = findViewById(R.id.totalSplit);
        final TextView customTipText = findViewById(R.id.customTip);
        final Button calcButton = findViewById(R.id.calcButton);
        final Button resetButton = findViewById(R.id.resetButton);
        final TextView result = findViewById(R.id.resultText);



        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                totalBill.setText(null);
                totalSplit.setText(null);
                customTipText.setText(null);
                customTipText.setVisibility(View.GONE);
                radioTips.clearCheck();
            }
        });

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double bill = Double.parseDouble(totalBill.getText().toString());
                int split = Integer.parseInt(totalSplit.getText().toString());
                Double amount = (double) selectedTip;
                if (amount != 15.0 && amount != 20.0 && amount != 25.0) {

                    amount = Double.parseDouble(customTipText.getText().toString());
                }

                if (bill < 1 || split < 1 || amount < 1) {
                    if (bill < 1) {
                        showErrorAlert(getString(R.string.errBillAmount), totalBill.getId());
                    } else if (split < 1) {
                        showErrorAlert(getString(R.string.errSplit), totalSplit.getId());
                    } else {
                        showErrorAlert(getString(R.string.errTip), customTipText.getId());
                    }
                } else {

                    double billPerPerson = bill / split;
                    double tipPerPerson = billPerPerson * (amount / 100);
                    tipPerPerson = Math.round(tipPerPerson * 100);
                    tipPerPerson = tipPerPerson / 100;
                    result.setText(getString(R.string.usd) + String.valueOf(tipPerPerson) + " " + getString(R.string.defResConst));
                    result.setVisibility(View.VISIBLE);
                }
            }

        });



        totalBill.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean ready = readyCheck(totalBill.getText().toString(), totalSplit.getText().toString(), customTipText.getText().toString());

                if (ready == true) {
                    calcButton.setEnabled(true);
                } else {
                    calcButton.setEnabled(false);
                }
                return false;
            }
        });

        totalSplit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean ready = readyCheck(totalBill.getText().toString(), totalSplit.getText().toString(), customTipText.getText().toString());
                if (ready == true) {
                    calcButton.setEnabled(true);
                } else {
                    calcButton.setEnabled(false);
                }
                return false;
            }
        });

        customTipText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean ready = readyCheck(totalBill.getText().toString(), totalSplit.getText().toString(), customTipText.getText().toString());
                if (ready == true) {
                    calcButton.setEnabled(true);
                } else {
                    calcButton.setEnabled(false);
                }

                return false;
            }
        });

        radioTips.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int radioID) {
                switch (radioID) {
                    case R.id.tip15:
                        selectedTip = 15;
                        customTipText.setVisibility(View.GONE);
                        break;
                    case R.id.tip20:
                        selectedTip = 20;
                        customTipText.setVisibility(View.GONE);
                        break;
                    case R.id.tip25:
                        selectedTip = 25;
                        customTipText.setVisibility(View.GONE);
                        break;
                    case R.id.tipCustom:
                        selectedTip = 0;
                        customTipText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }



}