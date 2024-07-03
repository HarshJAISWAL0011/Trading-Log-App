package com.example.dcxlogs;

import com.google.firebase.database.FirebaseDatabase;

public class Application extends android.app.Application {
    @Override
    public void onCreate () {
        super.onCreate ( );
        FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
        databaseReference.setPersistenceEnabled ( true );
    }
}
