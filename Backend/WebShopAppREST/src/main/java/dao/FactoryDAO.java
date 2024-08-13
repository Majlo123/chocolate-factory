package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import beans.Chocolate;
import beans.Factory;

public class FactoryDAO {

    private List<Factory> factoryList = new ArrayList<>();
    private String contextPath;

    public FactoryDAO() {}
    public FactoryDAO(String contextPath) {
        this.contextPath = contextPath;
        loadFactoriesFromFile();
    }

    public List<Factory> getAll() {
        return factoryList;
    }
    public List<Factory> searchFactories(String name, String chocolateName, String location, Double averageRating) {
        return factoryList.stream()
                .filter(factory -> (name == null || factory.getName().toLowerCase().contains(name.toLowerCase())) &&
                                   (chocolateName == null || factory.getChocolates().stream().anyMatch(chocolate -> chocolate.getName().toLowerCase().contains(chocolateName.toLowerCase()))) &&
                                   (location == null || factory.getLocation().getCity().toLowerCase().contains(location.toLowerCase()) || factory.getLocation().getState().toLowerCase().contains(location.toLowerCase())) &&
                                   (averageRating == null || factory.getAverageRating() >= averageRating))
                .collect(Collectors.toList());
    }
    private void loadFactoriesFromFile() {
        try (Reader reader = new FileReader(new File(contextPath, "resources/factories.json"))) {
            Gson gson = new Gson();
            List<Factory> loadedFactories = gson.fromJson(reader, new TypeToken<List<Factory>>() {}.getType());

            if (loadedFactories != null) {
                factoryList.clear(); // Clear existing factories
                factoryList.addAll(loadedFactories);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading factories from file: " + e.getMessage());
        }
    }
    private void saveFactoriesToFile() {
        try (FileWriter writer = new FileWriter(new File(contextPath, "resources/factories.json"))) {
            Gson gson = new Gson();
            gson.toJson(factoryList, writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving factories to file: " + e.getMessage());
        }
    }
    public Factory getById(int id) {
        for (Factory factory : factoryList) {
            if (factory.getId() == id) {
                return factory;
            }
        }
        return null;
    }
    public Factory create(Factory factory) {
    	factory.setId(nextId());
    	factoryList.add(factory);
        saveFactoriesToFile();
        return factory;
    }

    public int nextId() {
        int maxId = -1;
        for (Factory factory : factoryList) {
            if (maxId < factory.getId()) {
                maxId = factory.getId();
            }
        }
        maxId++;
        return maxId;
    }
    public void updateFactory(Factory factory) {
        for (int i = 0; i < factoryList.size(); i++) {
            if (factoryList.get(i).getId() == factory.getId()) {
                factoryList.set(i, factory);
                break;
            }
        }

        try (Writer writer = new FileWriter(new File(contextPath, "resources/factories.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(factoryList, writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error updating factory in file: " + e.getMessage());
        }
    }

    public void deleteChocolateFromFactory(int chocolateId, int factoryId) {
        Factory factory = getById(factoryId);
        if (factory != null) {
            Chocolate chocolateToRemove = null;
            for (Chocolate chocolate : factory.getChocolates()) {
                if (chocolate.getId() == chocolateId) {
                    chocolateToRemove = chocolate;
                    break;
                }
            }
            if (chocolateToRemove != null) {
                factory.getChocolates().remove(chocolateToRemove);
                updateFactory(factory);
            }
        }
    }
    public void deleteFactory(int factoryId) {
        Factory factory = getById(factoryId);
        if (factory != null) {
            factoryList.remove(factory);
            saveFactoriesToFile();
        } 
   
    }
    }

