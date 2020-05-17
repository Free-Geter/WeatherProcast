package com.example.weatherprocast.DataBase;

public class dbContract {

    public dbContract() {
    }

    public static class ContactEntry{
        public static final String TABLE_NAME = "city_database";
        public static final String CONTACT_ID = "_id";
        public static final String NAME = "city_name";
        public static final String CONTENT = "city_content";
    }
}
