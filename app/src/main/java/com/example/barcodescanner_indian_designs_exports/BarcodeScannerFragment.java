package com.example.barcodescanner_indian_designs_exports;

import android.animation.Animator;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.barcodescanner_indian_designs_exports.databinding.BarcodeScannerFragmentBinding;
import com.example.barcodescanner_indian_designs_exports.room_db.MyAppRoomDataBase;
import com.example.barcodescanner_indian_designs_exports.room_db.Qr_Code_details;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class BarcodeScannerFragment extends Fragment {
    BarcodeScannerFragmentBinding binding;
    private CodeScanner scanner;
    String qr_code_text = "";
    private LottieAnimationView lottieAnimationView;
    public static MyAppRoomDataBase myAppRoomDataBase;
    public Qr_Code_details qr_code_details = new Qr_Code_details();
    private int count = 0;
    List<Qr_Code_details> qr_code_data;

    public BarcodeScannerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BarcodeScannerFragmentBinding.inflate(inflater, container, false);
        codeScanner();
        lottieAnimationView = binding.includedLayout.animationView;
        myAppRoomDataBase = Room.databaseBuilder(getContext(), MyAppRoomDataBase.class, "qr_code_db").allowMainThreadQueries().build();

        return binding.getRoot();
    }


    public void codeScanner() {
        scanner = new CodeScanner(getContext(), binding.scannerView);
        scanner.setCamera(CodeScanner.CAMERA_BACK);
        scanner.setFormats(CodeScanner.ALL_FORMATS);
        scanner.setScanMode(ScanMode.SINGLE);
        scanner.setAutoFocusMode(AutoFocusMode.SAFE);

        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(() -> {
                    if (result.getText().length() > 10) {
                        qr_code_text = result.getText();
                        show_animation("https://assets8.lottiefiles.com/packages/lf20_wsawbmq2.json");
                        qr_code_details.setId(++count);
                        qr_code_details.setQr_code_decoded_txt(qr_code_text);
                        myAppRoomDataBase.myDAO().addQr_codes_decodes(qr_code_details);
                    } else {
                        show_animation("https://assets10.lottiefiles.com/packages/lf20_rlvyyuci.json");
                    }
                });
            }
        });

        scanner.setErrorCallback(error -> {
            binding.scanSomethingTxtview.setText(error.getMessage());
            hide_animation();
        });

        binding.scanQrBtn.setOnClickListener(view -> {
            binding.scanSomethingTxtview.setText("");
            lottieAnimationView.clearAnimation();
            scanner.startPreview();
            binding.scanSomethingTxtview.setHint("Scanning...");
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // scanner.startPreview();
    }

    @Override
    public void onPause() {
        scanner.releaseResources();
        super.onPause();
    }

    public void hide_animation() {
        binding.includedLayout.loaderLayout.setVisibility(View.INVISIBLE);
    }

    public void show_animation(String lottie_animation_json_url) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimationFromUrl(lottie_animation_json_url);
        lottieAnimationView.playAnimation();
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                binding.scanSomethingTxtview.setText(qr_code_text);
                lottieAnimationView.setVisibility(View.INVISIBLE);

                qr_code_data = myAppRoomDataBase.myDAO().get_all_Qr_code_details();
                String Concatenate = "";
                if (qr_code_data != null) {
                    for (int i = 0; i < qr_code_data.size(); i++) {
                        Concatenate += (i + 1) + ") " + qr_code_data.get(i).getQr_code_decoded_txt();
                        if (i < qr_code_data.size() - 1) Concatenate += "," + "\n";
                    }
                    binding.scanDataRecyclerView.setText(Concatenate);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

    }
}