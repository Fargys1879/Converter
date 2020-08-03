package com.example.converter.models;

import javax.persistence.*;

@Entity
@Table(name = "valutes")
public class Valute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pk;
    private String idValute;
    private String numCode;
    private String charCode;
    private int nominal;
    private String name;
    private float value;

    public Valute() {
    }

    public Valute(String idValute, String numCode, String charCode, int nominal, String name, float value) {
        this.idValute = idValute;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    public Valute(String numCode, String charCode, int nominal, String name) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
    }

    public Valute(String idValute, String numCode, String charCode, int nominal, String name) {
        this.idValute = idValute;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
    }

    public String getIdValute() {
        return idValute;
    }

    public void setIdValute(String idValute) {
        this.idValute = idValute;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
