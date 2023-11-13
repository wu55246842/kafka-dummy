package com.laowudi.service;

import com.laowudi.entity.Person;
import org.springframework.stereotype.Service;

@Service
public class TestService {


    public void doIt(Person p){
        updateSomething(p);
    }

    public static void updateSomething(Person p) {

        String name = p.getName();
        String gender = p.getGender();
        String age = String.valueOf(p.getAge());

        //System.out.println("Name: " + p.getName() + ", Gender: " + p.getGender() + ", Age: " + p.getAge());
        if (!name.equals(gender) && !name.equals(age) && !gender.equals(age)) {
            System.out.println("Name: " + name + ", Gender: " + gender + ", Age: " + age);
        } else {
            //System.out.println("Some or all of the attributes are equal.");
        }
    }

}
