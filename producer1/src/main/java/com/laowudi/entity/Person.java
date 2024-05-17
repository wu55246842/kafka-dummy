package com.laowudi.entity;

public class Person {
    private String name;
    private int age;
    private String gender;

    private String imageData;

    // 构造器
    public Person() {
    }

    public Person(String name, int age, String gender,String imageData) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.imageData = imageData;
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    // toString 方法，用于输出 Person 对象的信息
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}

