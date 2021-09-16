package com.superhao.part_time_job.serialize;


import com.superhao.base.util.SSerializeUtil;
import org.apache.tomcat.websocket.WsSession;

import java.io.IOException;

import java.io.InvalidObjectException;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.io.ObjectStreamException;

import java.io.Serializable;

/**
 * @Auther: super
 * @Date: 2019/11/6 21:50
 * @email:
 */
public class SerializablePerson implements Serializable

{

    private String lastName;

    private String firstName;

    private CityState cityAndState;
    private WsSession session;

    public SerializablePerson(
            final String newLastName, final String newFirstName,
            final CityState newCityAndState, final WsSession session)

    {

        this.lastName = newLastName;

        this.firstName = newFirstName;

        this.cityAndState = newCityAndState;
        this.session = session;

    }

    public SerializablePerson(String newLastName, String newFirstName, CityState newCityAndState) {


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

    /**
     * Serialize this instance.
     *
     * @param out Target to which this instance is written.
     * @throws IOException Thrown if exception occurs during serialization.
     */

    private void writeObject(final ObjectOutputStream out) throws IOException

    {

        out.writeUTF(this.lastName);

        out.writeUTF(this.firstName);

        out.writeUTF(this.cityAndState.getCityName());

        out.writeUTF(this.cityAndState.getStateName());

    }

    /**
     * Deserialize this instance from input stream.
     *
     * @param in Input Stream from which this instance is to be deserialized.
     * @throws IOException            Thrown if error occurs in deserialization.
     * @throws ClassNotFoundException Thrown if expected class is not found.
     */

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException

    {

        this.lastName = in.readUTF();

        this.firstName = in.readUTF();

        this.cityAndState = new CityState(in.readUTF(), in.readUTF());

    }

    private void readObjectNoData() throws ObjectStreamException

    {

        throw new InvalidObjectException("Stream data required");

    }

    public static void main(String[] args) {
        final SerializablePerson personIn = new SerializablePerson("Flintstone", "Fred", new CityState("Bedrock", "Cobblestone"));

        SSerializeUtil.serialize(personIn, "person1.dat");

        final SerializablePerson personOut = SSerializeUtil.deserialize("person1.dat", SerializablePerson.class);
        System.out.println(personOut.cityAndState.toString());
    }

}
