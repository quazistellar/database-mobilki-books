package com.example.db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView bookNameTextView, bookAuthorTextView;
    private EditText bookNameEditText, bookAuthorEditText;
    private Button updateButton, deleteButton, editButton;
    private DataBaseHelper dbHelper;
    private int bookId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details_activity);

        bookNameTextView = findViewById(R.id.book_name_text);
        bookAuthorTextView = findViewById(R.id.book_author_text);
        bookNameEditText = findViewById(R.id.book_name_edit);
        bookAuthorEditText = findViewById(R.id.book_author_edit);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.edit_button);

        dbHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        bookId = intent.getIntExtra("bookId", -1);

        if (bookId != -1) {
            loadBookDetails();
            bookNameEditText.setVisibility(View.GONE);
            bookAuthorEditText.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        } else {
            bookNameTextView.setVisibility(View.GONE);
            bookAuthorTextView.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookNameTextView.setVisibility(View.GONE);
                bookAuthorTextView.setVisibility(View.GONE);
                bookNameEditText.setVisibility(View.VISIBLE);
                bookAuthorEditText.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                bookNameEditText.setText(bookNameTextView.getText());
                bookAuthorEditText.setText(bookAuthorTextView.getText());
            }
        });


        updateButton.setOnClickListener(v -> updateBook());
        deleteButton.setOnClickListener(v -> deleteBook());
    }

    private void loadBookDetails() {
        Book book = dbHelper.getBookById(bookId);
        if (book != null) {
            bookNameTextView.setText(book.getBook_Name());
            bookAuthorTextView.setVisibility(View.VISIBLE);
            bookAuthorTextView.setText(book.getBook_Author());
            bookNameTextView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Не найдено", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateBook() {
        String updatedName = bookNameEditText.getText().toString().trim();
        String updatedAuthor = bookAuthorEditText.getText().toString().trim();

        if (updatedName.isEmpty() || updatedAuthor.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.updateBook(bookId, updatedName, updatedAuthor);
        if (success) {
            Toast.makeText(this, "Книга была изменена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка в изменении", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        boolean success = dbHelper.deleteBook(bookId);
        if (success) {
            Toast.makeText(this, "Книга была удалена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка в удалении книги", Toast.LENGTH_SHORT).show();
        }
    }
}

