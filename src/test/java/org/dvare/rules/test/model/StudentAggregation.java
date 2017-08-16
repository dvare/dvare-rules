package org.dvare.rules.test.model;


public class StudentAggregation {

    private int count;
    private float sgpa;
    private int age;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getSgpa() {
        return sgpa;
    }

    public void setSgpa(float sgpa) {
        this.sgpa = sgpa;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "StudentAggregation{" +
                "count=" + count +
                ", sgpa=" + sgpa +
                ", age=" + age +
                '}';
    }
}
