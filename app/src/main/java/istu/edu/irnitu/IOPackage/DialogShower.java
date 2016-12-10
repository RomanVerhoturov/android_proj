package istu.edu.irnitu.IOPackage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import istu.edu.irnitu.R;

/**
 * NewFitGid
 * Created by Александр on 19.10.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */
public class DialogShower {
    private static MaterialDialog.Builder builder;
    private static MaterialDialog dialog;

    public static void showMessageDialog(Context context, int title, int message) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showMessageDialog(Context context, int title, String message) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showOkNODialog(Context context, int title, int message, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .negativeText(R.string.dialog_cancel)
                .cancelable(false)
                .onPositive(singleButtonCallback)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showOkNODialog(Context context, int title, String message, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .negativeText(R.string.dialog_cancel)
                .cancelable(false)
                .onPositive(singleButtonCallback)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showMessageDialog(Context context, String title, String message) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showMessageDialog(Context context, int title, String message, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .cancelable(false)
                .onPositive(singleButtonCallback);
        dialog = builder.build();
        dialog.show();
    }

    public static void showMessageDialog(Context context, int title, int message, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .cancelable(false)
                .onPositive(singleButtonCallback);
        dialog = builder.build();
        dialog.show();
    }

    public static void showMessageDialog(Context context, String title, String message, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(R.string.positive_ok)
                .cancelable(false)
                .onPositive(singleButtonCallback);
        dialog = builder.build();
        dialog.show();
    }

    public static void showProgressDialog(Context context, boolean flag, int title, int message) {
        if (flag) {
            builder = new MaterialDialog.Builder(context)
                    .title(title)
                    .content(message)
                    .progress(true, 0)
                    .cancelable(false);
            dialog = builder.build();
            dialog.show();
        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public static void showNumberDialog(View view, ArrayList<String> list) {
        builder = new MaterialDialog.Builder(view.getContext())
                .title(R.string.chooseNumber)
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        dialog.dismiss();
                        Uri phone = Uri.parse("tel:" + text.toString());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, phone);
                        view.getContext().startActivity(callIntent);
                    }
                })
                .negativeText(R.string.dialog_cancel)
                .negativeColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent))
                .cancelable(true)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showURLDialog(View view, ArrayList<String> list) {
        builder = new MaterialDialog.Builder(view.getContext())
                .title(R.string.chooseUrl)
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        dialog.dismiss();
                        Uri address = Uri.parse(text.toString());
                        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                        view.getContext().startActivity(openlinkIntent);

                    }
                })
                .negativeText(R.string.dialog_cancel)
                .negativeColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent))
                .cancelable(true)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }

    public static void showChooseDialog(View v, int header, int items, final ArrayList<String> urls, final ArrayList<String> phones) {
        builder = new MaterialDialog.Builder(v.getContext())
                .title(header)
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            showURLDialog(view, urls);
                        } else if (which == 1) {
                            showNumberDialog(view, phones);
                        }
                        dialog.dismiss();

                    }
                })
                .negativeText(R.string.dialog_cancel)
                .cancelable(true)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.build();
        dialog.show();
    }
}
