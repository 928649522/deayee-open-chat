package com.superhao.part_time_job.serialize;

/**
 * @Auther: super
 * @Date: 2019/11/6 21:51
 * @email:
 */
import java.io.Serializable;

/**

 * Person class.

 *

 * @author Dustin

 */

public class Person implements Serializable

{

    private String lastName;

    private String firstName;

    private CityState cityAndState;

    public Person(

            final String newLastName, final String newFirstName,

            final CityState newCityAndState)

    {

        this.lastName = newLastName;

        this.firstName = newFirstName;

        this.cityAndState = newCityAndState;

    }

    public String getFirstName()

    {

        return this.firstName;

    }

    public String getLastName()

    {

        return this.lastName;

    }

    @Override

    public String toString()

    {

        return this.firstName + " " + this.lastName + " of " + this.cityAndState;

    }

}