package com.codepath.android.booksearch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.adapter.BookAdapter;
import com.codepath.android.booksearch.api.BookApi;
import com.codepath.android.booksearch.model.Book;
import com.codepath.android.booksearch.model.BookSearch;
import com.codepath.android.booksearch.utils.RetrofitUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookListActivity extends AppCompatActivity {
    private BookAdapter mBookAdapter;
    private BookApi mBookApi;

    @BindView(R.id.lvBooks)
    ListView lvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);

        // initialize api
        mBookApi = RetrofitUtils.get().create(BookApi.class);
        // initialize the adapter
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        // attach the adapter to the ListView
        lvBooks.setAdapter(mBookAdapter);
        // Fetch the data remotely
        fetchBooks("Oscar Wilde");
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {
        mBookApi.search(query).enqueue(new Callback<BookSearch>() {
            @Override
            public void onResponse(Call<BookSearch> call, Response<BookSearch> response) {
                handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<BookSearch> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void handleResponse(BookSearch bookSearch) {
        mBookAdapter.setBooks(bookSearch.getBooks());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
