package dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.Location;

public class LocationDAO {

    private List<Location> locationsList = new ArrayList<>();
    private String contextPath;

    public LocationDAO() {}
    public LocationDAO(String contextPath) {
        this.contextPath = contextPath;
        loadLocationsFromFile();
    }

    public Collection<Location> getAll() {
        return locationsList;
    }
    
    private void loadLocationsFromFile() {
        try (Reader reader = new FileReader(new File(contextPath, "resources/locations.json"))) {
            Gson gson = new Gson();
            List<Location> loadedLocations = gson.fromJson(reader, new TypeToken<List<Location>>() {}.getType());

            if (loadedLocations != null) {
                locationsList.clear(); // Clear existing locations
                locationsList.addAll(loadedLocations);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading locations from file: " + e.getMessage());
        }
    }
}
