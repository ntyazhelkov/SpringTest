package ru.appline.controller;

import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private static final PetModel petModel = PetModel.getInstance();
    private static final AtomicInteger newId = new AtomicInteger(1);

    @PostMapping(value = "/createPet", consumes = "application/json", produces = "application/json")
    public Map<String, String> createPet(@RequestBody Pet pet) {
        petModel.add(pet, newId.getAndIncrement());
        Map<String, String> message = new HashMap<String, String>();
        message.put("Message", "Питомец успешно создан");
        return message;
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Map<Integer, Pet> getAll() {
        return petModel.getAll();
    }

    @GetMapping(value = "/getPet", consumes = "application/json", produces = "application/json")
    public Pet getPet(@RequestBody Map<String, Integer> id) {
        return petModel.getFromList(id.get("id"));
    }


    @DeleteMapping(value = "/delPet", consumes = "application/json", produces = "application/json")
    public Map<String, String> deletePets(@RequestBody Map<String, Integer> id) {
        Map<String, String> message = new HashMap<String, String>();
        petModel.getAll().remove(id.get("id"));
        message.put("Message", "Питомец успешно удалён");
        return message;
    }

    @PutMapping(value = "/putPet", consumes = "application/json", produces = "application/json")
    public Map<Integer, Pet> putPet(@RequestBody Map<String, String> pet) {
        Pet mainPet = new Pet(pet.get("name"), pet.get("type"), Integer.parseInt(pet.get("age")));
        petModel.getAll().put(Integer.parseInt(pet.get("id")), mainPet);
        return petModel.getAll();
    }
}
