package mobile.li.librarymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static mobile.li.librarymanagement.LibrarianMainActivity.KEY_NAME;

public class BookAdderActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mLibrarianId;
    private String mLibrarianEmail;

    String name = "";
    String author = "";
    String title = "";
    String call_number = "";
    String publisher = "";
    String year = "";
    String location = "";
    String copies = "";
    String status = "";
    String keywords = "";
    String image_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_adder);
        setTitle("Librarian Add New Book");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mLibrarianId = mFirebaseUser.getUid();
        mLibrarianEmail = mFirebaseUser.getEmail();


        findViewById(R.id.book_adder_addButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveBook();
                if(name == " " || author == " " || title == " " || call_number == " " || publisher == " " || year == " " ||
                        location == " " || copies == " " || status == " " || keywords == " " || image_path == " "){
                    Toast.makeText(BookAdderActivity.this, "Please fill all the blank", Toast.LENGTH_LONG).show();
                }else if(!isNumber(copies)){
                    Toast.makeText(BookAdderActivity.this, "Please enter a valid number on the copies field", Toast.LENGTH_LONG).show();
                }else if (!(status.equals("ONLINE") || status.equals("OFFLINE"))){
                    Toast.makeText(BookAdderActivity.this, "Please enter online or offline on the status field", Toast.LENGTH_LONG).show();
                }
                else {
                    mDatabase.child("books")
                            .orderByChild("bookName")
                            .equalTo(name)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren()){
                                        Toast.makeText(BookAdderActivity.this, "Book with same name had been added", Toast.LENGTH_LONG).show();
                                    }else{
                                        Book book = new Book(name);
                                        book.setBookCreatedBy(mLibrarianId);
                                        book.setBookCreateByEmail(mLibrarianEmail);
                                        book.setBookAuthor(author);
                                        book.setBookTitle(title);
                                        book.setBookCallNumber(call_number);
                                        book.setBookPublisher(publisher);
                                        book.setBookYear(year);
                                        book.setBookLocation(location);
                                        book.setBookCopies(Integer.parseInt(copies));
                                        book.setBookStatus(status);
                                        book.setBookKeywords(keywords);
                                        book.setBookImagePath(image_path);
                                        mDatabase.child("books").push().setValue(book);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }
            }
        });
    }

    private void saveBook() {

        name = ((EditText) findViewById(R.id.book_adder_name)).getText().toString().toUpperCase();
        author = ((EditText) findViewById(R.id.book_adder_author)).getText().toString().toUpperCase();
        title = ((EditText) findViewById(R.id.book_adder_title)).getText().toString().toUpperCase();
        call_number = ((EditText) findViewById(R.id.book_adder_number)).getText().toString().toUpperCase();
        publisher = ((EditText) findViewById(R.id.book_adder_publisher)).getText().toString().toUpperCase();
        year = ((EditText) findViewById(R.id.book_adder_year)).getText().toString().toUpperCase();
        location = ((EditText) findViewById(R.id.book_adder_location)).getText().toString().toUpperCase();
        copies = ((EditText) findViewById(R.id.book_adder_copies)).getText().toString().toUpperCase();
        status = ((EditText) findViewById(R.id.book_adder_status)).getText().toString().toUpperCase();
        keywords = ((EditText) findViewById(R.id.book_adder_keyword)).getText().toString().toUpperCase();
        image_path = ((EditText) findViewById(R.id.book_adder_imagePath)).getText().toString().toUpperCase();

        if(name == null || name.trim().equals("")){
            name = " ";
        }
        if(author == null || author.trim().equals("")){
            author = " ";
        }
        if(title == null || title.trim().equals("")){
            title = " ";
        }
        if(call_number == null || call_number.trim().equals("")){
            call_number = " ";
        }
        if(publisher == null || publisher.trim().equals("")){
            publisher = " ";
        }
        if(year == null || year.trim().equals("")){
            year = " ";
        }
        if(location == null || location.trim().equals("")){
            location = " ";
        }
        if(copies == null || copies.trim().equals("")){
            copies = " ";
        }
        if(status == null || status.trim().equals("")){
            status = " ";
        }
        if(keywords == null || keywords.trim().equals("")){
            keywords = " ";
        }
        if(image_path == null || image_path.trim().equals("")){
            image_path = " ";
        }
    }

    private boolean isNumber(String s){
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");

    }

}
