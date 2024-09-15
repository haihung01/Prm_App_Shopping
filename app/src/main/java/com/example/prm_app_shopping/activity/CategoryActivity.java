package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.adapters.ProductAdapter;
import com.example.prm_app_shopping.adapters.SearchAdapter;
import com.example.prm_app_shopping.api.CategoryApiService;
import com.example.prm_app_shopping.api.ProductApiService;
import com.example.prm_app_shopping.databinding.ActivityCategoryBinding;
import com.example.prm_app_shopping.model.Product;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;

    ProductAdapter productAdapter;
    ArrayList<Product> products;
    ImageView card, history, menu;
    AutoCompleteTextView atcProductSearch;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuLogout: {
                    startActivity(new Intent(CategoryActivity.this, Login.class));
                    return true;
                }
                case R.id.menuHome: {
                    startActivity(new Intent(CategoryActivity.this, HomeActivity.class));
                    return true;
                }
            }
            return true;
        });

        card = (ImageView) findViewById(R.id.iconCard);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        history = (ImageView) findViewById(R.id.iconHistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, History.class);
                startActivity(intent);
            }
        });

        initProducts();
        setProductSearchAdapter();
        initDrawerLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        if(id == R.id.menuLogout) {

            Intent intent = new Intent(CategoryActivity.this, Login.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.menuHome) {

            Intent intent2 = new Intent(CategoryActivity.this, HomeActivity.class);
            startActivity(intent2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void initProducts(){
        products = new ArrayList<>();
        Intent intent = getIntent();
        int category = intent.getIntExtra("category",0);
        CategoryApiService.CategoryApiService.getProductsByCategoryId(category).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> productList = response.body();
                products.addAll(productList);
                productAdapter = new ProductAdapter(CategoryActivity.this, products);

                GridLayoutManager layoutManager = new GridLayoutManager(CategoryActivity.this, 2);
                binding.productList.setLayoutManager(layoutManager);
                binding.productList.setAdapter(productAdapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });

    }
    private  void initDrawerLayout(){
        menu = (ImageView) findViewById(R.id.iconMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getRoot().openDrawer(GravityCompat.START);

            }
        });
    }
    public void setProductSearchAdapter() {
        products = new ArrayList<>();
        Intent intent = getIntent();
        int category = intent.getIntExtra("category", 0);
        ProductApiService.productApiService.getProductsByCategoryId(category).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();

                    //set adapter
                    atcProductSearch = findViewById(R.id.atc_product_search);
                    SearchAdapter productSearchAdapter = new SearchAdapter(CategoryActivity.this, R.layout.item_search, productList);
                    atcProductSearch.setAdapter(productSearchAdapter);

//                     Sau khi chọn item search sẽ chuyển sang fragment detail
                    atcProductSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            ProductAdapter ab = new ProductAdapter(MainActivity.this, );
                            Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
                            intent.putExtra("name", productList.get(position).getName());
                            intent.putExtra("image", productList.get(position).getImage());
                            intent.putExtra("price", productList.get(position).getPrice());
                            intent.putExtra("status", productList.get(position).getStatus());
                            CategoryActivity.this.startActivity(intent);

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Failed to retrieve data from API", Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", "onFailure: " + t.getMessage());
            }
        });

    }
}