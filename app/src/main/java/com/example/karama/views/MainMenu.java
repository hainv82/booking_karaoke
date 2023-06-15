package com.example.karama.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karama.BillActivity;
import com.example.karama.BookingRoom;
import com.example.karama.MainActivity;
import com.example.karama.MemberActivity;
import com.example.karama.R;
import com.example.karama.adapter.ItemAdapter;
import com.example.karama.adapter.StaffAdapter;
import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UHelper;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.Res3ListStaff;
import com.example.karama.model.person.Res4AddStaff;
import com.example.karama.model.ResNullData;
import com.example.karama.model.person.ResProfile;
import com.example.karama.model.person.Staff;
import com.example.karama.model.product.Products;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.services.KaraServices;
import com.example.karama.services.ProdServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    public Context mContext =this;
    public static Dialog loadingDialog;
    Button nav,btn_update,btn_logout,nav_product;
    View view;
    CardView card_filter,card_info,card_update,card_menuitem;
    TextView text_info,text_update_info,txt_username,txt_first_name,txt_last_name,txt_gender,txt_email, txt_sdt,txt_adress1,txt_adress2,tv_role;
    TextView update_username, update_email, update_sdt, update_gender,update_adres1,update_adres2,text_qlnv,tv_adduser,reload_rcv,add_product;
    ProgressDialog dialogUpdate,dialogGetProfile,loadingGetStaff,loadingChangepass,loadingAddUser,loadingKItem;
    ImageView img_ketoan,img_manager, img_employee;
    RecyclerView rcv_staff,rcv_menu_items;
    StaffAdapter staffAdapter;
    ItemAdapter itemAdapter;
    List<Staff> staffList;
    List<Products> productsList;
    TextView nav_customer,nav_room,nav_service;
    private String m_newpass = "";
    private static MainMenu instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();
        initView();
        setClick();
        getProfile();
    }

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    private void getProfile() {
        dialogGetProfile.show();
        KaraServices.seeProfile(mContext, new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                dialogGetProfile.cancel();
                ResProfile resProfile = (ResProfile) response.body();
                if (resProfile != null) {
                    if (resProfile.getStatus().equals("200")) {
                        //successs
                        txt_username.setText(UHelper.getNullorEmptyV2(resProfile.getData().getUsername()));
                        txt_first_name.setText(UHelper.getNullorEmptyV2(resProfile.getData().getFirstName()));
                        txt_last_name.setText(UHelper.getNullorEmptyV2(resProfile.getData().getLastName()));
                        txt_gender.setText(UHelper.getNullorEmptyV2(resProfile.getData().getGender()));
                        txt_email.setText(UHelper.getNullorEmptyV2(resProfile.getData().getEmail()));
                        txt_sdt.setText(UHelper.getNullorEmptyV2(resProfile.getData().getPhoneNumber()));
                        txt_adress1.setText(UHelper.getNullorEmptyV2(resProfile.getData().getAddress1()));
                        txt_adress2.setText(UHelper.getNullorEmptyV2(resProfile.getData().getAddress2()));
                        tv_role.setText(UHelper.getNullorEmptyV2(resProfile.getData().getRoleCodeName()));

                        update_email.setText(UHelper.getNullorEmptyV2(resProfile.getData().getEmail()));
                        update_sdt.setText(UHelper.getNullorEmptyV2(resProfile.getData().getPhoneNumber()));
                        update_gender.setText(UHelper.getNullorEmptyV2(resProfile.getData().getGender()));
                        update_adres1.setText(UHelper.getNullorEmptyV2(resProfile.getData().getAddress1()));
                        update_adres2.setText(UHelper.getNullorEmptyV2(resProfile.getData().getAddress2()));
                        tv_role.setText(UHelper.getNullorEmptyV2(resProfile.getData().getRoleCodeName()));
                        if (UHelper.getNullorEmptyV2(resProfile.getData().getRoleCodeName()).equals("MANAGER")) {
                            img_manager.setVisibility(View.VISIBLE);
                            img_ketoan.setVisibility(View.GONE);
                            img_employee.setVisibility(View.GONE);
                            text_qlnv.setVisibility(View.VISIBLE);
                        }
                        if (UHelper.getNullorEmptyV2(resProfile.getData().getRoleCodeName()).equals("STAFF")) {
                            img_manager.setVisibility(View.GONE);
                            img_ketoan.setVisibility(View.GONE);
                            img_employee.setVisibility(View.VISIBLE);
                            text_qlnv.setVisibility(View.GONE);

                        }
                        if (UHelper.getNullorEmptyV2(resProfile.getData().getRoleCodeName()).equals("ACCOUNTANT")) {
                            img_manager.setVisibility(View.GONE);
                            img_ketoan.setVisibility(View.VISIBLE);
                            img_employee.setVisibility(View.GONE);
                            text_qlnv.setVisibility(View.GONE);
                        }


                    } else if (resProfile.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resProfile.getStatus(), resProfile.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
//                                Intent i = new Intent(MainMenu.this, MainActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
                                logout();
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resProfile.getStatus(),resProfile.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                dialogGetProfile.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.amazing_64);

            }
        },SharedPrefManager.getUsername());

    }

    private void setClick() {
        nav.setOnClickListener(this);
        nav_product.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        view.setOnClickListener(this);
        card_info.setOnClickListener(this);
        card_update.setOnClickListener(this);
        text_info.setOnClickListener(this);
        text_update_info.setOnClickListener(this);
        text_qlnv.setOnClickListener(this);
        tv_adduser.setOnClickListener(this);
        nav_customer.setOnClickListener(this);
        nav_room.setOnClickListener(this);
        nav_service.setOnClickListener(this);
        add_product.setOnClickListener(this);
        reload_rcv.setOnClickListener(this);

    }

    private void initView() {
        instance = this;
        loadingDialog = UIHelper.setShowProgressBar(mContext);
        dialogGetProfile = new ProgressDialog(mContext);
        dialogGetProfile.setCancelable(false);
        dialogUpdate = new ProgressDialog(mContext);
        dialogUpdate.setMessage("Đang update thông tin");
        loadingChangepass = new ProgressDialog(mContext);
        loadingChangepass.setMessage("Đang cấp lại mật khẩu mới \n Vui lòng waitting ...");
        loadingChangepass.setProgressDrawable(mContext.getDrawable(R.drawable.loading_64));
        loadingAddUser = new ProgressDialog(mContext);
        loadingAddUser.setMessage("Đang thêm User \n Vui lòng đợi");
        loadingKItem = new ProgressDialog(mContext);
        loadingKItem.setMessage("Loading karaoke order items...");
        loadingChangepass.setProgressDrawable(mContext.getDrawable(R.drawable.import_36));
        loadingGetStaff = new ProgressDialog(mContext);
        loadingGetStaff.setMessage("Getting List Staffs...!");
        nav = findViewById(R.id.nav);
        nav_product = findViewById(R.id.nav_product);
        nav_service = findViewById(R.id.nav_service);
        btn_update = findViewById(R.id.btn_update);
        btn_logout = findViewById(R.id.btn_logout);
        view = findViewById(R.id.view);
        card_filter = findViewById(R.id.card_filter);
        card_filter.setVisibility(View.GONE);
        card_menuitem = findViewById(R.id.card_menuitem);
        card_menuitem.setVisibility(View.GONE);
        card_menuitem.animate().translationY(0);
        view.setVisibility(View.GONE);
        card_info = findViewById(R.id.card_info);
        card_info.setVisibility(View.GONE);
        card_update = findViewById(R.id.card_update);
        card_update.setVisibility(View.GONE);

        txt_username = findViewById(R.id.txt_username);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_gender = findViewById(R.id.txt_gender);
        txt_email = findViewById(R.id.txt_email);
        txt_sdt = findViewById(R.id.txt_sdt);
        txt_adress1 = findViewById(R.id.txt_adress1);
        txt_adress2 = findViewById(R.id.txt_adress2);
        tv_role = findViewById(R.id.tv_role);
        text_info = findViewById(R.id.text_info);
        text_qlnv = findViewById(R.id.text_qlnv);
        text_update_info = findViewById(R.id.text_update_info);
        update_email = findViewById(R.id.update_email);
        update_gender = findViewById(R.id.update_gender);
        update_sdt = findViewById(R.id.update_sdt);
        update_adres1 = findViewById(R.id.update_adres1);
        update_adres2 = findViewById(R.id.update_adres2);
        img_ketoan = findViewById(R.id.img_ketoan);
        img_manager = findViewById(R.id.img_manager);
        img_employee = findViewById(R.id.img_employee);
        tv_adduser = findViewById(R.id.tv_adduser);
        tv_adduser.setVisibility(View.GONE);

        rcv_staff = findViewById(R.id.rcv_staff);
        rcv_menu_items = findViewById(R.id.rcv_menu_items);
        nav_customer = findViewById(R.id.nav_customer);
        nav_room = findViewById(R.id.nav_room);
        reload_rcv = findViewById(R.id.reload_rcv);
        add_product = findViewById(R.id.add_product);
        rcv_staff.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rcv_menu_items.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        staffList = new ArrayList<>();
        productsList = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav:
                view.setVisibility(View.VISIBLE);
                card_filter.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_product:
                view.setVisibility(View.VISIBLE);
                card_menuitem.setVisibility(View.VISIBLE);
                loadListKItem();
                break;
            case R.id.view:
                card_filter.setVisibility(View.GONE);
                card_menuitem.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                rcv_staff.setVisibility(View.GONE);
                break;
            case R.id.text_info:
                if (card_info.isShown()) {
                    card_info.setVisibility(View.GONE);
                } else {
                    card_info.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.text_update_info:
                if (card_update.isShown()) {
                    card_update.setVisibility(View.GONE);
                } else {
                    card_update.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_update:
                updateInfo();
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.card_filter:
                Log.e("==lick:", "card_filter");
                if (view.isShown()) {
                    card_filter.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.card_menuitem:
                if (view.isShown()) {
                    card_menuitem.setVisibility(View.VISIBLE);
                }
            case R.id.text_qlnv:
                if (rcv_staff.isShown()) {
                    rcv_staff.setVisibility(View.GONE);
                    tv_adduser.setVisibility(View.GONE);
                } else {
                    rcv_staff.setVisibility(View.VISIBLE);
                    tv_adduser.setVisibility(View.VISIBLE);
                    getStaff();
                }
                break;
            case R.id.tv_adduser:
                DialogAddStaff dlAddStaff = new DialogAddStaff(MainMenu.this);
                dlAddStaff.setCanceledOnTouchOutside(false);
                dlAddStaff.show();
                break;
            case R.id.nav_customer:
                Intent intent = new Intent(MainMenu.this, MemberActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_room:
                Intent intent1 = new Intent(MainMenu.this, BookingRoom.class);
                startActivity(intent1);
                break;
            case R.id.reload_rcv:
                loadListKItem();
                break;
            case R.id.add_product:
                Log.e("==add:","items");
                DialogAddItem dlAddItem = new DialogAddItem(MainMenu.this,"ADD");
                dlAddItem.setCanceledOnTouchOutside(false);
                dlAddItem.show();
                break;
            case R.id.nav_service:
                Intent intent2 = new Intent(MainMenu.this, BillActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public void loadListKItem() {
        loadingKItem.show();
        ProdServices.getAllProduct(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingKItem.cancel();
                ResAllProducts resAllProducts = (ResAllProducts) response.body();
                if (resAllProducts.getStatus().equals("200")) {
                    productsList.clear();
                    for (int i = 0; i < resAllProducts.getData().getData().size(); i++) {
                        Products item = resAllProducts.getData().getData().get(i);
                        productsList.add(item);
                    }
                    itemAdapter = new ItemAdapter(mContext, productsList);
                    rcv_menu_items.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                } else if (resAllProducts.getStatus().equals("403")){
                    UIHelper.showAlertDialogV3(mContext, resAllProducts.getStatus(), resAllProducts.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(MainMenu.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });
                } else{
                    UIHelper.showAlertDialog(mContext,resAllProducts.getStatus(),resAllProducts.getMessage(),R.drawable.troll_64);
                }

            }

            @Override
            public void Error(String error) {
                loadingKItem.cancel();

            }
        });


    }

    private void getStaff() {
        loadingGetStaff.show();
        KaraServices.getAllStaff(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingGetStaff.cancel();
                Res3ListStaff resGetStaff = (Res3ListStaff) response.body();
                if (resGetStaff != null) {
                    if (resGetStaff.getStatus().equals("200")) {
                        staffList.clear();
                        //xuli
                        for (int i = 0; i <resGetStaff.getData().getData().size() ; i++) {
                            Staff staff=resGetStaff.getData().getData().get(i);
                            staffList.add(staff);
                            staffAdapter = new StaffAdapter(mContext, staffList);
                            rcv_staff.setAdapter(staffAdapter);
                            staffAdapter.notifyDataSetChanged();
                        }

                    } else if (resGetStaff.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resGetStaff.getStatus(), resGetStaff.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MainMenu.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resGetStaff.getStatus(),resGetStaff.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },"0","20","ASC");
    }

    private void logout() {
        SharedPrefManager.setAccessToken("NON");
        SharedPrefManager.setRefreshToken("NON");
        Intent i = new Intent(MainMenu.this, MainActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void updateInfo() {
        String mail = update_email.getText().toString();
        String sdt = update_sdt.getText().toString();
        String gend = update_gender.getText().toString();
        String adres1 = update_adres1.getText().toString();
        String adres2 = update_adres2.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", mail);
            jsonObject.put("phoneNumber", sdt);
            jsonObject.put("gender", gend);
            jsonObject.put("address1", adres1);
            jsonObject.put("address2", adres2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String req = jsonObject.toString();
        dialogUpdate.show();
        KaraServices.updateUser(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                dialogUpdate.cancel();
                Res4AddStaff resUpdate = (Res4AddStaff) response.body();
                if (resUpdate != null) {
                    if (resUpdate.getStatus().equals("200")) {
                        UIHelper.showAlertDialogConfirm(mContext, "SUCCESS", resUpdate.getMessage(), R.drawable.ic_success_35, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                getProfile();
                            }
                        });
                    } else if (resUpdate.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resUpdate.getStatus(), resUpdate.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MainMenu.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resUpdate.getStatus(),resUpdate.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                dialogUpdate.cancel();
                UIHelper.showAlertDialog(mContext,"FAILURE",error,R.drawable.amazing_64);
            }
        },req);

    }

    public void dialogStaff(Staff staff) {
        DialogStaff dialogStaff = new DialogStaff(MainMenu.this, staff);
        dialogStaff.setCanceledOnTouchOutside(false);
        dialogStaff.show();
    }
    public void dialogNewpass(String userChange){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New password for "+userChange);

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_newpass = input.getText().toString();
                Log.e("==newpass", m_newpass);
                changePass(userChange, m_newpass);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void changePass(String userChange, String newpass) {
        loadingChangepass.show();
        KaraServices.renewPass(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingChangepass.cancel();
                ResNullData resRenew = (ResNullData) response.body();
                if (resRenew != null) {
                    if (resRenew.getStatus().equals("200")) {
                        UIHelper.showAlertDialog(mContext,"SUCCESS",resRenew.getMessage(),R.drawable.ic_success_35);
                    } else if (resRenew.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resRenew.getStatus(), resRenew.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MainMenu.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resRenew.getStatus(),resRenew.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingChangepass.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.amazing_64);

            }
        },userChange,newpass);
    }

    public void addUser(String stringAddUser) {
        loadingAddUser.show();
        KaraServices.addUser(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingAddUser.cancel();
                Res4AddStaff res4AddStaff = (Res4AddStaff) response.body();
                if (res4AddStaff != null) {
                    if (res4AddStaff.getStatus().equals("200")) {
                        UIHelper.showAlertDialogV3(mContext, "200", res4AddStaff.getMessage(), R.drawable.ic_success_35, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                getStaff();

                            }
                        });
                    } else if (res4AddStaff.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, res4AddStaff.getStatus(), res4AddStaff.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MainMenu.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,res4AddStaff.getStatus(),res4AddStaff.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingAddUser.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.troll_64);

            }
        },stringAddUser);


    }

    public void lockItem(String productId) {
        ProdServices.lockProducrt(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resLock = (ResNullData) response.body();
                if (resLock != null) {
                    if (resLock.getStatus().equals("200")) {
                        Toast.makeText(mContext, resLock.getStatus()+"-"+resLock.getMessage(), Toast.LENGTH_SHORT).show();
                        loadListKItem();
                    } else if (resLock.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resLock.getStatus(), resLock.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MainMenu.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resLock.getStatus(),resLock.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },productId);
    }
    public void unLockItem(String productId) {
        ProdServices.unlockProduct(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resLock = (ResNullData) response.body();
                if (resLock != null) {
                    if (resLock.getStatus().equals("200")) {
                        Toast.makeText(mContext, resLock.getStatus()+"-"+resLock.getMessage(), Toast.LENGTH_SHORT).show();
                        loadListKItem();
                    } else if (resLock.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resLock.getStatus(), resLock.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MainMenu.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resLock.getStatus(),resLock.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },productId);
    }

    public void dialogUpdateItem(Products item){
        DialogAddItem dialogUpdateItem = new DialogAddItem(MainMenu.this, "UPDATE", item);
        dialogUpdateItem.setCanceledOnTouchOutside(false);
        dialogUpdateItem.show();
    }

}