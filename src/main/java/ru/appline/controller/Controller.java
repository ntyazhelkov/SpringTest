package ru.appline.controller;

import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.HashMap;
import java.util.Iterator;
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
        int i = 0;
        for(Iterator<Integer> iterator = petModel.getAll().keySet().iterator(); iterator.hasNext(); ) {
            Integer key = iterator.next();
            if(key == id.get("id")) {
                iterator.remove();
                i++;
            }
            message.put("Message", "Питомец успешно удалён");
        }
        if (i == 0) {
            message.put("Message", "Такого питомца нет!");
        }
        return message;
    }

    @PutMapping(value = "/putPet", consumes = "application/json", produces = "application/json")
    public Map<String, String> putPet(@RequestBody Map<String, String> pet) {
        Pet mainPet = new Pet(pet.get("name"), pet.get("type"), Integer.parseInt(pet.get("age")));
        Map<String, String> message = new HashMap<String, String>();
        if (Integer.parseInt(pet.get("id")) > 0) {
            int i = 0;
            for(Iterator<Integer> iterator = petModel.getAll().keySet().iterator(); iterator.hasNext(); ) {
                Integer key = iterator.next();
                if(key == Integer.parseInt(pet.get("id"))) {
                    petModel.getAll().put(Integer.parseInt(pet.get("id")), mainPet);
                    i++;
                }
                message.put("Message", "Питомец успешно изменен");
            }
            if (i == 0) {
                message.put("Message", "Такого питомца нет");
            }
        } else {
            message.put("Message", "ID должен быть больше нуля!");
        }
        return message;
    }
}
