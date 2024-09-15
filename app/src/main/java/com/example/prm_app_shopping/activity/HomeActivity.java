package com.example.prm_app_shopping.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.adapters.CategoryAdapter;
import com.example.prm_app_shopping.adapters.ProductAdapter;
import com.example.prm_app_shopping.adapters.SearchAdapter;
import com.example.prm_app_shopping.api.ProductApiService;
import com.example.prm_app_shopping.databinding.ActivityHomeBinding;
import com.example.prm_app_shopping.model.Category;
import com.example.prm_app_shopping.model.Product;
import com.example.prm_app_shopping.model.Users;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;
    ProductAdapter productAdapter;
    NavigationView navigationView;
    ImageView card, history, menu;
    AutoCompleteTextView atcProductSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuLogout: {
                    SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("USERS");
                    editor.apply();
                    startActivity(new Intent(HomeActivity.this, Login.class));
                    return true;
                }
                case R.id.menuHome: {
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                    return true;
                }
            }
            return true;
        });

        initCategories();
        initProducts();
        initSlider();
        initDrawerLayout();
        setProductSearchAdapter();

        card = (ImageView) findViewById(R.id.iconCard);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        history = (ImageView) findViewById(R.id.iconHistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, History.class);
                startActivity(intent);
            }
        });

    }

    private void initDrawerLayout() {
        menu = (ImageView) findViewById(R.id.iconMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getRoot().openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuLogout);
        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
        String cachedUsers = sharedPreferences.getString("USERS", null);
        Users customer = new Gson().fromJson(cachedUsers, Users.class);
        if (customer == null){
            item.setTitle("Login");
        }else{item.setTitle("Logout");}
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {

            SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
            String cachedUsers = sharedPreferences.getString("USERS", null);
            Users customer = new Gson().fromJson(cachedUsers, Users.class);
            if (customer == null){
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
            }else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("USERS");
                editor.apply();
            }
            return true;
        }
        if (id == R.id.menuHome) {

            Intent intent2 = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSlider() {
        binding.carousel.addData(new CarouselItem("https://tinhte.vn/store/2017/01/3949514_CV.png", "Tu lanh Panasonic promax"));
        binding.carousel.addData(new CarouselItem("https://s3.cloud.cmctelecom.vn/tinhte1/2017/05/4036405_image001.jpg", "Tivi LG Vip promax"));
        binding.carousel.addData(new CarouselItem("https://t4.ftcdn.net/jpg/02/61/01/87/360_F_261018762_f15Hmze7A0oL58Uwe7SrDKNS4fZIjLiF.jpg", "Giam gia den 45%"));


    }

    void initCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Các loại Tivi ngon ", "", "#18ab4e", "Some description", 1));
        categories.add(new Category("Các loại Máy lạnh ngon", "", "#fb0504", "Some description", 2));
        categories.add(new Category("Các loại Bếp ngon", "", "#ff870e", "Some description", 3));
        categories.add(new Category("Khác", "", "#ff6f52", "Some description", 4));


        categoryAdapter = new CategoryAdapter(this, categories);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }


    void initProducts() {
        ArrayList<Product> productsList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
        String cachedProducts = sharedPreferences.getString("PRODUCTS_LIST", null);
        //call api
        ProductApiService.productApiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    productsList.addAll(productList);
                    //lưu dữ liệu vào bộ nhớ cache
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String productsJson = new Gson().toJson(productsList);
                    editor.putString("PRODUCTS_LIST", productsJson);
                    editor.apply();
                    // Tiếp tục xử lý danh sách sản phẩm tại đây

                    productAdapter = new ProductAdapter(HomeActivity.this, productsList);

                    GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
                    binding.productList.setLayoutManager(layoutManager);
                    binding.productList.setAdapter(productAdapter);
                } else {
                    // Xử lý lỗi nếu có
                    // tải lại danh sách dữ liệu từ bộ nhớ
                    List<Product> productsList = new Gson().fromJson(cachedProducts, new TypeToken<List<Product>>() {
                    }.getType());
                    //tạo màn hình
                    productAdapter = new ProductAdapter(HomeActivity.this, (ArrayList<Product>) productsList);
                    GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
                    binding.productList.setLayoutManager(layoutManager);
                    binding.productList.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Xử lý lỗi nếu có
                // tải lại danh sách dữ liệu từ bộ nhớ
                List<Product> productsList = new Gson().fromJson(cachedProducts, new TypeToken<List<Product>>() {
                }.getType());
                productAdapter = new ProductAdapter(HomeActivity.this, (ArrayList<Product>) productsList);
                GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
                binding.productList.setLayoutManager(layoutManager);
                binding.productList.setAdapter(productAdapter);
            }
        });


    }

    public void setProductSearchAdapter() {

        ProductApiService.productApiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();

                    //set adapter
                    atcProductSearch = findViewById(R.id.atc_product_search);
                    SearchAdapter productSearchAdapter = new SearchAdapter(HomeActivity.this, R.layout.item_search, productList);
                    atcProductSearch.setAdapter(productSearchAdapter);

                    atcProductSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                            intent.putExtra("name", productList.get(position).getName());
                            intent.putExtra("image", productList.get(position).getImage());
                            intent.putExtra("price", productList.get(position).getPrice());
                            intent.putExtra("status", productList.get(position).getStatus());
                            HomeActivity.this.startActivity(intent);

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to retrieve data from API", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
