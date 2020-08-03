package com.example.converter.models;

import javax.persistence.*;

@Entity
@Table(name = "valutes")
public class Valute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pk;
    private String idValute;
    private short numCode;
    private String charCode;
    private int nominal;
    private String name;
    private double value;

    public Valute() {
    }

    public Valute(String idValute, short numCode, String charCode, int nominal, String name, double value) {
        this.idValute = idValute;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }


    public Valute(short numCode, String charCode, int nominal, String name, double value) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    public String getIdValute() {
        return idValute;
    }

    public void setIdValute(String idValute) {
        this.idValute = idValute;
    }

    public short getNumCode() {
        return numCode;
    }

    public void setNumCode(short numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
