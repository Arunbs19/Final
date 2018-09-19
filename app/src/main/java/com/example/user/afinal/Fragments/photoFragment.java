package com.example.user.afinal.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class photoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View view;
    TextView getsign;
    signature mSignature;
    String signaturepath;
    Button btn_multichoice;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/signatures/";
    LinearLayout dynamic_linear;
    TextView bartext;

    public photoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        init(view);
        initcallback();
        return view;

    }

    private void init(View view) {
       getsign =(TextView)view.findViewById(R.id.txtsignature);
        btn_multichoice = (Button)view.findViewById(R.id.btn_multiplechoice);
        dynamic_linear = (LinearLayout)view.findViewById(R.id.dynamic_linear);
        Button button = new Button(getActivity());
        button.setText("Barcode Scanner");
        dynamic_linear.addView(button);
        bartext = new TextView(getActivity());
        bartext.setGravity(Gravity.CENTER);
        dynamic_linear.addView(bartext);
        MyApplication.getInstance().setBartext(bartext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,QR_CODE");
                startActivityForResult(intent, 0);    //Barcode Scanner to scan for us

                }catch (Exception ex){
                    showDialog(getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();

                }

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            String result = data.getStringExtra("SCAN_RESULT");
            bartext.setText(result);
            Toast.makeText(getActivity(), result ,Toast.LENGTH_SHORT).show();

        }catch (Exception ex){

        }
    }

    private void initcallback() {
        try {
            btn_multichoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    {AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                        String[] colors = new String[]{
                                "Red",
                                "Green",
                                "Blue",
                                "Purple",
                                "Olive"
                        };

                        // Boolean array for initial selected items
                        final boolean[] checkedColors = new boolean[]{
                                false, // Red
                                false, // Green
                                false, // Blue
                                false, // Purple
                                false // Olive

                        };
                        builderSingle.setTitle("Select Machine Type");
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_multichoice);
                        final List<String> colorsList = Arrays.asList(colors);
                        arrayAdapter.add("LOT No.");
                        arrayAdapter.add("ALC No.");

                        builderSingle.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                // Update the current focused item's checked status
                                checkedColors[which] = isChecked;

                                // Get the current focused item
                                String currentItem = colorsList.get(which);

                                // Notify the current action
                                Toast.makeText(getActivity(), currentItem + " " , Toast.LENGTH_SHORT).show();
                            }
                        });

                        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                        AlertDialog alert = builderSingle.create();
                        alert.show();
                        alert.setCanceledOnTouchOutside(false);

                    }

                }
            });


            getsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    LayoutInflater layinf = (LayoutInflater)getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    final View signatureDialog = layinf.inflate(R.layout.signature, null);
                    builder.setView(signatureDialog);


                    mSignature = new signature(getContext());
                    Button btncancel = (Button) signatureDialog.findViewById(R.id.cancel);
                    Button btnclear = (Button) signatureDialog.findViewById(R.id.clear);
                    Button btngetsign = (Button) signatureDialog.findViewById(R.id.getsign);
                    LinearLayout mContent = (LinearLayout) signatureDialog.findViewById(R.id.canvasLayout);
                    mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    view = mContent;

                    final android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    btncancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Log.v("log_tag", "Panel Canceled");
                            alertDialog.dismiss();


                        }
                    });
                    btnclear.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Log.v("log_tag", "Panel Cleared");
                            mSignature.clearSignature();
                            //signaturepresent = false;

                        }
                    });
                    btngetsign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                            save(mSignature.getSignature());

                        }
                    });

                    alertDialog.show();

                }
            });

        }catch (Exception ex){

        }
    }
    final void save(Bitmap signature) {

        // the directory where the signature will be saved
        File myDir = new File(DIRECTORY);

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String signature_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());


        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, signature_name+".png");
        if (file.exists()) {
            file.delete();
        }

        try {

            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            signaturepath = file.getAbsolutePath();
            //Toast.makeText(Newrunsheet.this, "Signature saved.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class signature extends View {

        // set the stroke width
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context) {

            super(context);

            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);

            // set the bg color as white
            this.setBackgroundColor(Color.WHITE);

            // width and height should cover the screen
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        }

        /**
         * Get signature
         *
         * @return
         */
        protected Bitmap getSignature() {

            Bitmap signatureBitmap = null;

            // set the signature bitmap
            if (signatureBitmap == null) {
                signatureBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
            }

            // important for saving signature
            final Canvas canvas = new Canvas(signatureBitmap);
            this.draw(canvas);

            return signatureBitmap;
        }

        /**
         * clear signature canvas
         */
        private void clearSignature() {
            path.reset();
            this.invalidate();
        }

        // all touch events during the drawing
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(this.path, this.paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
           // signaturepresent = true;

            switch (event.getAction()) {


                case MotionEvent.ACTION_DOWN:

                    path.moveTo(eventX, eventY);

                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);

                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:

                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

    }
    private Dialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence Yes, CharSequence No) {
        // TODO Auto-generated method stub
        AlertDialog.Builder download = new AlertDialog.Builder(act);
        download.setTitle(title);
        download.setMessage(message);
        download.setPositiveButton(Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent in = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(in);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        download.setNegativeButton(No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub

            }
        });
        return download.show();
    }


}
